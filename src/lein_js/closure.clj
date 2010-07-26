(ns lein-js.closure
  (:import [com.google.javascript.jscomp CompilerOptions JSSourceFile
	    CompilationLevel WarningLevel ClosureCodingConvention
	    DiagnosticGroups CheckLevel DefaultCodingConvention]
	   [com.google.common.base Charsets])
  (:use [clojure.contrib.string :only [lower-case]]
	[clojure.contrib.io :only [file]])
  (:require [clojure.contrib.duck-streams :as duck-streams]))

;; TODO
;; Make charset configurable

(def default-options
     {:compilation-level :simple-optimizations
      :warning-level :default
      :coding-convention :default
      :summary-detail :print-on-errors
      :compilation-errors []
      :compilation-warnings ["FILEOVERVIEW_JSDOC" "UNKNOWN_DEFINES"]
      :compilation-ignored ["DEPRECATED" "VISIBILITY" "ACCESS_CONTROLS"
			    "STRICT_MODULE_DEP_CHECK" "MISSING_PROPERTIES"
			    "CHECK_TYPES"]
      :pretty-print false
      :print-input-delimiter false
      :process-closure-primitives false
      :manage-closure-deps false})

(def compilation-levels
     {:whitespace-only CompilationLevel/WHITESPACE_ONLY
      :simple-optimizations CompilationLevel/SIMPLE_OPTIMIZATIONS
      :advanced-optimizations CompilationLevel/ADVANCED_OPTIMIZATIONS})

(def warning-levels
     {:quiet WarningLevel/QUIET
      :default WarningLevel/DEFAULT
      :verbose WarningLevel/VERBOSE})

(def summary-details
     {:never-print 0
      :print-on-errors 1
      :print-if-type-checking 2
      :always-print 3})

(def diagnostic-groups (seq (.getFields DiagnosticGroups)))

(defn- filter-fields
  [group-names]
  (let [name-set (set group-names)]
    (filter #(name-set (.getName %)) diagnostic-groups)))

(defn- set-warning-level
  [compiler-options groups level]
  (doseq [field (filter-fields groups)]
    (.setWarningLevel compiler-options (.get field nil) level)))

;; See DiagnosticGroups.setWarningLevels
(defn- set-diagnostics
  [compiler-options options]
  (set-warning-level compiler-options (:compilation-errors options) CheckLevel/ERROR)
  (set-warning-level compiler-options (:compilation-warnings options) CheckLevel/WARNING)
  (set-warning-level compiler-options (:compilation-ignored options) CheckLevel/OFF))

(defn- make-compiler-options
  [options output]
  (let [options (merge default-options options)
	compiler-opts (CompilerOptions.)]
    (.setOptionsForCompilationLevel ((:compilation-level options) compilation-levels)
				    compiler-opts)
    (.setOptionsForWarningLevel ((:warning-level options) warning-levels) compiler-opts)
    (set! (. compiler-opts prettyPrint) (:pretty-print options))
    (set! (. compiler-opts printInputDelimiter) (:print-input-delimiter options))
    (set! (. compiler-opts closurePass) (:process-closure-primitives options))
    (set! (. compiler-opts jsOutputFile) output)
    (set-diagnostics compiler-opts options)
    (doto compiler-opts
      (.setCodingConvention (if (= (:coding-convention options) :closure)
			      (ClosureCodingConvention.)
			      (DefaultCodingConvention.)))
      (.setSummaryDetailLevel ((:summary-detail options) summary-details))
      (.setManageClosureDependencies (boolean (:manage-closure-deps options))))))

(defn- write-output
  [compiler output]
  (println "Writing result to" (.getAbsolutePath output))
  (duck-streams/spit output (.toSource compiler)))

(defn run
  [inputs output options]
  (let [inputs (map #(JSSourceFile/fromFile % Charsets/UTF_8) inputs)
	externs (map #(JSSourceFile/fromFile % Charsets/UTF_8) (:externs options))
	compiler-options (make-compiler-options options output)
	compiler (com.google.javascript.jscomp.Compiler. System/err)]
    (com.google.javascript.jscomp.Compiler/setLoggingLevel java.util.logging.Level/WARNING)
    (doto compiler
      (.compile (into-array JSSourceFile externs)
		(into-array JSSourceFile inputs)
		compiler-options))
    (write-output compiler (file output))))