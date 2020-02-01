(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require [cambium.core :as log]))

(defn foo [x]
  (if (neg? x)
    (spyx :foo-inc (inc x))
    (spyx :foo-noop x)))

(defn foo-1 [x]
  (let [result (if (neg? x)
                 (inc x)
                 x)]
    (log/debug (format "foo-1 => %s" result))
    result))

(defn foo-2 [x]
  (let [new-x (if (neg? x)
                (inc x)
                x)]
    (with-result new-x
      (log/debug (format "foo-2 => %s" new-x)))))

(dotest
  (is= 42
    (with-result 42
      (spyx (+ 2 3))))

  (is=  2 (foo-1 2))
  (is= -1 (foo-1 -2))

  (is=  2 (foo-2 2))
  (is= -1 (foo-2 -2))

  (is=  2 (foo 2))
  (is= -1 (foo -2))

  )




