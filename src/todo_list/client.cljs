(ns todo-list.client
  (:require [reagent.core :as r]))

;; TODO: use bulma for styling

(enable-console-print!)

(def filters [:all :completed :active])
(def initial-state {:filter  :all
                    :todos   []
                    :next-id 0})

(def todo-list (r/atom initial-state))

(defn add-todo [todo]
  (let [id (:next-id @todo-list)
        new-todos (-> @todo-list
                      :todos
                      (conj {:title todo :id id :checked false}))]
    (swap! todo-list #(-> %
                          (assoc :todos new-todos)
                          (update :next-id inc)))))

(defn delete-todo [id]
  (let [new-todos (->> (:todos @todo-list)
                       (filter #(not= id (:id %))))]
    (swap! todo-list #(assoc % :todos new-todos))))

(defn toggle-todo [id]
  (let [new-todos (->> (:todos @todo-list)
                       (map #(if (= id (:id %))
                               (update % :checked not)
                               %)))]
    (swap! todo-list #(assoc % :todos new-todos))))

(defn get-todos []
  (let [f (:filter @todo-list)
        todos (:todos @todo-list)]
    (case f
      :all
      todos
      :completed
      (filter #(= true (:checked %)) todos)
      :active
      (filter #(= false (:checked %)) todos))))

(defn count-elements-left []
  (->> (:todos @todo-list)
       (filter #(false? (:checked %)))
       count))

(defn count-completed-elements []
  (->> (:todos @todo-list)
       (filter #(true? (:checked %)))
       count))

(defn change-filter [new-filter]
  (do (swap! todo-list #(assoc % :filter new-filter))))

(defn remove-completed-elements []
  (let [todos-without-completed (->> (:todos @todo-list)
                                     (filter #(false? (:checked %))))]
    (swap! todo-list #(assoc % :todos todos-without-completed))))

(defn is-all-completed []
  (->> (:todos @todo-list)
       (every? #(true? (:checked %)))))

(defn complete-all-todos []
  (let [todos-completed (->> (:todos @todo-list)
                             (map #(assoc % :checked true)))
        _ (println "here")]
    (swap! todo-list #(assoc % :todos todos-completed))))

(defn uncomplete-all-todos []
  (let [todos-uncompleted (->> (:todos @todo-list)
                               (map #(assoc % :checked false)))]
    (swap! todo-list #(assoc % :todos todos-uncompleted))))

(defn count-todos []
  (->> (:todos @todo-list)
       count))

(defn complete-uncomplete-all-btn []
  [:button
   {:on-click (if (is-all-completed)
                uncomplete-all-todos
                complete-all-todos)
    :disabled (= 0 (count-todos))}
   (if (is-all-completed)
     "Uncomplete all"
     "Complete all")])

(defn lister [items]
  [:ul
   (for [item items]
     ^{:key (:id item)} [:li
                         {:style (if (:checked item)
                                   {:text-decoration "line-through"}
                                   {})}
                         [:input {:type     "checkbox"
                                  :checked  (:checked item)
                                  :on-click #(toggle-todo (:id item))}]
                         (:title item)
                         [:button
                          {:on-click #(delete-todo (:id item))}
                          "Delete"]])])


(defn input []
  (let [value (r/atom "")]
    (fn []
      [:form
       {:on-submit (fn [e]
                     (do
                       (.preventDefault e)
                       (add-todo @value)
                       (reset! value "")))}
       [:input {:type      "text"
                :value     @value
                :on-change #(reset! value (-> % .-target .-value))}]])))

(defn elements-left []
  [:div (str "Elements left: " (count-elements-left))])

(defn remove-completed-btn []
  [:button
   {:on-click remove-completed-elements
    :disabled (= 0 (count-completed-elements))}
   "Remove all completed elements"])

(defn home-page []
  (fn []
    [:div
     [:h1 "My todos:"]
     [:ul
      [:li [:button {:on-click #(change-filter :all)} "Show All"]]
      [:li [:button {:on-click #(change-filter :active)} "Show Active"]]
      [:li [:button {:on-click #(change-filter :completed)} "Show Completed"]]]
     [input]
     [complete-uncomplete-all-btn]
     [elements-left]
     [lister (get-todos)]
     [remove-completed-btn]]))

(r/render [home-page]
          (-> js/document
              (.getElementById "app")))
