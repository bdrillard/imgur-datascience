(ns imgur-datascience.repost
  (:require [clojure.java.io :as io])
  (:import (org.apache.commons.io IOUtils)
           (org.apache.commons.codec.digest DigestUtils)))

(defn read-image [path]
  (IOUtils/toByteArray (io/input-stream path)))

(defn compute-MD5-hash [data]
  (DigestUtils/md5Hex data))

(defn img->hash [path]
  (->
    path
    read-image
    compute-MD5-hash))

(defn equal? [path1 path2]
  (let [h1 (img->hash path1)
        h2 (img->hash path2)]
    (if (= h1 h2)
      true
      false)))
