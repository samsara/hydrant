(ns hydrant.core
  (:require [taoensso.timbre :as log]))

(def ^:private nucleus (atom  {:data-sources {}
                               :flows []}))

(defmacro call-flows [flws data]
  `(doseq [flw# ~flws]
     (try
       (flw# ~data)
       (catch Throwable t#
         (log/warn flw# " threw >>" t#)))))


(defn add-flow [s]
  (swap! nucleus update-in [:flows] #(conj %1 %2) s))


(defn flows [& args]
  (-> (fn [data]
        (call-flows args data))
      add-flow))


(defn data-to-flows [d]
  (doseq [flow (:flows @nucleus)]
    (flow d)))

(defn update-service-fn [old-s new-s]
  (when (some? old-s)
    (.stop old-s))
  (when (some? new-s)
    (.start new-s))
  new-s)

(defn add-data-source [name s]
  (swap! nucleus update-in [:data-sources name] update-service-fn s))



