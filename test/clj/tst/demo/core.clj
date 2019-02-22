(ns tst.demo.core
  (:use tupelo.core demo.core tupelo.test))

(dotest
  (is= 5 (+ 2 3))
  )

