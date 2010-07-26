(defproject lein-js "0.1.0-SNAPSHOT"
  :description "A Leiningen plugin for concatenating and compiling JavaScript files using Google's Closure Compiler. "
  ;; Depends on the Google Closure Compiler, but it is not available on a Maven repo.
  ;; See http://code.google.com/p/closure-compiler/issues/detail?id=37
  ;; I may push a copy to Clojars, but this seems like an abuse of the system.
  
  ;; :dependencies [[com.google/closure-compiler "0.0.1-SNAPSHOT"]]
  :dev-dependencies [[org.clojure/clojure "1.2.0-beta1"]
		     [org.clojure/clojure-contrib "1.2.0-beta1"]
		     [lein-clojars "0.5.0-SNAPSHOT"]
		     [swank-clojure "1.2.1"]])