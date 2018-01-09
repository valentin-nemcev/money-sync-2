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
                            [cljsjs/material-components "0.25.0-0"]]
)

(require
  'boot.lein
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[hoplon.boot-hoplon :refer [hoplon prerender]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-test :refer [test]]
  'money-sync.core)

; this will generate project.clj on every run
; that is needed to work with the project in IDEA
(boot.lein/generate)

(task-options!
 aot {:namespace   #{'money-sync.core}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/money-sync"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main        'money-sync.core
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
  []
  (println (migratus.core/migrate config)))

(deftask migratus-up
  [m migration-id MIGRATIONID #{int} "Migration ids"]
  (println (apply migratus.core/up config migration-id)))

(deftask migratus-down
  [m migration-id MIGRATIONID [int] "Migration ids"]
  (println (apply migratus.core/down config migration-id)))

(deftask migratus-pending
  []
  (println (migratus.core/pending-list config)))

(deftask migratus-create
   [n name NAME str "Migration name"]
   (println (migratus.core/create config name)))
