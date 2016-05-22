(ns gguk-bro.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defonce https       (nodejs/require "https"))
(defonce querystring (nodejs/require "querystring"))

(defn -main [event context]
  (println "Request received:" (js->clj event :keywordize-keys true))
  (let [api-key "YOUR-BOT-API-KEY"
        text    (str "Hello " (.. event -message -from -first_name) ", "
                     "I'm ClojureScript version Bot. "
                     "You said \"" (.. event -message -text) "\".")
        data    (->> {:chat_id             (.. event -message -from -id)
                      :reply_to_message_id (.. event -message -message_id)
                      :text                text}
                     (clj->js)
                     (.stringify querystring))
        header  (clj->js {:hostname "api.telegram.org"
                          :port     443
                          :path     (str "/bot" api-key "/sendMessage")
                          :method   "POST"
                          :headers  (clj->js {:Content-Type   "application/x-www-form-urlencoded"
                                              :Content-Length (.-length data)})})
        request (. https (request header (fn [res]
                                           (. res (setEncoding "utf8"))
                                           (. res (on "data" #(println "Response:" %)))
                                           (. res (on "end"  #(. context (succeed)))))))]
    (. request (write data))
    (. request (end)))
  (println "Done."))

;; to fake CLJS compiler
(defn none [& _] nil)
(set! *main-cli-fn* none)
