(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[perun "0.4.0-SNAPSHOT"]
                  [hiccup "1.0.5"]
                  [pandeiro/boot-http "0.6.3-SNAPSHOT"]
                  [jeluard/boot-notify "0.1.2" :scope "test"]])

(require '[io.perun :refer :all]
         '[io.perun.example.index :as index-view]
         '[io.perun.example.post :as post-view]
         '[pandeiro.boot-http :refer [serve]]
         '[jeluard.boot-notify :refer [notify]])

(deftask build
  "Build test blog. This task is just for testing different plugins together."
  []
  (comp
        (global-metadata)
        (images-dimensions)
        (images-resize)
        (base)
        (markdown)
        (draft)
        (print-meta)
        (slug)
        (permalink)
        (canonical-url)
        (build-date)
        (render :renderer 'io.perun.example.post/render)
        (collection :renderer 'io.perun.example.index/render :page "index.html" :filterer identity)
        (sitemap)
        (notify)))

(deftask dev
  []
  (comp (watch)
        (build)
        (serve :resource-root "public")))