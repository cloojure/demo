(ns demo.core
  (:use tupelo.core tupelo.forest)
  (:require
    [overtone.at-at :as at]
    )
  (:gen-class))

(comment
(defmacro with-timer
  "Prints `id` and the elapsed (elapsed) execution time for a set of forms."
  [id & forms]
  `(let [start#   (System/nanoTime)
         result#  (do ~@forms)
         stop#    (System/nanoTime)
         elapsed# (double (- stop# start#))
         secs#    (/ elapsed# 1e9)]
     (println (format ":with-timer   %s   = %.3f sec" ~id secs#))
     result#))
  )

(def plus-nil-zero (fnil + 0))
(def timer-stats (atom {}))
(defn timer-stats-reset
  "Reset timer-stats to empty"
  [] (reset! timer-stats {}))


(defmacro with-timer-accum
  "Prints `id` and the elapsed (elapsed) execution time for a set of forms."
  [id & forms]
  (do
    (when-not (keyword? id)
      (throw (ex-info "id must be a keyword" (vals->map id))))
    (println (ns-name *ns*))
    (println :meta (meta &form))
    `(let [start#   (System/nanoTime)
           result#  (do ~@forms)
           stop#    (System/nanoTime)
           elapsed# (double (- stop# start#))
           secs#    (/ elapsed# 1e9)]
       (swap! timer-stats update ~id plus-nil-zero secs#))))

(defmacro defnp
  [name & forms]
  (let [fqid       (keyword (str (ns-name *ns*) "/" name))
        form-0     (first forms)
        [docstring args-code] (if (string? form-0)
                                [form-0 (rest forms)]
                                ["" forms])
        args-vec   (first args-code)
        code-forms (rest args-code)
        ]
    (list 'defn name docstring args-vec
      `(with-timer-accum ~fqid ~@code-forms))))

(defn sleep [millis] (Thread/sleep millis))
(defn sleep-20 [] (println :sleep-20) (sleep 20))
(defn sleep-50 [] (println :sleep-50) (sleep 50))
(defn sleep-100 [] (println :sleep-100) (sleep 100))

(newline)
(println "********************")
(pretty (macroexpand-1
           '(defnp sleep-99 []
              (println :sleep-99)
              (sleep 99))))
(println "********************")
(newline)
(defnp sleep-99 [] (println :sleep-99) (sleep 99))

; (def atat-pool (at/mk-pool))
;(spyx (plus-nil-zero nil 1))

(defn -main [& args]

  ; (System/exit 0)
  )
