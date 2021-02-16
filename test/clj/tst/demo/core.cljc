(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [schema.core :as s]))



(comment

  (defn play-data [& names]
    (for [n names
          i (range 20)]
      {:time i :item n :quantity (+ (Math/pow (* i (count n)) 0.8) (rand-int (count n)))}))

  (defn line-plot []
    {:data     {:values (play-data "monkey" "slipper" "broom")}
     :encoding {:x     {:field "time" :type "quantitative"}
                :y     {:field "quantity" :type "quantitative"}
                :color {:field "item" :type "nominal"}}
     :mark     "line"})

  (dotest
    ; Render the plot
    (oz/view! (line-plot))
    )



  (comment
    (do
      (require '[clojure.java.browse :as cjb])
      (cjb/browse-url "http://wikipedia.com"))
    ))
