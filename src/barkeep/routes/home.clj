(ns barkeep.routes.home
  (:use compojure.core)
  (:require [barkeep.views.layout :as layout]
            [barkeep.generator :refer [make-barcode-image]]
            [barkeep.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn content-type-for-image [format]
  (case format
    "gif" "image/gif"
    "png" "image/png"
    "jpg" "image/jpeg"
    "application/binary"))

(defn barcode [value format options]
  (let [options (into {:type "c128c" :height "200" :width "400"} options)
        height  (Integer. (:height options))
        width   (Integer. (:width options))]
  {:status  200
   :headers {"Content-Type"  (content-type-for-image format)
             "Cache-Control" "public, max-age=2592000"}
   :body    (java.io.ByteArrayInputStream.
             (make-barcode-image value
                                 :imageformat format
                                 :height height
                                 :width width
                                 :type (:type options)))
   }))

(defroutes home-routes
  (GET "/" [] (home-page))
  (HEAD "/" [] "")
  (GET "/barcode/:value.:format" [value format & options]
       (barcode value format options))
  (GET "/about" [] (about-page)))
