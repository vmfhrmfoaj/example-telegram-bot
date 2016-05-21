(ns gguk-bro.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defonce https       (nodejs/require "https"))
(defonce querystring (nodejs/require "querystring"))

(defn -main [event context]
  (println (. js/JSON (stringify event)))
  (println (. js/JSON (stringify context))))

;; to fake CLJS compiler
(defn none [& _] nil)
(set! *main-cli-fn* none)
