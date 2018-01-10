(ns money-sync.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc accounts nil)
(defc error nil)
(defc loading [])

(add-watch error nil (fn [_ _ _ error] (js/console.error (.-serverStack error))))

(def list-accounts
  (mkremote 'money-sync.api/list-accounts accounts error loading))

(def create-account
  (mkremote 'money-sync.api/create-account accounts error loading))

