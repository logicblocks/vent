(ns vent.test-support.data
  (:require
   [faker.lorem :as lorem])
  (:import
   [java.util UUID]))

(def url-template "https://%s.com/%s/%s")

(defn random-uuid-string []
  (str (UUID/randomUUID)))

(defn random-url
  ([] (random-url (random-uuid-string)))
  ([id]
   (let [words (take 2 (lorem/words))]
     (format url-template (first words) (last words) id))))

(defn random-payload
  ([] (random-payload {}))
  ([overrides]
   (merge
     (into {} [[(random-uuid-string) (random-uuid-string)]
               [(random-uuid-string) (random-uuid-string)]])
     overrides)))
