(ns money-sync.api
  (:require [castra.core :refer [defrpc *session*]]
            [money-sync.db :as db]))

(defrpc get-state []
  (swap! *session* update-in [:id] #(or % (rand-int 100)))
  {:random (rand-int 100) :session (:id @*session*)})

(defrpc list-accounts
  []
  (db/list-accounts))

(defrpc create-account
  [fields]
  (db/create-account fields)
  (db/list-accounts))

(defrpc delete-account
  [id]
  (db/delete-account id))
