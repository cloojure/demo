(ns tst.demo.core
  (:use tupelo.core tupelo.test))

(let [check (fn [& sets]
              (first
                (filter #(not (nil? %))
                  (map (fn [ss]
                         (let [r (first (filter #(or
                                                   (= % #{:x})
                                                   (= % #{:o}))
                                          ss))]
                           (if r (first r)
                                 nil)))
                    sets))))]

  (defn ttt
    [board]
    (check
      (map set board)
      (map set (apply map list board))
      (list (set
              (map #(nth (nth board %) %)
                (range 3))))
      (list (set
              (map #(nth (nth board %) (- 2 %))
                (range 3)))))))

(assert (= :x (ttt [[:x :o :x]
                    [:x :o :o]
                    [:x :x :o]])))
(assert (= :o (ttt [[:o :x :x]
                    [:x :o :x]
                    [:x :o :o]])))
(assert (nil? (ttt [[:x :o :x]
                    [:x :o :x]
                    [:o :x :o]])))