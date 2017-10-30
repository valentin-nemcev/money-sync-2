(ns money-sync.core
  (:require [money-sync.my-migrations :as migr])
  (:gen-class))

(defn -main
  [& args]
  (migr/up))
