(set-env!
  :dependencies '[[org.clojure/clojure "1.6.0"]
                  [time-to-read "0.1.0"]])

(ns io.perun.draft
  {:boot/export-tasks true}
  (:require [boot.core         :as boot]
            [boot.util         :as u]
            [io.perun.utils :as util]
            [clojure.java.io   :as io]))

(boot/deftask draft
  "Exclude draft posts"
  []
  (let [tmp (boot/temp-dir!)]
    (fn middleware [next-handler]
      (fn handler [fileset]
        (let [posts (util/read-posts "posts.edn")
              updated-posts (remove #(true? (:draft %)) posts)
              posts-file (io/file tmp "posts.edn")
              content (prn-str updated-posts)]
          (util/write-to-file posts-file content)
          (u/info "Remove draft posts. Remaining %s posts\n" (count updated-posts))
          (-> fileset
              (boot/add-resource tmp)
              boot/commit!
              next-handler))))))
