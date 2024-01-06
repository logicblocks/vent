(ns vent.test-support.selectors.greater-than-selector
  (:require [vent.core :as v]))

(defrecord GreaterThanSelector [event initial-context key-fn value]
  v/Selector
  (selects? [_ context]
    (> (key-fn context) value)))

(defn greater-than-selector [& {:as options}]
  (map->GreaterThanSelector options))

(defn greater-than [key-fn value]
  (fn [event context]
    (greater-than-selector
      :event event
      :initial-context context
      :key-fn key-fn
      :value value)))
