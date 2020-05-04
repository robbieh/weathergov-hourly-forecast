(ns weathergov-hourly-forecast.core
  (:require 
    [clojure.java.io :as io]
    ;[clojure.xml :as xml]
    ;[clojure.zip :as zip]
    ;[clojure.data.zip.xml :as zxml]
    ;[clj-time.core :as clj-time]
    ;[clj-time.local]
    ;[clj-time.format]
    ;[org.httpkit.client :as http]
    ;[cheshire.core :refer :all]
    [pl.danieljanus.tagsoup :as tagsoup]
    [hiccup.core :as hic]
    )
   (:use  [clj-xpath.core :only  [$x $x:tag $x:text $x:text* $x:attrs $x:attrs* $x:node $x:node*]])
  )

;Thanks, MBV!
(defn holdv [blank? coll]
  (second (reduce (fn [[x v] x']
            (let [x (if (blank? x') x x')]
              [x (conj v x)]))
          [nil []] coll)))

(defn scrub-keyword "Removes parenthesis, spaces, and degree symbols from a string and returns a keyword" [s]
  (keyword (clojure.string/replace s #"[°\(\) ]" "")))

(defn handle-wgov-tr 
  "Parses the <tr> rows from weather.gov tabular data page, returning first column as map key, and data as vector in map value.
  Example: {:Rain [\"10\" \"--\" \"--\"]}" 
  [x]
  (let [row ($x:text* "./td" x)]
    {(scrub-keyword (first row)) (vec (rest row))}
    )
  )

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

(defn get-forecast-page [lat lon]
  (tagsoup/parse (str "https://forecast.weather.gov/MapClick.php?lat=" lat "&lon=" lon "&FcstType=digital")))

(defn get-forecast-table 
  "Fetches the hourly forecast data from forecast.weather.gov and returns a hashmap."
  [lat lon]
  (let [ftx (hic/html (get-forecast-page lat lon))
        t (apply (partial merge-with (comp vec concat)) (map handle-wgov-tr ($x "//html/body/table[6]/*" ftx)))
        t (merge t {:Date (datefix (:Date t))})
        t (into { } (filter #((comp not empty?) (second %)) t))
        t (date-time-merge t)
        ]
    t
    ))

(comment 
  (get-forecast-table 33.82 -84.36)
  (def page (get-forecast-page 33.82 -84.36))
  (identity page))


;(def ftt (tagsoup/parse "http://forecast.weather.gov/MapClick.php?lat=33.82&lon=-84.36&FcstType=digital" )) 
;(def ftx (hic/html ftt))
;(def t (apply (partial merge-with (comp vec concat)) (map handle-wgov-tr ($x "//html/body/table[6]/*" ftx))))
;(def t (merge t {:Date (datefix (:Date t))}))
;(def t (into { } (filter #((comp not empty?) (second %)) t)))
;(def t (date-time-merge t))
;(clean-and-pivot-data t)
;(identity t)
