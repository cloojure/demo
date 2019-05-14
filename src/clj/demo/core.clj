(ns demo.core
  (:use tupelo.core tupelo.forest)
  (:require
    [clojure.string :as str] ) )

(defn foo [] "Foo!")

(defn -main [& args]
  (nl)
  (println "result =>" (foo))
  (nl))