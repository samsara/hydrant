(ns hydrant.flows.samsara
  (:require [org.httpkit.client :as http]
            [samsara.client :as cli]
            [clojure.data.json :as json]
            [taoensso.timbre :as log])
  (:import [java.net InetAddress UnknownHostException]))

(def ^:private default-sourceId (str "hydrant-"
                                     (try
                                       (.getCanonicalHostName (InetAddress/getLocalHost))
                                       (catch UnknownHostException uhe
                                         "UnknownHostException"))))

(def ^:private default-config {:url "http://localhost:9000/v1"
                               :sourceId default-sourceId})


(defn event->samsara-event [{:keys [timestamp sourceId eventName]
                             :or {timestamp (System/currentTimeMillis)
                                  sourceId default-sourceId
                                  eventName "UnknownEvent"}
                             :as event}]
  (assoc event :timestamp timestamp :sourceId sourceId :eventName eventName))


(defn- send-to-endpoint [{:keys [url]} events]
  (let [events (map event->samsara-event events)
        resp  (http/post (str  url "/events")
                         {:headers {"Content-Type" "application/json"
                                    "X-Samsara-publishedTimestamp" (System/currentTimeMillis)}
                          :body (json/write-str events)})]
    (log/debug "Sent " (count events) " events directly to samsara endpoint. Http Response status " (:status @resp))))



(defn- send-via-cli [conf events ]
  (cli/set-config! conf)
  (cli/publish-events (map event->samsara-event events))
  (log/debug "Sent " (count events) " events to samsara."))

(defn samsara
  ([events]
   (samsara default-config events ))

  ([conf events]
   #_(send-via-cli conf events)
   (send-to-endpoint conf events)))

