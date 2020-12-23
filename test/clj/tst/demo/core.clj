(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [tupelo.string :as str]
    [schema.core :as s]
    ))

(dotest
  (spyx (demo.Calc/theAnswer))
  )