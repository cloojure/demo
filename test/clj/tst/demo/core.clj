;(ns tst.demo.core
;  (:use demo.core tupelo.core tupelo.test)
;  (:require
;    [schema.core :as s] ))

(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:import [demo Msg Homer Bart]))

(dotest
  (let [homer (Homer.)
        bart  (Bart.)]
    (spyx (.msg homer))
    (spyx (.msg bart))))

