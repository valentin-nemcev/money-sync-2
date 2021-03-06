#!/bin/bash
if [[ "$DOCKER_UID" && "$DOCKER_UID" != "0" ]]; then
  groupadd --non-unique --gid $DOCKER_GID appgroup;
  useradd -K MAIL_DIR=/dev/null --uid $DOCKER_UID --gid $DOCKER_GID --home-dir /root --no-create-home appuser &> /dev/null;
fi;

exec sudo -u appuser "$@";
