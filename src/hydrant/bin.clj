(ns hydrant.bin
  (:require [hydrant.sandbox :as sandbox]
            [taoensso.timbre :as log])
  (:gen-class))

(defn -main
  ([]
   (-main "hydrant.config"))

  ([cfg]
   (try
     (sandbox/load-forms cfg)
     (catch Exception e
       (log/error "Not able to start" e)))))

