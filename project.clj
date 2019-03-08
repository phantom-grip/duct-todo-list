(defproject todo-list "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [duct/core "0.7.0"]
                 [duct/module.logging "0.4.0"]
                 [duct/module.web "0.7.0"]
                 [duct/module.cljs "0.4.0"]
                 [reagent "0.8.1"]
                 [nano-id "0.9.3"]]
  :plugins [[duct/lein-duct "0.11.2"]]
  :main ^:skip-aot todo-list.main
  :uberjar-name "todo-list-standalone.jar"
  :resource-paths ["resources" "target/resources"]
  :prep-tasks ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware [lein-duct.plugin/middleware]
  :profiles
  {:dev          [:project/dev :profiles/dev]
   :repl         {:prep-tasks   ^:replace ["javac" "compile"]
                  :dependencies [[cider/piggieback "0.3.10"]]
                  :repl-options {:init-ns          user
                                 :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}
   :uberjar      {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[integrant/repl "0.3.1"]
                                   [eftest "0.5.4"]
                                   [kerodon "0.9.0"]]}})
