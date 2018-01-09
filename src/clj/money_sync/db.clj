(ns money-sync.db
  (:require
    [honeysql.core :as sql]
    [honeysql.helpers :refer :all]
    [honeysql-postgres.helpers :refer :all]
    [clojure.java.jdbc :as jdbc]))

(def db {:dbtype   "postgres"
         :dbname   "postgres"
         :user     "postgres"
         :password "postgres"})

(defn list-accounts
  []
  (jdbc/query db (sql/format {:select [:*] :from [:account]})))

(defn create-account
  [name]
  (jdbc/query db (sql/format (-> (insert-into :account)
                                 (columns :id :name)
                                 (values [[(sql/call :nextval "account_id_seq") name]])))))

(defn delete-account
  [name]
  (jdbc/query db (sql/format (-> (delete-from :account)
                                 (where [:= :name name])))))
