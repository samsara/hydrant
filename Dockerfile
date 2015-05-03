FROM samsara/base-image-jdk8:u1410-j8u40

MAINTAINER Samsara's team (https://github.com/samsara/hydrant)

ADD ./target/hydrant          /opt/hydrant/
ADD ./config/hydrant.config.tmpl        /opt/hydrant/config/hydrant.config.tmpl
ADD ./docker/configure-and-start.sh /configure-and-start.sh

ADD ./docker/hydrant-supervisor.conf /etc/supervisor/conf.d/

# Available ports
# 15000 - supervisor admin interface
EXPOSE 15000

# hydrant logs
VOLUME ["/logs"]

CMD /configure-and-start.sh
