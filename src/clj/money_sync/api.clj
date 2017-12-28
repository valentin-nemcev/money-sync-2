(ns money-sync.api
  (:require [castra.core :refer [defrpc *session*]]
            [money-sync.core :as core]))

(defrpc get-state []
  (swap! *session* update-in [:id] #(or % (rand-int 100)))
  {:random (rand-int 100) :session (:id @*session*)})

(defrpc list-accounts
  []
  (core/list-accounts))
