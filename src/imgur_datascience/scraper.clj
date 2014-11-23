(ns imgur-datascience.scraper
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.walk :as walk]))

;; --------------------------------------------------------------------------------------
;; Get corpus: Identify initial body of images.
;; --------------------------------------------------------------------------------------

(def user-sub-url "https://api.imgur.com/3/gallery/user/time/")

(defn request-gallery-page
  "Accepts an Imgur-API structured URL representing a GET request for a gallery
  page of User Submitted, ordered by newest submission, with the correct
  authorization headers included in the request.
  Returns a string representing the JSON response of images in that gallery page."
  [url]
  (client/get url
              {:headers {:Authorization "Client-ID f613570b716a3c8"}}))

(defn get-page
  "Accepts a page number.
  Returns a string representing the JSON response for images in that gallery page."
  [page-num]
  (request-gallery-page (str user-sub-url page-num))) 

(defn resp->json-data 
  "Transforms the string response of an Imgur-API gallery GET call to a correctly
  structured dictionary representation of the JSON"
  [resp]
  (-> resp
      :body
      json/read-str
      walk/keywordize-keys
      :data))

(defn get-page-range 
  "Gets the response data for a range of User Submitted gallery pages indexed from 0."
  [ceiling]
  (mapcat #(resp->json-data (get-page %)) (range 0 ceiling)))

(def corpus (get-page-range 20))
