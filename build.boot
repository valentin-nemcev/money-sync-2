(def project 'money-sync)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [migratus "1.0.0"]
                            [org.slf4j/slf4j-log4j12 "1.7.9"]
                            [onetom/boot-lein-generate "0.1.3" :scope "test"]
                            [org.postgresql/postgresql "RELEASE"]])

; this will generate project.clj on every run that is needed to work with the project in IDEA
(require 'boot.lein)
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

(require '[adzerk.boot-test :refer [test]])
