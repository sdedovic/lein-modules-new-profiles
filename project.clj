(defproject com.dedovic/lein-modules-new-profiles "0.3.15-SNAPSHOT"
  :description "Similar to Maven multi-module projects, but less sucky"
  :url "https://github.com/sdedovic/lein-modules-new-profiles"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true
  :aliases {"all" ["do" "clean," "test," "install"]}
  :signing {:gpg-key "92439EF5"}
  :plugins [[lein-file-replace "0.1.0"]]
  :deploy-repositories  [["releases" {:sign-releases false
                                      :url           "https://clojars.org/repo"
                                      :username      :env/clojars_user
                                      :password      :env/clojars_token}]]
  :release-tasks
  [["vcs" "assert-committed"]
   ["change" "version" "leiningen.release/bump-version" "release"]

   ["file-replace" "README.md" "lein-modules \"" "\"]" "version"]

   ["vcs" "commit"]
   ["vcs" "tag" "--no-sign"]
   ["deploy"]
   ["change" "version" "leiningen.release/bump-version"]
   ["vcs" "commit"]
   ["vcs" "push"]])
