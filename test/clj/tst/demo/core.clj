(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [clojure.string :as str]
    [schema.core :as s]
    [tupelo.array :as ta]
    [tupelo.core :as t]
    [tupelo.schema :as tsk]
    [tupelo.string :as ts]))


(s/defn row->chars :- [Character]
  "Convert a string to a vector of chars, excluding blanks"
  [s :- s/Str]
  (t/it-> s
    (ts/collapse-whitespace it)
    (remove ts/whitespace? it)
    (vec it)))

; #todo maybe enforce a case-insensitive search here?
(s/defn count-matches-in-vec :- s/Int
  "Counts the matches of a target string in a single vector of chars"
  [char-vec :- [Character]
   target :- s/Str] ; warning: assumes a simple string without any regex chars
  (let [search-str (str/join char-vec)
        matches    (re-seq (re-pattern target) search-str)
        result     (count matches)]
    result))

(s/defn count-row-matches :- s/Int
  "Counts the matches of a target string in an array of chars, searching left->right in each row"
  [char-arr :- tsk/Array
   target :- s/Str]
  (let [found-cnts (mapv #(count-matches-in-vec % target) char-arr)
        result     (apply + found-cnts)]
    result))

; #todo #awt:  does not check for all lowercase data & target
(s/defn count-words-in-matrix :- s/Int
  "Searches for a target string in a 2D character array, returning the number of matches
  found.  Searches up/down & left/right."
  [char-array :- tsk/Array
   target :- s/Str]
  (let [chars-lr    char-array ; for symmetry of names
        chars-rl    (ta/flip-lr char-array)
        chars-ud    (ta/rotate-left char-array)
        chars-du    (ta/rotate-right char-array)
        total-found (+
                      (count-row-matches chars-lr target)
                      (count-row-matches chars-rl target)
                      (count-row-matches chars-ud target)
                      (count-row-matches chars-du target))]
    total-found))

(dotest
  (let [word-data  "A O T D L R O W
                    L C B M U M L U
                    D R U J D B L J
                    P A Z H Z Z E F
                    B C Z E L F H W
                    R K U L V P P G
                    A L B L P O P Q
                    B E M O P P J Y "
        word-array (t/it-> word-data
                     (str/trim it)
                     (str/lower-case it)
                     (str/split-lines it)
                     (mapv row->chars it))]
    (is= word-array
      [[\a \o \t \d \l \r \o \w]
       [\l \c \b \m \u \m \l \u]
       [\d \r \u \j \d \b \l \j]
       [\p \a \z \h \z \z \e \f]
       [\b \c \z \e \l \f \h \w]
       [\r \k \u \l \v \p \p \g]
       [\a \l \b \l \p \o \p \q]
       [\b \e \m \o \p \p \j \y]])

    (is= 2 (count-words-in-matrix word-array "hello"))
    (is= 1 (count-words-in-matrix word-array "world"))
    (is= 2 (count-words-in-matrix word-array "buzz"))
    (is= 0 (count-words-in-matrix word-array "clojure"))
    (is= 0 (count-words-in-matrix word-array "cowabunga"))))

