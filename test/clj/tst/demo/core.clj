(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [cambium.codec :as codec]
    [cambium.core :as log]
    [cambium.logback.json.flat-layout :as flat]
  ))

(dotest
  (flat/set-decoder! codec/destringify-val) ; configure backend with the codec
  (log/info "Application started")
  (log/info {:stuff [1 2 3] :argc 99} "Arguments received")

  )




