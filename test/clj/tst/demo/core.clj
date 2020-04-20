(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test) )

(dotest
  (is (unquote-form? (quote (unquote (+ 2 3)))))
  (is (unquote-splicing-form? (quote (unquote-splicing (+ 2 3)))))

  (is= (tmpl-quote-impl (quote {:a 1 :b (unquote (+ 2 3))}))
    {:a 1, :b 5})

  (is= (tmpl-quote {:a 1 :b (unquote (+ 2 3))})
    {:a 1, :b 5})

  (let [result (tmpl-quote (list 1 2 (unquote (inc 2)) 4 5))]
    (is (list? result))
    (is= result (quote (1 2 3 4 5)))))

(def vv [2 3 4])
(dotest
  (is= (tmpl-quote-impl (quote [1 (unquote-splicing (range 2 4)) 4]))
    [1 2 3 4])
  (is= (tmpl-quote-impl (quote [1 (unquote-splicing tst.demo.core/vv) 5])) ; must be fully-qualified Var here
    [1 2 3 4 5])

  (is= (tmpl-quote [1 (unquote-splicing vv) 5]) ; unqualified name OK here
    [1 2 3 4 5])
  (is= (tmpl-quote [1 (unquote-splicing (thru 2 4)) 5]) ; unqualified name `thru` also works
    [1 2 3 4 5]) )


