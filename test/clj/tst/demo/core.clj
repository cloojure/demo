(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [clojure.string :as str]))

(defn gardentwo
  ([plants]
   (gardentwo plants ["Alice" "Bob" "Charlie" "David" "Eve" "Fred"
                      "Ginny" "Harriet" "Ileana" "Joseph" "Kincaid" "Larry"]))
  ([plants children]
   (let-spy-pretty
     [child-keys (sort (map (comp keyword str/lower-case) children))
      all-plants {\G :grass \C :clover \R :radishes \V :violets}
      lines      (str/split-lines plants)
      twos       (map (partial partition 2) lines)
      cups       (apply map concat twos)
      cups       (map (partial map all-plants) cups)
      pairs      (map vector child-keys cups)]
     (reduce (partial apply assoc) {} pairs))))

(def plants ; #todo this is wrong - what is the right input?????
"grass
clover
radishes
violets")

(dotest
  (gardentwo plants)
  )

