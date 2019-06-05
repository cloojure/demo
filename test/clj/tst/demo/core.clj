(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:import [java.time ZonedDateTime ZoneId]))

(defn inst->date-time
  "Convert a java.time.Instant to a DateTime for the supplied ZoneId"
  [instant zoneid]
  (.toLocalDate
    (ZonedDateTime/ofInstant instant zoneid)))

(dotest
  (let [may-4    #inst "2018-05-04T01:23:45.678-00:00" ; a java.util.Date
        instant  (.toInstant may-4) ]
    (spyxx may-4)
    (spyx instant)
    (println "utc =>" (inst->date-time instant (ZoneId/of "UTC")))
    (println "nyc =>" (inst->date-time instant (ZoneId/of "America/New_York")))
    ))
