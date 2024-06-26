(ns vent.util
  (:import [java.lang.reflect Method]))

; Inspired by:
; https://stackoverflow.com/questions/1696693/clojure-how-to-find-out-the-arity-of-function-at-runtime
(defn highest-arity-of
  "Returns the maximum arity of:
    - anonymous functions like `#()` and `(fn [])`.
    - defined functions like `map` or `+`.
    - macros, by passing a var like `#'->`.

  Returns `:variadic` if the function/macro is variadic."
  [f]
  (let [func (if (var? f) @f f)
        methods (->> func class .getDeclaredMethods
                  (map #(vector (.getName ^Method %)
                          (count (.getParameterTypes ^Method %)))))
        var-args? (some #(-> % first #{"getRequiredArity"})
                    methods)]
    (if var-args?
      :variadic
      (let [max-arity (->> methods
                        (filter (comp #{"invoke"} first))
                        (sort-by second)
                        last
                        second)]
        (if (and (var? f) (-> f meta :macro))
          (- max-arity 2)
          max-arity)))))

(defn invoke-highest-arity [f & args]
  (let [highest-arity (highest-arity-of f)]
    (cond
      (= highest-arity :variadic) (apply f args)
      (zero? highest-arity) (f)
      :else (apply f (take highest-arity args)))))

(defn deep-merge
  [a b]
  (if (map? a)
    (into a (for [[k v] b] [k (deep-merge (a k) v)]))
    b))