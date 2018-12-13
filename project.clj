(defproject weathergov-hourly-forecast "0.0.3"
            :description "Fetch hourly weather data grid from weather.gov"
            :url ""
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [clj-tagsoup "0.3.0" :exclusions  [org.clojure/clojure]]
                           [hiccup "1.0.5"]
                           [xalan "2.7.2"]
                           [com.github.kyleburton/clj-xpath "1.4.5"]
                           ]
            :repositories  [["releases"  {:url "https://clojars.org/repo"
                                          :creds :gpg}]]

            )
