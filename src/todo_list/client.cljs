(ns todo-list.client
  (:require [reagent.core :as r]))

(defn hello-world []
  [:h1 "Hello world"])

(r/render [hello-world]
          (-> js/document
              (.getElementById "app")))
