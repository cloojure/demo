(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [clojure.string :as str]
    [tupelo.core :as t]))

(dotest
  (is= "Foo" (foo))
  )

