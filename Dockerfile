FROM gradle:8.10.2-jdk21

RUN apt-get update && apt-get install -qq -y --no-install-recommends

ENV INSTALL_PATH /reactive-flashcards

RUN mkdir $INSTALL_PATH

WORKDIR $INSTALL_PATH

COPY . .