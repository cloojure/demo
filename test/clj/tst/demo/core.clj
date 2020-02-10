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

; desired output format of...
(comment
  (defmacro+showable
    [pred & forms]
    `(if (and true ~pred)
       nil
       (do ~@forms))))
;...is the following:
;---------------------------------------------------------------------------------------------------
(do       ; *** live code ***
  (defn unless-impl
    [args]
    (let [[pred & forms] args]
      `(if (and true ~pred)
         nil
         (do ~@forms))))

  (defmacro unless
    [& args]
    (unless-impl args)))
;---------------------------------------------------------------------------------------------------


(def curr-ns *ns*)

(defn macro-show-impl
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

(defmacro macro-show
  [macro-expr]
  `(pprint/pprint
    (quote ~(macro-show-impl macro-expr))))

(dotest
  (nl) (println :macro-show-impl-output-begin)
  (pretty (macro-show-impl '(unless false (println "forms!") :yes)))
  (println :macro-show-impl-output-done)
  (nl)
  (println :macro-show-begin)
  (macro-show
    (unless false (println "forms!") :yeeessssss!))
  (println :macro-show-done)

  (nl)
  (println "calling...")
  (is (unless false
        (println "forms!") :yeeessssss!))
  (println "  ...done")
  )




