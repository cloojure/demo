(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require [tupelo.core :as t]))


(comment

  (println :last
    (+ (snoop 4) (snoop 5)))

  (defn max-coins [coin-value desired]
    (assert (< 0 coin-value))
    (assert (<= 0 desired))
    (quot desired coin-value))

  (def CoinPurse {s/Int ; coin value
                  s/Int}) ; number of those coins

  (s/defn total-for :- s/Int
    [coin-purse :- CoinPurse]
    (let [coin-vals    (keys coin-purse)
          coin-numbers (vals coin-purse)
          total        (reduce + (map * coin-vals coin-numbers))]
      total))

  (s/defn change-for :- [CoinPurse]
    [amount :- s/Int
     coin-vals :- [s/Int]]
    (if (empty? coin-vals)
      {}
      (let [coin-val        (first coin-vals)
            coins-remaining (rest coin-vals)
            upper-limit     (max-coins coin-val amount)]
        (for [num-coins (thru upper-limit)]
          (let [coin-total       (* num-coins coin-val)
                amount-remaining (- amount coin-total)]
            (assert (<= 0 amount-remaining))
            (if (zero?))
            )
          )
        ))

    )


  (def coin-vals [200 100 50 20 10 5 2 1])

  (dotest
    (is= 1 (max-coins 200 200))
    (is= 10 (max-coins 20 200))
    (is= 40 (max-coins 5 200))
    (is= 0 (max-coins 200 100))
    (is= 0 (max-coins 200 1))

    (is= 18 (total-for {5 3 1 3}))
    (is= 21 (total-for {5 3 2 1 1 4}))
    )

  )
