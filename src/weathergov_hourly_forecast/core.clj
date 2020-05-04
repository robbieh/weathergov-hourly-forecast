(ns weathergov-hourly-forecast.core
  (:require 
    [clojure.java.io :as io]
    [net.cgrand.enlive-html :as html]
    ))

(defn forecast-url [lat lon] (str "https://forecast.weather.gov/MapClick.php?lat=" lat "&lon=" lon "&FcstType=digital"))

;CSS selector from Firefox: body > table:nth-child (6) 
(def table-selector [:body :> [:table (html/nth-of-type 6)]])

(defn scrub-keyword "Removes parenthesis, spaces, and degree symbols from a string and returns a keyword" [s]
  (keyword (clojure.string/replace s #"[°\(\) ]" "")))

(defn extract-row [row]
  (let  [[first-row & rest-rows :as parts] (html/select row [:td])]
      {(-> first-row html/text scrub-keyword) (mapv html/text rest-rows)}
    ) )

;Thanks, MBV!
(defn holdv [blank? coll]
  (second (reduce (fn [[x v] x']
            (let [x (if (blank? x') x x')]
              [x (conj v x)]))
          [nil []] coll)))

(defn datefix "Weather.gov returns a sparse list of dates ['1/1' '' '' '' '1/2'] and this fills in the sparse list"
  [v]
  (let [lastdate (atom "")
        reifydate (fn [a] (if (= a "") @lastdate (reset! lastdate a)))]
    (mapv reifydate v)))


(defn date-time-merge "Merges the :Date and :Hour data into a single :Date"
  [v]
  (let [hrkey (keyword (first (filter #(re-matches #"Hour.*" %) (map name (keys v))))) ;since site returns TZ in the key...
        d (:Date v)
        t (hrkey v)]
    (dissoc (merge v {:Date (mapv str d (repeat "-") t)}) hrkey)
    )
  )


(defn pivot-data "Pivots the weather data such that the :Date data becomes the keys, and the rest of the data becomes entries in that map. Makes accessing the data by date/time easy."
  [v]
  (apply merge (let [ks (remove #(= % :Date) (keys v))
        sz (count (:Date v))
        f  (fn [n] {(nth (:Date v) n) (apply merge (map #(hash-map % (nth (% v) n)) ks))})
        ]
          (map f (range sz))
    )) )

(defn spy [x] (prn x) x)

(defn get-forecast-page-seq
  "Returns the seq of elements from Enlive from the forecast.weather.gov page for the given lat/lon coordinates"
  [lat lon]
  (html/html-resource (java.net.URL. (forecast-url 33 -84 ))))

(defn get-forecast-table 
  "Fetches the hourly forecast data from forecast.weather.gov and returns a hashmap."
  [lat lon]
  (let [pg (get-forecast-page-seq lat lon)
        ftab  (html/select pg table-selector) 
        rows  (->> ftab first :content rest (take 13))  ;the first half of the table
        table (into {} (map extract-row rows))
        table (merge table {:Date (datefix (:Date table))})
        table (date-time-merge table)]
    table))

(comment 
  (get-forecast-table 33.82 -84.36)
  (pivot-data (get-forecast-table 33.82 -84.36))
  )


;(def ftt (tagsoup/parse "http://forecast.weather.gov/MapClick.php?lat=33.82&lon=-84.36&FcstType=digital" )) 
;(def ftx (hic/html ftt))
;(def t (apply (partial merge-with (comp vec concat)) (map handle-wgov-tr ($x "//html/body/table[6]/*" ftx))))
;(def t (merge t {:Date (datefix (:Date t))}))
;(def t (into { } (filter #((comp not empty?) (second %)) t)))
;(def t (date-time-merge t))
;(clean-and-pivot-data t)
;(identity t)
