(ns todo-list.client
  (:require [reagent.core :as r]))

;; TODO: add ability to check/uncheck
;; TODO: add filters: all, active, completed
;; TODO: add counter of elements left
;; TODO: add ability to clear completed elements
;; TODO: add check/uncheck all elements
;; TODO: use bulma for styling

(def filters [:all :completed :active])
(def initial-state {:filter  :all
                    :todos   []
                    :next-id 0})

(def todo-list (r/atom initial-state))

(defn add-todo [todo]
  (let [id (:next-id @todo-list)
        new-todos (-> @todo-list
                      :todos
                      (conj {:title todo :id id}))]
    (swap! todo-list #(-> %
                          (assoc :todos new-todos)
                          (update :next-id inc)))))

(defn delete-todo [id]
  (let [new-todos (->> (:todos @todo-list)
                       (filter #(not= id (:id %))))]
    (swap! todo-list #(assoc % :todos new-todos))))

(defn get-todos []
  (:todos @todo-list))

(defn lister [items]
  [:ul
   (for [item items]
     ^{:key (:id item)} [:li
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

(defn home-page []
  (fn []
    [:div
     [:h1 "My todos:"]
     [input]
     [lister (get-todos)]]))

(r/render [home-page]
          (-> js/document
              (.getElementById "app")))
