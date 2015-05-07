(ns hydrant.sandbox
  (:require [hydrant.core :refer [flows]]
            [hydrant.sources.twitter :refer [twitter-source]]
            [hydrant.sources.web :refer [web-source]]
            [taoensso.timbre :as log]))


(defn load-forms [f]
  (binding [*ns* (find-ns 'hydrant.sandbox)]
    (in-ns 'hydrant.sandbox)
    (load-file f)))
