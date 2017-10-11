FROM postgres:10-alpine

RUN apk add --no-cache bash

COPY postgres-healthcheck.sh /usr/local/bin/

HEALTHCHECK CMD ["postgres-healthcheck.sh"]

COPY postgresql.conf /etc/postgresql.conf

CMD ["postgres", "-c", "config_file=/etc/postgresql.conf"]
