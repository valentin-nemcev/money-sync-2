(def project 'money-sync)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src/clj"}
          :source-paths   #{"test" "src/cljs" "src/hl"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [org.clojure/clojurescript "1.9.495"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [migratus "1.0.0"]
                            [onetom/boot-lein-generate "0.1.3" :scope "test"]
                            [org.clojure/java.jdbc "0.7.4"]
                            [org.postgresql/postgresql "42.1.4"]
                            [honeysql "0.9.1"]
                            [nilenso/honeysql-postgres "0.2.3"]
                            [adzerk/boot-reload "0.5.1"]
                            [compojure "1.6.0-beta3"]
                            [hoplon/castra "3.0.0-alpha7"]
                            [hoplon/hoplon "6.0.0-alpha17"]
                            [pandeiro/boot-http "0.7.6"]
                            [ring "1.5.1"]
                            [ring/ring-defaults "0.2.3"]
                            [adzerk/boot-cljs "1.7.228-2"]
                            [adzerk/boot-cljs-repl "0.3.3"]
                            [com.cemerick/piggieback "0.2.1" :scope "test"]
                            [weasel "0.7.0" :scope "test"]
                            [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                            [cljsjs/material-components "0.25.0-0"]
                            [clojurewerkz/money "1.10.0"]
                            [org.joda/joda-money "0.12"]
                            [com.cemerick/pomegranate "1.0.0"]])

(require
  'boot.lein
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[hoplon.boot-hoplon :refer [hoplon prerender]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-test :refer [test]]
  '[com.cemerick.pomegranate :refer [pomegranate]]
  'money-sync.db)

; this will generate project.clj on every run
; that is needed to work with the project in IDEA
(boot.lein/generate)

(task-options!
 aot {:namespace   #{'money-sync.db}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/money-sync"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main        'money-sync.db
      :file        (str "money-sync-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[money-sync.core :as app])
  (apply (resolve 'app/-main) args))

(deftask dev
  "Build money-sync for local development."
  []
  (comp
    (serve
      :port    8000
      :handler 'money-sync.handler/app
      :reload  true)
    (watch)
    (hoplon)
    (reload :port 8090)
    (cljs)))

(deftask prod
  "Build money-sync for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))

(deftask make-war
  "Build a war for deployment"
  []
  (comp (hoplon)
        (cljs :optimizations :advanced)
        (uber :as-jars true)
        (web :serve 'money_sync.handler/app)
        (war)
        (target :dir #{"target"})))

(require 'migratus.core)

(def project-dir (-> (java.io.File. "migrations/") .getAbsolutePath))

(def config {:store         :database
             :migration-dir "migrations/"
             :db            {:classname   "org.postgresql.Driver"
                             :subprotocol "postgresql"
                             :subname     "//localhost:5432/"
                             :user        "postgres"
                             :password    "postgres"}})


; Ported from
; https://github.com/yogthos/migratus-lein/blob/master/src/leiningen/migratus.clj

; Boot deftask options DSL:
; https://github.com/boot-clj/boot/wiki/Task-Options-DSL

(deftask migratus-migrate
  "Bring up any migrations that are not completed"
  []
  (println (migratus.core/migrate config)))

(deftask migratus-up
  "Bring up the migrations identified by ids"
  [m migration-id MIGRATIONID #{int} " Migration ids "]
  (println (apply migratus.core/up config migration-id)))

(deftask migratus-down
  "Bring down the migrations identified by ids"
  [m migration-id MIGRATIONID [int] " Migration ids "]
  (println (apply migratus.core/down config migration-id)))

(deftask migratus-pending
  "List pending migrations"
  []
  (println (migratus.core/pending-list config)))

(deftask migratus-create
         "Create a new migration with the current date"
         [n name NAME str " Migration name "]
         (prn "hey there!")
         (prn "project dir" project-dir)

         ;; boot env
         ;(set-env! :boot-class-path (str (get-env :boot-class-path) ";" (get-env :fake-class-path)))
         ;(prn (get-env :boot-class-path))
         ;(prn (get-env :fake-class-path))
         ;(prn (get-env))

         ;; java class loader
         ;(prn "current class loader" (.getContextClassLoader (Thread/currentThread)))
         ;(prn "url array" [(.toURL (.toURI (java.io.File. project-dir)))])
         ;(prn "new class loader" (java.net.URLClassLoader/newInstance (into-array [(.toURL (.toURI (java.io.File. project-dir)))]) (.getContextClassLoader (Thread/currentThread))))
         ;(.setContextClassLoader (Thread/currentThread) (java.net.URLClassLoader/newInstance (into-array [(.toURL (.toURI (java.io.File. project-dir)))]) (.getContextClassLoader (Thread/currentThread))))

         ;; java classpath env var

         ;; clojure add classpath
         ;(add-classpath (.toURL (.toURI (java.io.File. project-dir))))

         ;; pomegranate
         (com.cemerick.pomegranate/add-classpath )

         (prn "classpath")
         (map prn clojure.java.classpath/classpath-directories)
         (prn "migration-dir" (migratus.migrations/find-or-create-migration-dir (migratus.utils/get-migration-dir config)))
         ;(prn (clojure.java.io/resource "migrations"))
         ;(prn (-> (java.io.File. ".") .getAbsolutePath))
         (println (migratus.core/create config name)))
