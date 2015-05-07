(ns hydrant.sources.web
  (:require [itsy.core :as spider]
            [hydrant.service :refer [Service]]
            [hydrant.core :refer [add-data-source data-to-flows]]
            [taoensso.timbre :as log]))


(defn generate-event [root-url url body]
  {:timestamp (System/currentTimeMillis)
   :sourceId root-url
   :rootUrl root-url
   :eventName "web-crawl"
   :url url
   :body body
   :bodyByteSize (count (.getBytes body))})


(defn crawler-fn [root-url url body]
  (let [event (generate-event root-url url body)]
    (data-to-flows event)))

(defn create-crawler [root-url url-limit]
  (spider/crawl {:url root-url
                 :handler (fn [{:keys [url body]}] (crawler-fn root-url url body))
                 :workers 3
                 :url-limit url-limit
                 :url-extractor spider/extract-all
                 :http-opts {}
                 :host-limit true
                 :polite? true}))


(defrecord WebSource [^String root-url
                      ^Integer  url-limit
                      crawler
                      running]

  Service
  (start [this]
    (locking this
      (when-not @running
        (reset! running true)
        (reset! crawler (create-crawler root-url url-limit))
        (log/info "Started WebCrawlerSource"))))

  (stop [this]
    (locking this
      (when @running
        (spider/stop-workers @crawler) ;;blocking call
        (reset! running false)
        (log/info "Stopped WebCrawlerSource")))))


(defn web-source
  [{:keys [ root-url page-crawl-limit]}]
  (let [source (WebSource. root-url page-crawl-limit (atom nil) (atom nil))]
    (add-data-source :web-source source)))

(comment

  (defn dtf [event]
    (let [print-friendly (select-keys event [:timestamp :sourceId :rootUrl :eventName :url :bodyByteSize])]
      (println print-friendly)))

  (with-redefs [data-to-flows dtf]
    (let [service (WebSource. "http://www.google.com" 50 (atom nil) (atom nil))]
      (.start service)
      (Thread/sleep 30000) ;stay within this context (so that dtf function is used)
      (.stop service)))

  )