(ns money-sync.my-migrations
  (:require [migratus.core :as migratus]))

(def config {:store                :database
             :migration-dir        "migrations/"
             ;:init-script          "init.sql"
             ;defaults to true, some databases do not support
             ;schema initialization in a transaction
             ;:init-in-transaction? false
             ;:migration-table-name "foo_bar"
             :db                   {:classname   "org.postgresql.Driver"
                                    :subprotocol "postgresql"
                                    :subname     "//db:5432/"}})

;initialize the database using the 'init.sql' script
(migratus/init config)

(defn up
  []
  (println "Applying pending migrations...")
  (migratus/migrate config)
  (println "Done!"))

;rollback the last migration applied
(migratus/rollback config)

;bring up migrations matching the ids
(migratus/up config 20111206154000)

;bring down migrations matching the ids
(migratus/down config 20111206154000)

