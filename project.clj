(defproject hydrant "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [twitter-streaming-client/twitter-streaming-client "0.3.2" :exclusions [[ch.qos.logback/logback-classic]]]]

  :main hydrant.bin

  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-bin "0.3.5"]]}}

  :bin {:name "hydrant" :bootclasspath :false})
