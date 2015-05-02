(ns hydrant.twitter
  (:require [hydrant.service :refer [Service]]
            [hydrant.core :refer [add-data-source data-to-flows]]
            [twitter.oauth :as oauth]
            [twitter-streaming-client.core :as client]
            [taoensso.timbre :as log]))

(def tweet-time-format "EEE MMM dd HH:mm:ss ZZZZZ yyyy")
(def tweet-date-formatter (doto (java.text.SimpleDateFormat. tweet-time-format java.util.Locale/ENGLISH)
             (.setLenient true)))

(defn default-tweet-transformer [m]
  {:timestamp (->> (:created_at m) (.parse tweet-date-formatter) (.getTime))
  :sourceId (get-in m [:user :name])
  :eventName "tweet"
  :twitter-user (get-in m [:user :name])
  :location (get-in m [:user :location])
  :tweet (:text m)})

(defn create-stream [^String con-key
                     ^String con-secret
                     ^String token
                     ^String token-secret
                     streaming-params]
  (let [creds (oauth/make-oauth-creds con-key con-secret token token-secret)]
    (client/create-twitter-stream twitter.api.streaming/statuses-filter
                                  :oauth-creds creds :params streaming-params)))

(defn process-queues [queues tweet-transformer]
  (let [tweets (:tweet queues)
        transformed-tweets (map tweet-transformer tweets)]
    (data-to-flows transformed-tweets)
    (log/debug "TwitterSource sent data to flows ->" transformed-tweets)))



(defrecord TwitterSource [^String con-key
                          ^String con-secret
                          ^String token
                          ^String token-secret
                          s-params
                          tweet-transformer
                          twitter-stream
                          running
                          thread]

  Service
  (start [this]
    (locking this
        (when-not @running
          (reset! running true)
          (let [t (Thread. (fn []
                             (let [s (create-stream con-key con-secret token token-secret s-params)]
                               (reset! twitter-stream s)
                               (client/start-twitter-stream @twitter-stream)
                               (while @running
                                 (try
                                   (process-queues (client/retrieve-queues @twitter-stream) tweet-transformer)
                                   (Thread/sleep 1000)
                                   (catch InterruptedException ie
                                     nil))))))]
            (reset! thread t)
            (.start t)
            (log/info "Started TwitterSource")))))

  (stop [this]
    (locking this
      (when @running
        (client/cancel-twitter-stream @twitter-stream)
        (reset! running false)
        (.interrupt ^Thread @thread)
        (while (.isAlive ^Thread @thread)
          (Thread/sleep 5))
        (log/info "Stopped TwitterSource")))))


(defn twitter-source
  ([auth-map stream-params]
   (twitter-source auth-map stream-params #'default-tweet-transformer))

  ([{:keys [con-key con-secret token token-secret]}
    stream-params
    tweet-transformer]
    (let [source  (TwitterSource. con-key con-secret token token-secret
                                  stream-params tweet-transformer
                                  (atom nil) (atom nil) (atom nil))]
      (add-data-source :twitter-source source))))
