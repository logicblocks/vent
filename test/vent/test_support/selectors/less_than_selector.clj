(ns vent.test-support.selectors.less-than-selector
  (:require
   [vent.core :as v]))

(defrecord LessThanSelector [event initial-context key-fn value]
  v/Selector
  (selects? [_ context]
    (< (key-fn context) value)))

(defn less-than-selector [& {:as options}]
  (map->LessThanSelector options))

(defn less-than [key-fn value]
  (fn [event context]
    (less-than-selector
      :event event
      :initial-context context
      :key-fn key-fn
      :value value)))
