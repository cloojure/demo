FROM clojure-full
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
#-----------------------------------------------------------------------------
#COPY project.clj /usr/src/app
#RUN lein deps
#COPY . /usr/src/app
#
#RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" \
#       demo-standalone.jar
#RUN (pwd; ls -al)
#RUN jar -tvf demo-standalone.jar | grep demo
#
#CMD ["java", "-jar", "demo-standalone.jar" ]

#-----------------------------------------------------------------------------
COPY . .
# RUN clojure -Stree  # force download of all deps.edn libs
#-----------------------------------------------------------------------------
# don't change above this line or will force re-download of all deps

# RUN find / -name .m2 | xargs du    # maven cache is in /root/.m2
# RUN clojure --eval '(println "hello world")'
CMD clojure --eval '(println "hello world")'

#RUN clojure -Sverbose
#COPY . /usr/src/app

