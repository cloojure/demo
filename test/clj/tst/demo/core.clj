(ns tst.demo.core
  (:use tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
    ))

(comment
  (let [counter (atom nil)]
    (set-it counter 0)
    (set-it counter (+ it 1))
    (satyr counter (+ counter 1))
    )
  )

(defn atom?
  "Returns true iff the arg is a clojure.lang.Atom."
  [arg] (= (type arg) clojure.lang.Atom))
(defn agent?
  "Returns true iff the arg is a clojure.lang.Agent."
  [arg] (= (type arg) clojure.lang.Agent))
(defn ref?
  "Returns true iff the arg is a clojure.lang.Ref."
  [arg] (= (type arg) clojure.lang.Ref))

(defmacro set-it
  [state & forms]
  `(do
     (spyx (var? ~state))
     (cond
       (atom? ~state) (swap! ~state
                        (fn [~'it]
                          ~@forms))

       (agent? ~state) (send ~state
                         (fn [~'it]
                           ~@forms))

       (ref? ~state) (alter ~state
                       (fn [~'it]
                         ~@forms))

       (var? ~state) (alter-var-root ~state
                       (fn [~'it]
                         ~@forms))

       :else (throw (ex-info "Illegal state arg" {:state ~state
                                                  :type  (type ~state)}))
       )))

(defmacro var-set-dynamic-1
  [var-sym val] (list 'set! var-sym val))
(defmacro set-it-dynamic
  [var-sym & forms]
  `(let [~'it ~var-sym]
     (clojure.core/var-set (var ~var-sym) ~@forms)))
(defmacro get-it-local
  [var-sym] `(clojure.core/var-get ~var-sym ))
(defmacro set-it-local
  [var-obj & forms]
  `(let [~'it (clojure.core/deref ~var-obj)] ; or var-get
     (clojure.core/var-set ~var-obj ~@forms)))

(defmacro var-anon
  [val]
  `(def new-var# ~val))

(def five 5)
(dotest
  (spyx (var? five))
  (spyx (var? (var five)))
 ;(spyx (bound? six))
  (is (atom? (atom nil)))
  (isnt (atom? (agent 0)))
  (throws? (set-it {} 5))

  (nl) (println :var-dynamic)
  (def ^:dynamic fred nil)
  (let [fn2 (fn []
              (spyx (var? fred))
              (spyx (bound? (var fred)))
              (spyx (thread-bound? (var fred)))
              (spyx :in fred)
             ;(set! fred 99)
             ;(var-set (var fred) 99)
              (set-it-dynamic fred 77) ; better than:  (set! fred 99)
              (spyx :out fred)
              (set-it-dynamic fred (+ fred 11)) ; better than:  (set! fred 99)
              (spyx :out fred)
              (set-it-dynamic fred (+ it 11)) ; better than:  (set! fred 99)
              (spyx :out fred)
              )]
    (spyx :base fred)
    (binding [fred 5]
      (spyx :before fred)
      (fn2)
      (spyx :after fred))
    (spyx :base fred))

  (nl) (println :var-local)
  (with-local-vars [x 1]
    (spyx @x)
    (set-it-local x 3)
    (spyx (get-it-local x))
    (set-it-local x (* it 5))
    (spyx (get-it-local x))
    (spyx @x)
    )


  (nl) (println :var-anon)
  (let [vv (var-anon nil)]
    (spyx @vv)
    (set-it vv 0)
    (spyx @vv)
    (set-it vv (+ it 1))
    (spyx @vv))

  (nl) (println :atom)
  (let [counter (atom nil)]
    (spyx @counter)
    (set-it counter 0)
    (spyx @counter)
    (set-it counter (+ it 1))
    (spyx @counter) )

  (nl) (println :agent)
  (let [counter (agent nil)]
    (spyx @counter)
    (set-it counter 0)
    (Thread/sleep 99) (spyx @counter)
    (set-it counter (+ it 1))
    (Thread/sleep 99) (spyx @counter) )

  (nl) (println :ref)
  (let [counter (ref nil)]
    (spyx (type counter))
    (spyx @counter)
    (dosync
      (set-it counter 0)
      (Thread/sleep 99)) (spyx @counter)
    (dosync
      (set-it counter (+ it 1)))
    (Thread/sleep 99) (spyx @counter) )
  )


