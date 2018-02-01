(ns money-sync.db
  (:require
    [honeysql.core :as sql]
    [honeysql.helpers :refer :all]
    [clojure.java.jdbc :as jdbc]))

(def db {:dbtype   "postgres"
         :dbname   "postgres"
         :user     "postgres"
         :password "postgres"})

(defn list-accounts
  []
  (jdbc/query db (sql/format {:select [:*] :from [:account] :order-by [:name]})))

(defn create-account
  [{:keys [name]}]
  (jdbc/execute! db (sql/format (-> (insert-into :account)
                                    (columns :id :name)
                                    (values [[(sql/call :nextval "account_id_seq") name]]))))
  nil)

(defn delete-account
  [id]
  (jdbc/execute! db (sql/format (-> (delete-from :account)
                                    (where [:= :id id]))))
  nil)

(defn update-account
  [id {:keys [name]}]
  (jdbc/execute! db (sql/format (-> (honeysql.helpers/update :account)
                                    (sset {:name name})
                                    (where [:= :id id]))))
  nil)
