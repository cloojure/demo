# works, but slow
# FROM clojure:openjdk-11-tools-deps   

# Has a /root/.m2 pre-loaded with common Clojure libs
# FROM clojure-full
FROM zenika/alpine-chrome

SHELL ["/bin/bash", "-c"] 
RUN echo ~; ls -al ~
RUN mkdir -p /usr/src/app
WORKDIR      /usr/src/app
# always prefer `COPY` to `ADD`
COPY . .   

#-----------------------------------------------------------------------------
# RUN clojure -Stree  # force download of all deps.edn libs
#-----------------------------------------------------------------------------
# don't change above this line or will force re-download of all deps

# RUN clojure --eval '(println "hello world")'

# can stick in the middle below & cause the whole `docker run ...` command to fail
#   && false \
CMD clojure --eval '(println "Hello Clojure:  "  (clojure-version))'  \
    && echo "pwd = $(pwd)"  \
    && date

