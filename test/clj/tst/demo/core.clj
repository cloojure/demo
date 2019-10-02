(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test))

(defn sleep [millis] (Thread/sleep millis))
(defnp sleep-a [] (sleep 13))
(defnp sleep-b [] (sleep 14))
(defnp sleep-10 [] (sleep 10))
(defnp sleep-20
  "sleep 20 ms"
  [] (sleep 20))

(dotest
  (timer-stats-reset)
  (sleep-10) (sleep-10) (sleep-10) (sleep-10) (sleep-10) (sleep-10) (sleep-10) (sleep-10) (sleep-10) (sleep-10)
  (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20)
  (sleep-a) (sleep-a) (sleep-a) (sleep-a)
  (sleep-b)
  (sleep-20) (sleep-20) (sleep-20) (sleep-20) (sleep-20)

  (nl)
  (println "-----------------------------------------------------------------------------")
  (stats-print-all)

)

;(let [start       (System/currentTimeMillis)
;      elapsed-now (fn [] (- (System/currentTimeMillis) start))]
;  (println "alpha" (elapsed-now))
;  (at/after 1000 #(println "bravo" (elapsed-now)) atat-pool)
;  (at/every 500 sleep-20 atat-pool)
;  (at/every 500 sleep-50 atat-pool)
;  (at/every 500 sleep-100 atat-pool)
;  (println "charli" (elapsed-now))
;  (sleep 2345)
;  (println "tufte" (elapsed-now))
;
;  (sleep 999)
;  (println "fin")
;  (at/stop-and-reset-pool! atat-pool :strategy :kill)
;  (shutdown-agents)
;  )
