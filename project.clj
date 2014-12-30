(defproject weathergov-hourly-forecast "0.0.1"
            :description "Fetch hourly weather data grid from weather.gov"
            :url ""
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.5.1"]
                           [clj-tagsoup "0.3.0"]
                           [hiccup "1.0.5"]
                           [com.github.kyleburton/clj-xpath "1.4.4"]
                           ]
            :repositories  [["releases"  {:url "https://clojars.org/repo"
                                          :creds :gpg}]]

            )
