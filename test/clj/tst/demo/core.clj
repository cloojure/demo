(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
    [tupelo.array :as ta]
    [tupelo.core :as t]
    [tupelo.schema :as tsk]
    [clojure.set :as set]))

(def Key s/Int)

(def nrows 3)
(def ncols 3)
(def nkeys (* nrows ncols))
(def keys-vec (vec (take nkeys (thru 1 nkeys))))
(def keys-set (set keys-vec))

(s/defn validate-key
  "Throws if key isnt valid"
  [k]
  (truthy? (contains? keys-set k)))

(s/def keys-array :- ta/Array
  (ta/row-vals->array nrows ncols keys-vec))

(s/def key->coord :- {s/Int tsk/Pair}
  (apply glue (sorted-map)
    (for [ii (range nrows)
          jj (range ncols)]
      (let [key-val (ta/elem-get keys-array ii jj)]
        {key-val [ii jj]}))))

(s/defn between? :- s/Bool
  "Returns true if 3 points fall on a straight line
  and b is between a & c "
  [a :- Key
   b :- Key
   c :- Key]
  (assert (= 3 (count #{a b c}))) ; ensure all unique
  (let [[x1 y1] (grab a key->coord)
        [x2 y2] (grab b key->coord)
        [x3 y3] (grab c key->coord)

        dx3     (- x3 x1)
        dy3     (- y3 y1)
        dx3-2   (* dx3 dx3)
        dy3-2   (* dy3 dy3)
        dist3   (Math/sqrt (+ dx3-2 dy3-2))

        dx2     (- x2 x1)
        dy2     (- y2 y1)
        dx2-2   (* dx2 dx2)
        dy2-2   (* dy2 dy2)
        dist2   (Math/sqrt (+ dx2-2 dy2-2))

        ratio23 (/ dist2 dist3)

        result  (and
                  (<= (Math/abs ratio23) 1.0)
                  (t/rel= dx2 (* dx3 ratio23) :digits 5)
                  (t/rel= dy2 (* dy3 ratio23) :digits 5))]
    result))

(s/defn betweener-keys :- #{Key}
  "Given two keys, returns a set of all keys on a line between them"
  [a :- Key
   b :- Key]
  (let [candidate-keys (set/difference keys-set #{a b}) ; don't consider duplicates
        tweener?       (fn [cand-key] (between? a cand-key b))]
    (set (filter tweener? candidate-keys))))

(s/defn duplicate-keys? :- s/Bool
  "Returns true if a key path has duplicates"
  [keys :- [Key]]
  (let [num-unique (count (set keys))]
    (not= (count keys) num-unique)))

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

  (is (validate-key 1))
  (is (validate-key 9))
  (isnt (validate-key 0))
  (isnt (validate-key 10))

  ; OBE
  ;(is (aligned? 1 2 3)) ; verify alishment tests on a 3x3 keypad
  ;(is (aligned? 1 2 3)) ; verify alishment tests on a 3x3 keypad
  ;(is (aligned? 7 5 3))
  ;(is (aligned? 7 1 4))
  ;(is (aligned? 9 1 5))
  ;(isnt (aligned? 8 1 5))
  ;(isnt (aligned? 1 6 7))
  ;(throws? (aligned? 0 6 7)) ; throws for invalid keys
  ;(throws? (aligned? 0 6 10))

  (is (between? 1 2 3)) ; verify alishment tests on a 3x3 keypad
  (isnt (between? 2 1 3))
  (is (between? 7 5 3))
  (isnt (between? 5 7 3))
  (is (between? 7 4 1))
  (isnt (between? 7 1 4))
  (isnt (between? 9 1 5))
  (is (between? 1 5 9))
  (isnt (between? 8 1 5))
  (isnt (between? 1 6 7))
  (throws? (between? 0 6 7)) ; throws for invalid keys
  (throws? (between? 0 6 10))

  (is= #{2} (betweener-keys 1 3))
  (is= #{} (betweener-keys 1 2))
  (is= #{5} (betweener-keys 1 9))
  (is= #{5} (betweener-keys 7 3))
  (is= #{6} (betweener-keys 9 3))

  (isnt (duplicate-keys? [1 2 3]))
  (is (duplicate-keys? [1 2 3 2 1]))
  )


(s/defn valid-path? :- s/Bool
  "Returns true iff input key sequence is a valid pattern lock"
  [keys :- [Key]]
  (let [pattern-too-short (< (count keys) 4) ; #todo enforce a minimum pattern len?
        invalid-keys?     (t/has-some? falsey? (mapv validate-key keys))]
    (if (or invalid-keys?
          pattern-too-short
          (duplicate-keys? keys))
      false

      (do ; walk the path and ensure path doesn't skip unused keys
        (loop [prev (first keys)
               seen #{}

               ])
        true)
      ))
  )


(dotest

  ;(is (valid-path? [1 6 7 4]))   ; knights jump is valid
  ;(is (valid-path? [2 1 3]))     ; 2 is already used, so we can cross it
  ;(isnt (valid-path? [1 3 2]))     ; can't get from 1 to 3 without using 2 first
  ;(isnt (valid-path? [1 9]))       ; can't cross 5 without using
  (isnt (valid-path? [1 2 3 2 1])) ; can't use dots more than once
  (isnt (valid-path? [0 1 2 3])) ; there's no dot 0
  (isnt (valid-path? [1 2 3])) ; minimum pattern len is 4

  )
