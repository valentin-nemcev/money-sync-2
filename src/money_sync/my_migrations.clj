(ns money-sync.my-migrations
  (:require [migratus.core :as migratus]))

(def config {:store         :database
             :migration-dir "migrations/"
             :db            {:classname   "org.postgresql.Driver"
                             :subprotocol "postgresql"
                             :subname     "//db:5432/"
                             :user        "postgres"}})

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

