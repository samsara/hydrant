(ns hydrant.utils
  (:require [taoensso.timbre :as log]))

(defn match-any
  ([poss-vals]
   (fn [target-val]
     (match-any poss-vals target-val)))

  ([poss-vals target-val]
   (match-any #(.equals %1 %2) poss-vals target-val))

  ([f poss-vals target-val]
   (some (partial f target-val) poss-vals)))


(defmacro filter-source [pred & body]
  `(fn [events#]
     (when (~pred (-> events# first :eventSource))
       (doseq [f# [~@body]]
         (try
           (f# events#)
           (catch Throwable t#
             (log/warn f# " threw " t#)))))))


(defn sensibly-print-events [events]
  (let [one (first events)]
    (log/info "Processing " (count events) " events. All possibly with :eventName [" (:eventName one) "] and properties " (keys one) )))

(defn log-level [level]
  (log/set-level! level))
