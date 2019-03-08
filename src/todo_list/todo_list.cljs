(ns todo-list.todo-list
  (:require [todo-list.todo :as t]))

(def FILTERS {
              :all       :all
              :completed :completed
              :active    :active
              })

(defn create []
  {:filter (FILTERS :all)
   :todos  []})

(defn update-todos [todo-list todos]
  (assoc todo-list :todos todos))

(defn get-all-todos [todo-list]
  (:todos todo-list))

(defn get-completed-todos [todo-list]
  (->> (get-all-todos todo-list)
       (filter t/completed?)))

(defn get-active-todos [todo-list]
  (->> (get-all-todos todo-list)
       (filter t/active?)))

(defn get-filter [todo-list]
  (:filter todo-list))

(defn get-todos [todo-list]
  (let [[all completed active f] ((juxt
                                    get-all-todos
                                    get-completed-todos
                                    get-active-todos
                                    get-filter)
                                   todo-list)]
    (case f
      :all
      all
      :completed
      completed
      :active
      active
      all)))

(defn add-todo [todo-list todo]
  (let [new-todos (-> (get-all-todos todo-list)
                      (conj todo))]
    (update-todos todo-list new-todos)))

(defn delete-todo [todo-list todo]
  (let [new-todos (->> (get-all-todos todo-list)
                       (remove #(t/equals? % todo)))]
    (update-todos todo-list new-todos)))

(defn toggle-todo [todo-list todo]
  (let [new-todos (->> (get-all-todos todo-list)
                       (map #(if (t/equals? % todo)
                               (t/toggle %)
                               %)))]
    (update-todos todo-list new-todos)))

(defn complete-all [todo-list]
  (let [new-todos (->> (get-all-todos todo-list)
                       (map t/complete))]
    (update-todos todo-list new-todos)))

(defn uncomplete-all [todo-list]
  (let [new-todos (->> (get-all-todos todo-list)
                       (map t/uncomplete))]
    (update-todos todo-list new-todos)))

(defn all-todos-completed? [todo-list]
  (->> (get-all-todos todo-list)
       (every? t/completed?)))

(defn remove-all-completed-todos [todo-list]
  (let [new-todos (->> (get-all-todos todo-list)
                       (remove t/completed?))]
    (update-todos todo-list new-todos)))

(defn update-filter [todo-list filter]
  (assoc todo-list :filter filter))