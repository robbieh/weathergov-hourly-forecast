(defproject weathergov-hourly-forecast "0.0.5"
            :description "Fetch hourly weather data grid from weather.gov"
            :url ""
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.9.0"]
                           [clj-tagsoup "0.3.0" :exclusions  [org.clojure/clojure]]
                           [hiccup "2.0.0-alpha2"]
                           [com.github.kyleburton/clj-xpath "1.4.11"]]
            :repositories  [["releases"  {:url "https://clojars.org/repo"
                                          :creds :gpg}]])
