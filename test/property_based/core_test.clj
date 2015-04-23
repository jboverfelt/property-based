(ns property-based.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

(defspec encode-decode-produces-original
  100
  (prop/for-all [value (gen/map gen/string gen/string)]
    (= value (json/read-str (json/write-str value)))))
