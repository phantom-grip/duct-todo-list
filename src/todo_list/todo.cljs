(ns todo-list.todo
  (:require [nano-id.core :refer [nano-id]]))

(comment (def sample-todo {
                           :description "Sample title"
                           :id          0
                           :completed   false
                           }))

(defn create [description]
  {:description description
   :id          (nano-id)
   :completed   false})

(defn equals? [todo1 todo2]
  (= (:id todo1) (:id todo2)))

(defn toggle [todo]
  (update todo :completed #(not %)))

(defn complete [todo]
  (assoc todo :completed true))

(defn uncomplete [todo]
  (assoc todo :completed false))

(defn completed? [todo]
  (:completed todo))

(defn active? [todo]
  (false? (:completed todo)))

(defn get-id [todo]
  (:id todo))

(defn get-description [todo]
  (:description todo))