(defproject demo-grp/demo-art "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [clojure.java-time "0.3.2"]
                 [metasoarous/oz "1.6.0-alpha34"]
                 [org.flatland/ordered "1.5.9"]
                 [org.clojure/clojure "1.10.2"]
                 [prismatic/schema "1.1.12"]
                 [tupelo "21.01.26"]
                 ]
  :plugins [[com.jakemccrary/lein-test-refresh "0.24.1"]
            [lein-ancient "0.7.0"]
            [lein-codox "0.10.7"]
            ]

  :profiles {:dev     {:dependencies []}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* false}

  :main demo.core
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :test-paths ["test/clj"]
  :target-path "target/%s"
  :compile-path "%s/class-files"
  :clean-targets [:target-path]

  :jvm-opts ["-Xms500m" "-Xmx4g"]
  )
