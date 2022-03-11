(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [tupelo.string :as str]))

(dotest
  (spyx (str/split "a.b.c.d" #"\." ))
  )