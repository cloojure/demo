(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test))

(dotest
  (reset! timer-stats {})
  (sleep-99) (sleep-99) (sleep-99) (sleep-99) (sleep-99)

  (with-timer-accum :sleep-20 (sleep-20))
  (with-timer-accum :sleep-20 (sleep-20))
  (with-timer-accum :sleep-20 (sleep-20))
  (with-timer-accum :sleep-50 (sleep-50))
  (with-timer-accum :sleep-100 (sleep-100))

  (spyx-pretty @timer-stats)

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
