(ns demo.core
  (:use tupelo.core tupelo.forest)
  (:require
    [clojure.string :as str] ) )

; #todo move to tupelo
;(defmacro with-timer
;  [id & forms]
;  `(let [start#   (System/nanoTime)
;         result#  (do ~@forms)
;         stop#    (System/nanoTime)
;         elapsed# (double (- stop# start#))
;         secs#    (/ elapsed# 1e9)]
;     (println ~id (format "time = %.3f" secs#))
;     result#))
;
;(def xml-file-name
;   "data.xml"
;  ;"data-1k.xml"
;  ;"data-10k.xml"
;  ;"data-full.xml"
;  )
;(def bush-file-name "data-bush.edn")
;
;(defn load-xml-data  []
;  (-> xml-file-name
;    (io/resource)
;    (io/input-stream)
;    (slurp)))
;
;(defn load-bush-data  []
;  (let [bush-data (with-timer :load-bush-data
;                    (-> bush-file-name
;                      (io/resource)
;                      (io/input-stream)
;                      (slurp)
;                      (edn/read-string)))]
;    (spyx-pretty (clip-str 999 bush-data))
;  ))
;
;
;(defn xml->bush []
;  (with-forest (new-forest)
;    (let [xml-data      (with-timer :xml-data
;                          (load-xml-data))
;         ;>>            (println :xml-data \newline (clip-str 999 xml-data))
;          enlive-data   (with-timer :xml->enlive
;                          (xml->enlive xml-data))
;         ;>>            (println :enlive-data \newline (clip-str 999 enlive-data))
;          tree-data     (with-timer :enlive->tree
;                          (enlive->tree enlive-data))
;          root-hid      (with-timer :add-tree
;                          (add-tree tree-data))
;          >>            (with-timer :count-all-hid
;                          (spyx (count (all-hids))))
;          bush-data     (with-timer :hid->bush
;                          (hid->bush root-hid))
;         ;>>            (spyx-pretty (clip-str 999 bush-data))
;          bush-data-str (with-timer :pretty-str (pretty-str bush-data))
;          ]
;     ;(println (clip-str 999 bush-data-str))
;      (print "writing data...")
;      (spit (str "resources/" bush-file-name) bush-data-str)
;      (println "  done.")))
;  )
;
;
;(defn -main [& args]
;  (println "main - enter")
;  ;(println "main - hit <enter> to start:" ) (read-line)
; ;(xml->bush)
;  (load-bush-data)
;
;  ;(println "main - hit <enter> to exit:" ) (read-line)
;  (println "main - leave")
;  )
;

