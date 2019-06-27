# works, but slow
# FROM clojure:openjdk-11-tools-deps   

# Has a /root/.m2 pre-loaded with common Clojure libs
FROM clojure-full

SHELL ["/bin/bash", "-c"] 
RUN echo ~; ls -al ~
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . .

#-----------------------------------------------------------------------------
# RUN clojure -Stree  # force download of all deps.edn libs
#-----------------------------------------------------------------------------
# don't change above this line or will force re-download of all deps

# RUN find / -name .m2 | xargs du    # maven cache is in /root/.m2
# RUN clojure --eval '(println "hello world")'

CMD clojure --eval '(println "Hello Clojure:  "  (clojure-version))'  \
    && echo "pwd = $(pwd)"  \
    && false \
    && date

