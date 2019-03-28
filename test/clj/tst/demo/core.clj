(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [clojure.pprint :as pprint]
    [clojure.walk :as walk]
    [io.pedestal.log :as log]
    [tupelo.string :as tstr] ))

(defn log-warn [& args]
  (apply println args))

(defn pprint-str
  ([data]
   (with-out-str
     (pprint/pprint data)))
  ([key data]
   (with-out-str
     (println key)
     (pprint/pprint data))))

(defn warn-on-nilempty-key
  [some-map]
  (let [mapentry-key-nilempty-warning (fn [arg]
                                    (when (map-entry? arg)
                                      (when (let [k (key arg)]
                                              (or (nil? k) (= "" k) (= [] k)))
                                        (log-warn (pprint-str :awt-nil-key-found some-map))))
                                    arg)]
    (walk/postwalk mapentry-key-nilempty-warning some-map)))

(dotest
  (let [m      {:a 1 :b 2}
        result (with-out-str (warn-on-nilempty-key m))]
    (is= "" (spyx result)))
  (let [m      {:a 1 "" 2}
        result (with-out-str (warn-on-nilempty-key m))]
    (println result)
    (is (tstr/contains-str? result (str :awt-nil-key-found))))
  (let [m      {:a 1 nil 2}
        result (with-out-str (warn-on-nilempty-key m))]
    (println result)
    (is (tstr/contains-str? result (str :awt-nil-key-found))))
  (let [m      {:a 1 :b {:c {nil 2}}}
        result (with-out-str (warn-on-nilempty-key m))]
    (println result)
    (is (tstr/contains-str? result (str :awt-nil-key-found))))

  )
