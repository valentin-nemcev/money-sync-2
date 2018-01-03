(ns money-sync.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc accounts nil)
(defc accounts-error nil)
(defc accounts-loading [])

(def list-accounts
  (mkremote 'money-sync.api/list-accounts accounts accounts-error accounts-loading))

(defn init []
  (list-accounts))
