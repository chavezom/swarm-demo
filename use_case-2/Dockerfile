FROM vault:0.7.2

ADD vault-config /config
COPY scripts/* /usr/local/bin/


ENTRYPOINT ["wait-for.sh"]
