(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [tupelo.core :as t]
    [schema.core :as s]
    [clojure.string :as str]
    [tupelo.string :as ts]))

(defn normalized-ndc
  [ndc-in]
  (let [segs      (str/split ndc-in #"-")
        >>        (assert (= 3 (count segs)))
        zero-char \0
        seg-1     (ts/pad-left (xfirst segs) 5 zero-char)
        seg-2     (ts/pad-left (xsecond segs) 4 zero-char)
        seg-3     (ts/pad-left (xthird segs) 2 zero-char)
        result    (str seg-1 seg-2 seg-3)]
    (assert (= 5 (count seg-1)))
    (assert (= 4 (count seg-2)))
    (assert (= 2 (count seg-3)))
    (assert (= 11 (count result)))
    result))

(dotest
  ; sample vals
  (is= "00777310502" (normalized-ndc "0777-3105-02"))
  (is= "00777310502" (normalized-ndc "0777-3105-2"))
  (is= "00777310502" (normalized-ndc "777-3105-2"))

  (is= "00123004506" (normalized-ndc "123-45-6"))
  (is= "00001000203" (normalized-ndc "1-2-3"))

  ; must be 3 segments
  (throws? (normalized-ndc "1-2-"))
  (throws? (normalized-ndc "1-2"))

  ; result must be 11 chars
  (throws? (normalized-ndc "12345-1234-123"))
  (throws? (normalized-ndc "12345-12345-12"))
  (throws? (normalized-ndc "123456-1234-12"))

  ; segments must be right size
  (throws-not? (normalized-ndc "12345-9-9"))
  (throws-not? (normalized-ndc "9-1234-9"))
  (throws-not? (normalized-ndc "9-9-12"))
  (throws? (normalized-ndc "123456-9-9"))
  (throws? (normalized-ndc "9-12345-9"))
  (throws? (normalized-ndc "9-9-123"))

  )