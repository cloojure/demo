(ns tst.demo.core
  (:use tupelo.core tupelo.test))

(defn keep-pairs
  [data]
  (loop [result []
         prev nil
         remaining data]
    (if (empty? remaining)
      result
      (let [curr (first remaining)
            keep-curr (or (= 1 curr)
                          (= 1 prev))
            result-next (if keep-curr
                          (conj result curr)
                          result)
            prev-next curr
            remaining-next (rest remaining)]
        (recur result-next prev-next remaining-next)))))

(dotest
  (let [data [1 2 3 1 4 1 1 6 8 9 0 1]]
    (is= [1 2 1 4 1 1 6 1]
         (keep-pairs data))))


