(ns hydrant.service)


(defprotocol Service

  (start [service])

  (stop [service]))
