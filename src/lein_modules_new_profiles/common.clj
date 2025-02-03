(ns lein-modules-new-profiles.common
  (:require [leiningen.core.project :as prj]
            [leiningen.core.main :refer (version-satisfies? leiningen-version)]
            [clojure.java.io :as io]
            [lein-modules-new-profiles.compression :refer (compressed-profiles)]))

(def read-project (if (version-satisfies? (leiningen-version) "2.5")
                    (load-string "#(prj/init-profiles (prj/project-with-profiles (prj/read-raw %)) [:default])")
                    prj/read))

(defn with-profiles
  "Apply profiles to project"
  [project profiles]
  (when project
    (let [profiles (filter (set profiles) (-> project meta :profiles keys))]
      (prj/set-profiles project profiles))))

(defn parent
  "Return the project's parent project"
  ([project]
     (parent project (compressed-profiles project)))
  ([project profiles]
     (let [p (get-in project [:modules :parent] ::none)]
       (cond
         (map? p) p                        ; handy for testing
         (not p) nil                       ; don't search for parent
         :else (as-> (if (= p ::none) nil p) $
                 (or $ (-> project :parent prj/dependency-map :relative-path) "..")
                 (.getCanonicalFile (io/file (:root project) $))
                 (if (.isDirectory $) $ (.getParentFile $))
                 (io/file $ "project.clj")
                 (when (.exists $) (read-project (str $)))
                 (when $ (with-profiles $ profiles)))))))

(defn config
  "Traverse all parents to accumulate a list of :modules config,
  ordered by least to most immediate ancestors"
  [project]
  (loop [p project, acc '()]
    (if (nil? p)
      (remove nil? acc)
      (recur (parent p) (conj acc (-> p :modules))))))
