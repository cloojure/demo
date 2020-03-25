(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
    [tupelo.array :as ta]
    [tupelo.core :as t]
    [tupelo.schema :as tsk]
    ))

(defn check
  "Checks a multiple tic-tac-toe boards represented as lists of sets to see if a row-wise winner can be found."
  [& sets]
  (first
    (filter #(not (nil? %))
      (map (fn [ss]
             (let [r (first (filter #(or
                                       (= % #{:x})
                                       (= % #{:o}))
                              ss))]
               (if r (first r)
                     nil)))
        sets))))

(s/defn diagonal-main :- tsk/Vec
  "Returns the main diagonal of an array"
  [arr :- ta/Array]
  (assert (= (ta/num-rows arr)
            (ta/num-cols arr)))
  (let [rows-indexed (t/indexed arr)]
    (t/forv [[idx row] rows-indexed]
      (ta/elem-get arr idx idx))))

(s/defn diagonal-anti :- tsk/Vec
  "Returns the anti-diagonal of an array"
  [arr :- ta/Array]
  (diagonal-main (ta/rotate-left arr)))

(comment
  (defn triple-winners
    "Given a list of triples like [:x :o :x], return
       :x    - if X wins
       :y    - if Y wins
       nil   - else "
    ))

(defn valid-elem?
  "Returns true iff element is valid from #{ :x :o nil }"
  [elem]
  (contains? #{:x :o nil} elem))

(defn find-winner
  "Determine if a completed tic-tac-toe board contains a winner.  Returns:
     :x or :y      - if winner found
     nil           - otherwise. "
  [board]
  (nl)
  (println :-----------------------------------------------------------------------------)
  (let [arr (ta/rows->array board)] ; construct canonical array, verify rectangular
    (assert (and ; verify valid shape 3x3
              (= 3 (ta/num-rows arr))
              (= 3 (ta/num-cols arr))))
    (doseq [elem (ta/array->row-vals arr)] ; validate all elements
      (t/validate valid-elem? elem))
    (let [
          diag-main   (diagonal-main arr)
          diag-anti   (diagonal-main (ta/rotate-left arr))
          all-triples (-> (t/glue
                            (ta/array->rows arr)
                            (ta/array->cols arr))
                        (t/append diag-main)
                        (t/append diag-anti))]
      ))
  (check
    ; treats board as 3 row-wise sets
    (spy :rows (map set board))

    ; treats board as 3 col-wise sets
    (spy :cols (map set (apply map list board)))

    ; treats as a single set of the main diagonal
    (spy :main-diag
      (list (set
              (map #(nth (nth board %) %)
                (range 3)))))

    ; treats as a single set of the main anti-diagonal
    (spy :main-antidiag
      (list (set
              (map #(nth (nth board %) (- 2 %))
                (range 3)))))))

(dotest
  (let [linear-array (ta/row-vals->array 3 3 [0 1 2
                                              3 4 5
                                              6 7 8])]
    (is= [0 4 8] (diagonal-main linear-array))
    (is= [2 4 6] (diagonal-anti linear-array)))

  (is= :x (find-winner [[:x :o :x]
                        [:x :o :o]
                        [:x :x :o]]))

  (is= :o (find-winner [[:o :x :x]
                        [:x :o :x]
                        [:x :o :o]]))

  (is= nil (find-winner [[:x :o :x]
                         [:x :o :x]
                         [:o :x :o]]))
  )