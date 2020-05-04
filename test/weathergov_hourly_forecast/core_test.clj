(ns weathergov-hourly-forecast.core-test
  (:use clojure.test
        weathergov-hourly-forecast.core))

(deftest basic-test
         (testing "Basic test of ability to fetch data"
                  (let [forecast (get-forecast-table 33.82 -84.36)]
                    (is (=
                        (keys forecast)
                        [:TemperatureF :Gust :DewpointF :SkyCover% :SurfaceWindmph :RelativeHumidity% :PrecipitationPotential% :Thunder :Date :HeatIndexF :Rain :WindDir])))))

