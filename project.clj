(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
     [cambium/cambium.core "0.9.3"]
    ;[cambium/cambium.codec-simple "0.9.3" ]
     [cambium/cambium.codec-cheshire "0.9.3" ]
    ;[cambium/cambium.logback.core "0.4.3"]
     [cambium/cambium.logback.json "0.4.3"]
     [cljc.java-time "0.1.4"]
     [metosin/jsonista "0.2.3"]
     [metosin/muuntaja "0.6.4"]
    ;[ch.qos.logback/logback-classic "1.2.3"]
     [org.clojure/clojure "1.10.1"]
     ;[org.slf4j/slf4j-api "1.7.26"]
     ;[org.slf4j/slf4j-simple "1.7.26"]
     [prismatic/schema "1.1.11"]
     [tupelo "0.9.144"] ]
  :plugins [
    [com.jakemccrary/lein-test-refresh "0.23.0"]
    [lein-ancient "0.6.15"]
    [lein-codox "0.10.3"]]

  :db "jdbc:postgresql://localhost/default"
  :settings "settings-default.edn"

  :profiles {:dev     {:dependencies [ ]}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* false}
  :main ^:skip-aot demo.core

  :source-paths            ["src/clj"]
  :java-source-paths       ["src/java"]
  :test-paths              ["test/clj"]
  :target-path             "target/%s"
  :compile-path            "%s/class-files"
  :clean-targets           [:target-path]

  :jvm-opts ["-Xms500m" "-Xmx8g" "-server" ]
  )
