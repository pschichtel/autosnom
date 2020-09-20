FROM mozilla/sbt:latest AS build

RUN mkdir /build

WORKDIR /build

COPY build.sbt /build/
COPY project /build/project/

RUN sbt -no-colors update

ADD . /build/

RUN sbt -no-colors dist \
 && unzip target/universal/autosnom-*.zip

FROM adoptopenjdk/openjdk14:alpine-slim

RUN adduser -S play \
 && mkdir -p /app/configurations \
 && chown -R play /app

USER play

WORKDIR /app

COPY --from=build "/build/autosnom-*/bin" /app/bin/
COPY --from=build "/build/autosnom-*/lib" /app/lib/
COPY --from=build "/build/autosnom-*/conf" /app/conf/

EXPOSE 9000/tcp

VOLUME ["/app/configurations"]

HEALTHCHECK --interval=5m --timeout=3s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:9000/ || exit 1

ENTRYPOINT ["./bin/autosnom"]