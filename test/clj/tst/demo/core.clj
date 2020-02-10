(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [clojure.string :as str]
    [clojure.pprint :as pprint]))


(defmacro let-spyxx
  "An expression (println ...) for use in threading forms (& elsewhere). Evaluates the supplied
   expressions, printing both the expression and its value to stdout. Returns the value of the
   last expression."
  [& exprs]
  (let [decls      (xfirst exprs)
        _          (when (not (even? (count decls)))
                     (throw (ex-info "spy-let-proc: uneven number of decls:" {:decls decls})))
        forms      (xrest exprs)
        fmt-pair   (fn [[dest src]]
                     [dest src
                      '_ (list `spyxx dest)]) ; #todo gensym instead of underscore?
        pairs      (vec (partition 2 decls))
        r1         (vec (mapcat fmt-pair pairs))
        final-code `(let ~r1 ~@forms)]
    final-code))


(defn unless-impl
  [args]
  (let [[pred & forms] args]
    `(if (and true ~pred)
       nil
       (do ~@forms))))

(defmacro unless
  [& args]
  (unless-impl args))

(def curr-ns *ns*)

(defn macro-print-impl
  [macro-expr]
  (let [macro-sym       (first macro-expr)
        macro-impl-sym  (str->sym (str curr-ns "/"
                                    (sym->str macro-sym) "-impl"))
        macro-args      (rest macro-expr)
        macro-impl-call (list macro-impl-sym `(quote ~(vec macro-args)))
        macro-result    (eval macro-impl-call)]
    ;(spyx macro-impl-sym)
    ;(spyx macro-impl-call)
    ;(spyx macro-result)
    macro-result))

(defmacro macro-print
  [macro-expr]
  `(pprint/pprint
    (quote ~(macro-print-impl macro-expr))))

(dotest
  (nl) (println :dimple-impl-output-begin)
  (pretty (macro-print-impl '(unless false (println "forms!") :yes)))
  (println :dimple-impl-output-done)
  (nl)
  (println :dimple-call-begin)
  (macro-print
    (unless false (println "forms!") :yeeessssss!))
  (println :dimple-call-done)

  (nl)
  (println "calling...")
  (is (unless false
        (println "forms!") :yeeessssss!))
  (println "  ...done")
  )

;(comment
;  (defn show-impl
;    [macro-expr]
;    `(do
;       (let ; -spy
;         [macro-sym#      (first ~macro-expr)
;          macro-impl-sym# (str->sym (str curr-ns "/"
;                                      (sym->str macro-sym#) "-impl"))
;          macro-args#     (rest macro-expr#)
;          macro-impl-call `(~macro-impl-sym# '~macro-args#)
;          macro-result    (eval macro-impl-call)]
;         (spyx macro-impl-sym#)
;         (spyx macro-impl-call)
;         (spyx-pretty macro-result)
;         )))
;
;  (defmacro show
;    [macro-expr]
;    (show-impl macro-expr))
;
;  (dotest
;    (spyx (unless false (println "forms!") :yes))
;    (spyx (unless true (println "forms!") :yes))
;
;    (do   ; defn show-impl
;      (let [macro-expr# '(unless false (println "forms!") :yes)]
;        ; [macro-expr]
;        (nl)
;        (spyx *ns*)
;        (spyx `unless-impl)
;        (spyx (ns-resolve *ns* `unless-impl))
;
;        (nl)
;        (do
;          (let-spy
;            [macro-sym#      (first macro-expr#)
;             macro-impl-sym# (str->sym (str curr-ns "/"
;                                         (sym->str macro-sym#) "-impl"))
;             macro-args#     (rest macro-expr#)
;             macro-impl-call `(~macro-impl-sym# '~macro-args#)
;             macro-result    (eval macro-impl-call)]
;            (spyx macro-impl-sym#)
;            (spyx macro-impl-call)
;            (spyx-pretty macro-result)
;            ))))
;
;    ;(nl) (println "show-impl:")
;    ;(pretty
;    ;  (show-impl '(unless false (println "forms!") :yes)))
;
;    (nl) (println "calling show:")
;    (println (macroexpand-1
;               '(show (unless false (println "forms!") :yes))))
;
;    ;(nl) (println "calling macroexpand:")
;    ;(println
;    ;  (macroexpand-1 '(show (unless false (println "forms!") :yes))))
;    ))



