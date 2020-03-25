; #TODO  In a real app, would split up ns like:
; #TODO         tic-tac-toe.core   - core fns
; #TODO     tst.tic-tac-toe.core   - testing fns
(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
    [tupelo.array :as ta]
    [tupelo.core :as t]
    [tupelo.schema :as tsk]))

(s/defn valid-elem? :- s/Bool
  "Returns true iff element is valid from #{ :x :o nil }"
  [elem]
  (contains? #{:x :o nil} elem))

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

(s/defn triple->winner :- s/Keyword
  "Given a list of triples like [:x :o :x], return a list of
     :x      - if X wins
     :y      - if Y wins
     ::none  - otherwise "
  [row :- tsk/Triple]
  (cond
    (= [:x :x :x] row) :x
    (= [:o :o :o] row) :o
    :else ::none))

(defn board->result
  "Determine if a completed tic-tac-toe board contains a winner.  Returns:
     :x or :y    - if winner found
     :cats-game  - otherwise. "
  [board]
  (let [arr (ta/rows->array board)] ; construct canonical array, verify rectangular
    (assert (and ; verify valid shape 3x3
              (= 3 (ta/num-rows arr))
              (= 3 (ta/num-cols arr))))
    (doseq [elem (ta/array->row-vals arr)] ; validate all elements
      (t/validate valid-elem? elem))
    (let [diag-main      (diagonal-main arr)
          diag-anti      (diagonal-main (ta/rotate-left arr))
          all-triples    (-> (t/glue
                               (ta/array->rows arr)
                               (ta/array->cols arr))
                           (t/append diag-main)
                           (t/append diag-anti))
          winners        (mapv triple->winner all-triples)
          winners-unique (set
                           (remove #(= % ::none) winners))]
      (cond
        (zero? (count winners-unique)) :cats-game ; no winner

        (= 1 (count winners-unique)) (first winners-unique) ; a single winner

        :else ;  multiple winners found
        (throw (ex-info "Multiple winners found! " (t/vals->map arr winners)))))))

(dotest
  ; demonstrate diag fns
  (let [linear-array (ta/row-vals->array 3 3 [0 1 2
                                              3 4 5
                                              6 7 8])]
    (is= [0 4 8] (diagonal-main linear-array))
    (is= [2 4 6] (diagonal-anti linear-array)))

  (is= :x (board->result [[:x :o :x] ; col win
                          [:x :o :o]
                          [:x :x :o]]))
  (is= :x (board->result [[:x :x :x] ; row win
                          [:o :x :o]
                          [:x :o :o]]))
  (is= :o (board->result [[:o :x :x] ; diag win
                          [:x :o :x]
                          [:x :o :o]]))
  (is= :o (board->result [[:x :x :o] ; anti-diag win
                          [:x :o :x]
                          [:o :x :o]]))

  (is= :cats-game (board->result [[:x :o :x]
                                  [:x :o :x]
                                  [:o :x :o]]))

  ; #awt #todo:  maybe need to identify and exclude incomplete games ???
  (is= :cats-game (board->result [[:o :x :x]
                                  [:x nil :x]
                                  [:x :o :o]]))

  (throws? (board->result [[:x :o :x] ; invalid result with multiple winners
                           [:x :o :x]
                           [:x :o :o]]))

  )