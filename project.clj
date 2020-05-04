(defproject weathergov-hourly-forecast "0.0.6"
            :description "Fetch hourly weather data grid from weather.gov"
            :url ""
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.10.1"]
                           [enlive "1.1.6"] ]
            :repositories  [["releases"  {:url "https://clojars.org/repo"
                                          :creds :gpg}]])
