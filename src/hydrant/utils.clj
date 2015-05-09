(ns hydrant.utils
  (:require [taoensso.timbre :as log]))


(defn sensibly-print-events [events]
  (let [one (first events)]
    (log/info "Processing " (count events) " events. All possibly with :eventName [" (:eventName one) "] and properties " (keys one) )))

(defn log-level [level]
  (log/set-level! level))
