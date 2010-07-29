(ns js-test
  (:use [leiningen.js] :reload-all)
  (:use [clojure.test]
        [clojure.contrib.io :only [delete-file-recursively file make-parents]])
  (:import [java.io File]))

(defn test-file
  [& names]
  (apply file "test-js-temp" names))

(deftest test-js
  (let [f1 (test-file "test-js-1.js")
	f2 (test-file "test-js-2.js")
	result (test-file "compiled.js")]
    (make-parents f1)
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "." 
	      :bundles ["compiled.js" [(.getName f1)
				       (.getName f2)]]}})
    (is (.exists result))
    (delete-file-recursively (file "test-js-temp") true)))

(deftest test-js-options
  (let [f1 (test-file "test-js-1.js")
	f2 (test-file "test-js-2.js")
	result (test-file "compiled.js")]
    (make-parents f1)
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "." 
	      :bundles ["compiled.js" [(.getName f1)
				       (.getName f2)]]
	      :options {:print-input-delimiter true}}})
    (is (re-find #"Input 0" (slurp result)))
    (delete-file-recursively (file "test-js-temp") true)))

(deftest test-js-devel-options
  (let [f1 (test-file "test-js-1.js")
	f2 (test-file "test-js-2.js")
	result (test-file "compiled.js")]
    (make-parents f1)
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "." 
	      :bundles ["compiled.js" [(.getName f1)
				       (.getName f2)]]
	      :options {:print-input-delimiter false}
	      :devel-options {:print-input-delimiter true}}})
    (is (re-find #"Input 0" (slurp result)))
    (delete-file-recursively (file "test-js-temp") true)))

(deftest test-non-existent-deploy-dir
  (let [f1 (test-file "test-js-1.js")
	f2 (test-file "test-js-2.js")
	result (test-file "new-directory" "compiled.js")]
    (make-parents f1)
    (spit f1 "function foo() { }")
    (spit f2 "function bar() { }")
    (js {:root (.getParent f1)
	 :js {:src "."
	      :deploy "new-directory" 
	      :bundles ["compiled.js" [(.getName f1)
				       (.getName f2)]]}})
    (is (.exists result))
    (delete-file-recursively (file "test-js-temp") true)))