(ns property-based.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clojure.data.fressian :as fr]
            [clojure.data.priority-map :refer [priority-map]]
            [clojure.data.avl :as avl]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

; round trip
(defspec encode-decode-json-produces-original
  500
  (prop/for-all [value (gen/map gen/string gen/string)]
    (= value (json/read-str (json/write-str value)))))

(defspec encode-decode-fressian-produces-original
  500
  (prop/for-all [value (gen/map gen/string gen/string)]
    (= value (fr/read (fr/write value)))))

; input/output relation
(defn ascending? [coll]
 (every? (fn [[a b]] (<= (.compareTo a b) 0))
        (partition 2 1 coll)))

(defspec pri-map-values-are-sorted
  500
  (prop/for-all [value (gen/map gen/string gen/string)]
    (ascending? (vals (into (priority-map) value)))))

; trusted implementation

(defspec avl-sorted-maps-same-as-builtin
  500
  (prop/for-all [value (gen/map gen/string gen/string)]
    (= (into sorted-map value) (into avl/sorted-map value))))
