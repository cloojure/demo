#!/bin/bash
#_( ;# NOTE: if use 2-char EOL "comment" symbol, it works for both Bash & Clojure
    DEPS='{:deps {}}'  ;# DEPS => same format as deps.edn. Multiline is okay.
    DEPS='{:deps {
                  funcool/cuerdas      {:mvn/version "2.2.0"}
                  tupelo               {:mvn/version "0.9.157"}
                 }}'

      OPTS=' -J-Xms500m -J-Xmx2g -J-client '  ;# Any options other than deps

      exec clojure $OPTS -Sdeps "$DEPS" "$0" "$@"
    )
; `exec` aboveinvoke the 'clojure' program with desired $OPTS & $DEPS
; $0 is the name of this file, which `clojure` will read & run
; $@ is all args $1 $2 $3... (not including $0), so cmd-line args to your program
;---------------------------------------------------------------------------------------------------

(ns utils.go ; doesn't matter what ns we use, `:use` & `:require` function normally
  (:use tupelo.core)
  (:require
    [cuerdas.core :as cc]
    [tupelo.core :as t]
    [tupelo.string :as ts]
    ))

(newline)
(t/print-versions) 
(println (clojure-version))

(spyx (cc/camel "foo bar"))
(spyx (ts/str->kw-normalized "foo bar"))

(defn -main [& args] (println "args:  "  (vec args))) 

(newline)
(println "exiting...")

