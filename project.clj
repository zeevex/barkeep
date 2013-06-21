(defproject barkeep "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.6.2"]
                 [compojure "1.1.5"]
                 [ring-server "0.2.8"]
                 [clabango "0.5"]
                 [com.taoensso/timbre "2.1.2"]
                 [com.postspectacular/rotor "0.1.0"]
                 [com.taoensso/tower "1.7.1"]
                 [markdown-clj "0.9.26"]
                 [com.google.zxing/core "2.2"]
                 [com.google.zxing/javase "2.2"]]

  :plugins [[lein-ring "0.8.5"]
            [lein-beanstalk "0.2.7"]]

  :ring {:handler barkeep.handler/war-handler
         :init    barkeep.handler/init
         :destroy barkeep.handler/destroy}

  :profiles
      {:production {:ring {:open-browser? false
                           :stacktraces?  false
                           :auto-reload?  false}}
       :dev {:dependencies [[ring-mock "0.1.5"]
                            [ring/ring-devel "1.1.8"]]}}

  :aws {:beanstalk {:environments [{:name "development"}
                                   {:name "staging"}
                                   {:name "production"}]}}
      
  :min-lein-version "2.0.0")
