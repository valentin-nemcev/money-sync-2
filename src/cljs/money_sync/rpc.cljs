(ns money-sync.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc state nil)
(defc error nil)
(defc loading [])

(defc accounts [{:id 1 :name "test"}
                {:id 2 :name "another test"}])

(defc= random-number  (get state :random))
(defc= session-number (get state :session))

(def get-state
  (mkremote 'money-sync.api/get-state state error loading))

(def timer (atom nil))

(defn init []
  (get-state)
  (swap! timer #(do
                  (js/clearInterval %)
                  (js/setInterval get-state 1000))))
