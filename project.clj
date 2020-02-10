(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [cambium/cambium.core         "0.9.3"]
                 [cambium/cambium.codec-simple "0.9.3"]
                 [cambium/cambium.logback.core "0.4.3"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "0.7.559"]
                 [org.clojure/tools.reader "1.3.2"]
                 [prismatic/schema "1.1.12"]
                 [tupelo "0.9.173"]
                 [org.openjfx/javafx "13.0.1" :extension "pom"]
                 ]
  :plugins [
    [com.jakemccrary/lein-test-refresh "0.24.1"]
    [lein-ancient "0.6.15"]
    ]

  :db "jdbc:postgresql://localhost/default"
  :settings "settings-default.edn"

  :profiles {:dev     {:dependencies [ ]}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* false}
  :main ^:skip-aot demo.core/foo

  :source-paths            ["src/clj"]
  :java-source-paths       ["src/java"]
  :test-paths              ["test/clj"]
  :target-path             "target/%s"
  :compile-path            "%s/class-files"
  :clean-targets           [:target-path]

  :jvm-opts ["-Xms500m" "-Xmx8g" "-server" ]
  )
