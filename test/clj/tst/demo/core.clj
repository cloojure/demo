(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [tupelo.parse.tagsoup :as tagsoup]
    ))

(dotest
  (spyx (jar-file?))

  )

