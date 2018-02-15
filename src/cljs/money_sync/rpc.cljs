(ns money-sync.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc accounts nil)
(defc error nil)
(defc loading [])

(add-watch error nil (fn [_ _ _ error] (when error (js/console.error (.-serverStack error)))))
(add-watch loading nil (fn [_ _ _ loading] (js/console.info (clj->js (map #(.state %) loading)))))

(def list-accounts
  (mkremote 'money-sync.api/list-accounts accounts error loading))

(def create-account
  (mkremote 'money-sync.api/create-account accounts error loading))

(def update-account
  (mkremote 'money-sync.api/update-account accounts error loading))

(def delete-account
  (mkremote 'money-sync.api/delete-account accounts error loading))
