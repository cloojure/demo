(ns tst.demo.core
  (:use tupelo.core tupelo.test))

(defn theta
  [[a b -c-]]
  (Math/atan2 (double a) (double b)))

(defn radius
  [[a b c]]
  ; this uses the assumption that it is illegal for both a and b to be zero
  (let [n2 (double (+ (* a a) (* b b)))
        n  (Math/sqrt n2)
        r  (/ (- c) n)]
    r))

(defn parallel?
  [x y]
  (let [same-theta       (rel= (theta x) (theta y) :digits 9)
        different-radius (not (rel= (radius x) (radius y) :digits 9))]
    (and same-theta
         different-radius)))

(dotest
  (let [pi-ovr-4 (/ Math/PI 4)]
    (is (rel= pi-ovr-4 (theta [1 1 1]) :digits 9))
    (is (rel= pi-ovr-4 (theta [1 1 2]) :digits 9)))

  (is (parallel? [1 1 1] [1 1 2]))
  (isnt (parallel? [1.00001 1 1] [1 1 2]))
  (is (parallel? [1 2 1] [2 4 9]))

  (is (parallel? [2 4 1] [1 2 1]))
  (isnt (parallel? [2 4 1] [4 2 1]))
  (is (parallel? [0 1 5] [0 1 5.0000001])) ; slightly offset => parallel
  (isnt (parallel? [0 1 5] [0 1 5.000000000001])) ; within rounding error => non-parallel

  (isnt (parallel? [1 2 5] [100 200 500]))
  )
