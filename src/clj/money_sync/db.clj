(ns money-sync.db
  (:require
    [honeysql.core :as sql]
    [clojure.java.jdbc :as jdbc]))

(def db {:dbtype   "postgres"
         :dbname   "postgres"
         :user     "postgres"
         :password "postgres"})

(defn list-accounts
  []
  (jdbc/query db (sql/format {:select [:*] :from [:account]})))
