(defproject demo "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License" :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[cljc.java-time "0.1.6"]
   [org.clojure/clojure "1.10.1"]
   [overtone/at-at "1.2.0"]
   [prismatic/schema "1.1.12"]
   [com.taoensso/encore "2.116.0"]
   [com.taoensso/tufte "2.1.0"]
   [tupelo "0.9.154"]
   ]

  :plugins [[com.jakemccrary/lein-test-refresh "0.24.1"]
            [lein-ancient "0.6.15"]
            [lein-codox "0.10.7"]]

  :db "jdbc:postgresql://localhost/default"
  :settings "settings-default.edn"

  :profiles {:dev     {:dependencies []}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* false}
  :main ^:skip-aot demo.core

  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :test-paths ["test/clj"]
  :target-path "target/%s"
  :compile-path "%s/class-files"
  :clean-targets [:target-path]

  :jvm-opts ["-Xms500m" "-Xmx8g" "-server"]
  )
