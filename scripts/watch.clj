(require '[cljs.build.api :as b])

(b/watch "src"
         {:main       'gguk-bro.core
          :output-to  "out/main.js"
          :output-dir "out"})
