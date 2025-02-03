(ns lein-modules-new-profiles.plugin
  (:use [lein-modules-new-profiles.versionization :only (versionize)]
        [lein-modules-new-profiles.inheritance    :only (inherit)]
        [lein-modules-new-profiles.common         :only (config)]))

(defn middleware
  "Implicit Leiningen middleware, guarding recursive
  middleware calls with a metadata flag.
  See https://github.com/technomancy/leiningen/issues/1151"
  [project]
  (if (-> project meta ::middleware-applied)
    project
    (-> project
      (vary-meta assoc ::middleware-applied true)
      inherit
      versionize
      (vary-meta dissoc ::middleware-applied))))
