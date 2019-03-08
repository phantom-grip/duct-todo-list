(ns todo-list.client
  (:require [reagent.core :as r]
            [todo-list.todo :as todo]
            [todo-list.todo-list :as tdl]))

;; TODO: use bulma for styling

(enable-console-print!)

(defn filters [todo-list]
  (let [change-filter (fn [f]
                        (swap! todo-list #(tdl/update-filter % f)))]
    [:ul
     [:li [:button {:on-click #(change-filter :all)} "Show All"]]
     [:li [:button {:on-click #(change-filter :active)} "Show Active"]]
     [:li [:button {:on-click #(change-filter :completed)} "Show Completed"]]]))

(defn lister [todo-list]
  (let [items (tdl/get-todos @todo-list)
        toggle-todo (fn [item]
                      (swap! todo-list #(tdl/toggle-todo % item)))
        delete-todo (fn [item]
                      (swap! todo-list #(tdl/delete-todo % item)))]
    [:ul
     (for [item items]
       ^{:key (todo/get-id item)} [:li
                                   {:style (if (todo/completed? item)
                                             {:text-decoration "line-through"}
                                             {})}
                                   [:input {:type      "checkbox"
                                            :checked   (todo/completed? item)
                                            :on-change #(toggle-todo item)}]
                                   (todo/get-description item)
                                   [:button
                                    {:on-click #(delete-todo item)}
                                    "Delete"]])]))

(defn input [todo-list]
  (let [value (r/atom "")
        handle-click #(reset! value (-> % .-target .-value))
        handle-submit (fn [e]
                        (do
                          (.preventDefault e)
                          (swap! todo-list #(->> (todo/create @value)
                                                 (tdl/add-todo %)))
                          (reset! value "")))]
    (fn []
      [:form
       {:on-submit handle-submit}
       [:input {:type      "text"
                :value     @value
                :on-change handle-click}]])))

(defn elements-left [todo-list]
  [:div (str "Elements left: " (tdl/count-active-todos @todo-list))])

(defn remove-completed-btn [todo-list]
  (let [handle-click (fn []
                       (swap! todo-list #(tdl/remove-all-completed-todos %)))
        disabled? (zero? (tdl/count-completed-todos @todo-list))]
    [:button
     {:on-click handle-click
      :disabled disabled?}
     "Remove all completed elements"]))

(defn complete-uncomplete-button [todo-list]
  (let [handle-click (fn []
                       (if (tdl/all-todos-completed? @todo-list)
                         (swap! todo-list #(tdl/uncomplete-all %))
                         (swap! todo-list #(tdl/complete-all %))))
        btn-text (if (tdl/all-todos-completed? @todo-list)
                   "Uncomplete all"
                   "Complete all")
        disabled? (zero? (tdl/count-all-todos @todo-list))]
    [:button
     {:on-click handle-click
      :disabled disabled?}
     btn-text]))

(def tdl-atom (r/atom (tdl/create)))

(defn page []
  (let []
    [:<>
     [filters tdl-atom]
     [elements-left tdl-atom]
     [input tdl-atom]
     [complete-uncomplete-button tdl-atom]
     [remove-completed-btn tdl-atom]
     [lister tdl-atom]]))

(r/render [page]
          (-> js/document
              (.getElementById "app")))
