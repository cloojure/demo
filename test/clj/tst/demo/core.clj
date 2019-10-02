(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [clojure.string :as str]
    [schema.core :as s]
    [tupelo.schema :as tsk]
    )
  (:import [demo Calc])
  )

(dotest
  (spyx (Calc/add2 2 3)))


