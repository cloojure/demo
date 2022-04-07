(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [tupelo.string :as str]
    ))

(dotest
  (is true )
  (is= 5 (+ 2 3))
  )
