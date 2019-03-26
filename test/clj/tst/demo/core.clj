(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [jsonista.core :as json]
    [muuntaja.core :as muun]
    [tupelo.pedestal :as tp]
    [tupelo.string :as tstr]
    [tupelo.parse.yaml :as yaml]
    ))



(defn jsonista-round-trip [arg]
  (it-> arg
    (json/write-value-as-string it)
    (json/read-value it)))

(dotest
  (spyx (jsonista-round-trip {:kikka 42}))
  (spyx (jsonista-round-trip {"one" 1
                              ""    {:kikka 42}})))

(comment
  ; result
  (jsonista-round-trip {:kikka 42}) => {"kikka" 42}
  (jsonista-round-trip {"one" 1, "" {:kikka 42}}) => {"" {"kikka" 42}, "one" 1}
)

(defn muun-round-trip [arg]
  (it-> arg
    (muun/encode tp/application-json it)
    (muun/decode tp/application-json it)))

(dotest
  (spyx (muun-round-trip {:kikka 42}))
  (spyx (muun-round-trip {"one" 1
                          ""    {:kikka 42}})))

