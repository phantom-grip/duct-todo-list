(ns todo-list.handler.example
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defmethod ig/init-key :todo-list.handler/example [_ options]
  (context "/example" []
    (GET "/" []
      (io/resource "todo_list/handler/example/example.html"))))
