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

(defn barcode []
  {:status  200
   :headers {"Content-Type" "image/png"}
   :body    (java.io.ByteArrayInputStream. (make-barcode-image "1234567891")) })

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/barcode" [] (barcode))
  (GET "/about" [] (about-page)))
