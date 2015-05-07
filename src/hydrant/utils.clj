(ns hydrant.utils
  (:require [taoensso.timbre :as log]))


(defn- print-minimal-info [event]
  (let [mini-event (select-keys event [:timestamp :sourceId :eventName])]
    mini-event))

(defn- print-summary [events]
  (let [one (first events)]
    (log/info "Processing " (count events) " events. All possibly with :eventName [" (:eventName one)"]")))

(defn sensibly-print-events [events]
  (if (< (count events) 10)
    (log/info "Processing " (count events) " events -> " (mapv print-minimal-info events))
    (print-summary events)))

(defn log-level [level]
  (log/set-level! level))
