(ns js-test
  (:use [leiningen.js] :reload-all)
  (:use [clojure.test]
        [clojure.contrib.io :only [delete-file file]])
  (:import [java.io File]))


(deftest test-js
  (let [f1 (File/createTempFile "test-js" ".js")
	f2 (File/createTempFile "test-js" ".js")
	result (file (.getParent f1) "compiled.js")]
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (delete-file result true)
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "." 
	      :bundles ["compiled.js" [(.getName f1)
				       (.getName f2)]]}})
    (is (.exists result))))

(deftest test-js-options
  (let [f1 (File/createTempFile "test-js" ".js")
	f2 (File/createTempFile "test-js" ".js")
	result (file (.getParent f1) "compiled.js")]
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (delete-file result true)
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "." 
	      :bundles ["compiled.js" [(.getName f1)
					  (.getName f2)]]
	      :options {:print-input-delimiter true}}})
    (is (re-find #"Input 0" (slurp result)))))

(deftest test-js-devel-options
  (let [f1 (File/createTempFile "test-js" ".js")
	f2 (File/createTempFile "test-js" ".js")
	result (file (.getParent f1) "compiled.js")]
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (delete-file result true)
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "." 
	      :bundles ["compiled.js" [(.getName f1)
					  (.getName f2)]]
	      :options {:print-input-delimiter false}
	      :devel-options {:print-input-delimiter true}}})
    (is (re-find #"Input 0" (slurp result)))))
  