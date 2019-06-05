(ns demo.core
  (:use tupelo.core
        tupelo.forest)
  (:require
    [clojure.string :as str])
  (:gen-class)
  (:import [java.io ByteArrayOutputStream PrintStream]))


(defn foo [] "Foo!")

(defn -main [& args]
  (nl)
  (println "result =>" (foo))
  (nl))