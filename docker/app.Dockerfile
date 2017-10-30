FROM clojure:boot-2.7.2-alpine
RUN apk add --no-cache bash shadow sudo

WORKDIR /usr/src/app

COPY . .

RUN boot pom

RUN chmod -R a+rwX /root

ENTRYPOINT ["docker/permissions.sh"]

CMD ["boot", "-h"]
