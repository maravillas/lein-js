(ns leiningen.js
  (:import [java.io File])
  (:use [clojure.java.io :only [file]])
  (:require [lein-js.closure :as closure]))

(defn- add-path
  [path file]
  (str path File/separator file))

(defn- src-path
  [project]
  (add-path (:root project)
	    (or (:src (:js project)) "src/js")))

(defn- deploy-path
  [project]
  (add-path (:root project)
	    (or (:deploy (:js project)) "war/js")))

(defn- compile-bundle
  [inputs output project devel]
  (let [js-settings (:js project)
	input-paths (map #(add-path (src-path project) %) inputs)
	output-path (add-path (deploy-path project) output)
	options (merge {:pretty-print devel}
		       (:options js-settings)
		       (if devel
			 (:devel-options js-settings)
			 (:prod-options js-settings)))]
    (println "Compiling" (apply str (interpose ", " inputs)) "...")
    (closure/run input-paths output-path options)))

(defn js
  ([project]
     (js project "devel"))
  ([project action]
     (let [bundles (partition 2 (:bundles (:js project)))
	   devel (= action "devel")]
       (doseq [[output inputs] bundles]
	 (compile-bundle inputs output project devel)))))