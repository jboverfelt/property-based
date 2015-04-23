(ns property-based.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clojure.data.priority-map :refer [priority-map]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

(defspec encode-decode-produces-original
  500
  (prop/for-all [value (gen/map gen/string gen/string)]
    (= value (json/read-str (json/write-str value)))))

(defn ascending? [coll]
 (every? (fn [[a b]] (<= (.compareTo a b) 0))
        (partition 2 1 coll)))

(defspec values-are-sorted
  500
  (prop/for-all [value (gen/map gen/string gen/string)]
    (ascending? (vals (into (priority-map) value)))))
