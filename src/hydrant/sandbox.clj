(ns hydrant.sandbox
  (:require [hydrant.core :refer [flows]]
            [hydrant.utils :refer [match-any filter-source sensibly-print-events log-level]]
            [hydrant.sources.twitter :refer [twitter-source]]
            [hydrant.sources.web :refer [web-source]]
            [hydrant.flows.samsara :refer [samsara]]
            [taoensso.timbre :as log]))


(defn load-forms [f]
  (binding [*ns* (find-ns 'hydrant.sandbox)]
    (in-ns 'hydrant.sandbox)
    (load-file f)))
