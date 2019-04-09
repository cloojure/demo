(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require [schema.core :as s]
            [clojure.string :as str]))

(s/defn divisible-by-3 :- s/Str
  [binary-str-all]
  (let [binstr-div-by-3? (s/fn [binstr :- s/Str]
                           (it-> binstr
                             (Integer/parseInt it 2)
                             (mod it 3)
                             (zero? it)))]
    (it-> binary-str-all
      (str/split it #",")
      (keep-if binstr-div-by-3? it)
      (str/join "," it))))

(dotest
  (is= (divisible-by-3 "1100,101,111,1111")
    "1100,1111"))


