#!/bin/bash
if [[ "$DOCKER_UID" && "$DOCKER_UID" != "0" ]]; then
  groupadd --non-unique --gid $DOCKER_GID appgroup;
  useradd -K MAIL_DIR=/dev/null --uid $DOCKER_GID --groups appgroup --home-dir /root --no-create-home appuser;
fi;

exec sudo -u appuser "$@";
