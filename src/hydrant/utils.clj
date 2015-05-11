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


(defmacro call-flows [events  flows]
  `(do
     ~@(map #(list % events) flows))
;;  `(do
;;     (doseq [flow# ~flows]
;;       (try
;;         (flow# ~events)
;;         (catch Throwable t#
;;           (log/warn t#))))
;;  )
)

(defmacro filter-source [pred & body]
  `(fn [events#]
     (when (~pred (-> events# first :eventSource))
      (call-flows events# ~body))))


(defn sensibly-print-events [events]
  (let [one (first events)]
    (log/info "Processing " (count events) " events. All possibly with :eventName [" (:eventName one) "] and properties " (keys one) )))

(defn log-level [level]
  (log/set-level! level))
