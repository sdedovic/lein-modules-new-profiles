(defproject stepchild "0.1.0-SNAPSHOT"
  :description "stepchild"
  :modules {:parent nil}
  :middleware [lein-modules-new-profiles.plugin/middleware]
  :profiles {:by-child {:modules {:parent ".."}}
             :skip-parent {}
             :version-override {:modules {:versions {:v "3"}}}})
