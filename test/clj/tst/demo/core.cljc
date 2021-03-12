(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
     ))


;---------------------------------------------------------------------------------------------------
#_(do
    (require '[clojure.java.browse :as cjb])
    (dotest
      (spyx (cjb/browse-url "http://yahoo.com"))))

