(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
    [tupelo.array :as ta]
    [tupelo.core :as t]
    [tupelo.schema :as tsk]))

(def Key s/Int)

(def nrows 3)
(def ncols 3)
(def nkeys (* nrows ncols))
(def keys-vec (vec (take nkeys (thru 1 nkeys))))
(def keys-set (set keys-vec))

(s/def keys-array :- ta/Array
  (ta/row-vals->array nrows ncols keys-vec))

(s/def key->coord :- {s/Int tsk/Pair}
  (apply glue (sorted-map)
    (for [ii (range nrows)
          jj (range ncols)]
      (let [key-val (ta/elem-get keys-array ii jj)]
        {key-val [ii jj]}))))

(s/defn aligned? :- s/Bool
  "Returns true if 3 points fall on a straight line"
  [a :- Key
   b :- Key
   c :- Key]
  (assert (= 3 (count #{a b c}))) ; ensure all unique
  (let [[x1 y1] (grab a key->coord)
        [x2 y2] (grab b key->coord)
        [x3 y3] (grab c key->coord)

        dx2     (- x2 x1)
        dx3     (- x3 x1)
        dy2     (- y2 y1)
        dy3     (- y3 y1)

        prod-32 (* dx3 dy2)
        prod-23 (* dx2 dy3)
        result (t/rel= prod-32 prod-23 :digits 8) ]
    result) )

(dotest
  (is= keys-array [[1 2 3] ; what a 3x3 keypad looks like
                   [4 5 6]
                   [7 8 9]])

  (is= key->coord ; convert key value to [i,j] coords
    {1 [0 0]
     2 [0 1]
     3 [0 2]
     4 [1 0]
     5 [1 1]
     6 [1 2]
     7 [2 0]
     8 [2 1]
     9 [2 2]})

  (is (aligned? 1 2 3)) ; verify alishment tests on a 3x3 keypad
  (is (aligned? 7 5 3))
  (is (aligned? 7 1 4))
  (is (aligned? 9 1 5))
  (isnt (aligned? 8 1 5))
  (isnt (aligned? 1 6 7))
  (throws? (aligned? 0 6 7)) ; throws for invalid keys
  (throws? (aligned? 0 6 10))



  )
;(is (valid-path [1 6 7 4]))   ;; knights jump is valid
;(is (valid-path [2 1 3]))     ;; 2 is already used, so we can cross it
;(isnt (valid-path [1 3 2]))     ;; can't get from 1 to 3 without using 2 first
;(isnt (valid-path [1 9]))       ;; can't cross 5 without using
;(isnt (valid-path [1 2 3 2 1])) ;; can't use dots more than once
;(isnt (valid-path [0 1 2 3]))   ;; there's no dot 0