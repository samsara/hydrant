(ns hydrant.flows.samsara
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [taoensso.timbre :as log]))

(def ^:private default-options {:url "http://localhost:9000/v1/events"})


(defn samsara
  ([]
   (samsara default-options))

  ([opts]
   (let [url (:url opts)]
     (fn [events]
       (let [resp  (http/post url
                               {:headers {"Content-Type" "application/json"
                                          "X-Samsara-publishedTimestamp" (System/currentTimeMillis)}
                                :body (json/write-str events)})]
         (log/debug "Sent " (count events) " events to samsara. Http Response status " (:status @resp)))))))



