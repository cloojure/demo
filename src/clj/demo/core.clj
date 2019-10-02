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

(defn stats-update
  "Updates timing stats for a given key"
  [id seconds]
  (swap! timer-stats
    (fn update-stats-fn
      [stats-map]
      (let [stats-curr (if (contains? stats-map id)
                         (grab id stats-map)
                         {:n    0
                          :sum  0.0
                          :sum2 0.0})]
        (with-map-vals stats-curr [n sum sum2]
          (let [stats-new     {:n    (inc n)
                               :sum  (+ sum seconds)
                               :sum2 (+ sum2 (* seconds seconds))}
                stats-map-new (assoc stats-map id stats-new) ]
            stats-map-new))))))

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
       (stats-update ~id secs#)
      ;(swap! timer-stats update ~id plus-nil-zero secs#)
       )))

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

(defn stats-get
  "Return basic stats for a given id"
  [id]
  (let [stats-raw (grab id @timer-stats)
        n         (grab :n stats-raw)
        sum       (grab :sum stats-raw)
        sum2      (grab :sum2 stats-raw)
        mean-x    (/ sum n)
        mean2-x   (* mean-x mean-x)
        mean-x2   (/ sum2 n)
        sigma2-x  (- mean-x2 mean2-x)
        sigma-x   (Math/sqrt sigma2-x)]
    {:n n :mean mean-x :sigma sigma-x}))

(defn stats-get-all
  "Return all stats"
  []
  (let [ result (apply glue
                  (forv [k (keys @timer-stats)]
                    {k (stats-get k)}))]
    result))

(defn stats-print-all
  []
  (doseq [k (sort-by :mean (keys @timer-stats))]
    (let [stats  (stats-get k)
          n (grab :n stats)
          mean (grab :mean stats)
          sigma (grab :sigma stats) ]
    (println (format "%-30s %5d %9.5f %10.8f" k n mean sigma ))) )
  )

;(newline)
;(println "********************")
;(pretty (macroexpand-1
;           '(defnp sleep-99 []
;              (println :sleep-99)
;              (sleep 99))))
;(println "********************")
;(newline)

; (def atat-pool (at/mk-pool))
;(spyx (plus-nil-zero nil 1))

(defn -main [& args]

  ; (System/exit :0)
  )
