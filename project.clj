(defproject hydrant "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [twitter-streaming-client/twitter-streaming-client "0.3.2" :exclusions [[ch.qos.logback/logback-classic]]]
                 [itsy "0.1.1" :exclusions [[org.slf4j/slf4j-log4j12]]]
                 [http-kit "2.1.16"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [org.slf4j/slf4j-simple "1.7.12"]]

  :main hydrant.bin

  :resource-paths ["resources/"]

  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-bin "0.3.5"]]}}

  :bin {:name "hydrant" :bootclasspath :false})
