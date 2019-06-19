FROM resin/rpi-raspbian

RUN [ "cross-build-start" ]

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y python-pip sqlite3 openjdk-8-jdk python git binutils wget iproute2 build-essential ca-certificates libyaml-dev python-dev jq unzip

RUN adduser --system ggc_user \
    && groupadd --system ggc_group

RUN ln -s /usr/bin/java /usr/bin/java8

RUN curl https://raw.githubusercontent.com/tianon/cgroupfs-mount/951c38ee8d802330454bdede20d85ec1c0f8d312/cgroupfs-mount > cgroupfs-mount.sh
RUN chmod +x cgroupfs-mount.sh

RUN git clone https://github.com/tj/n.git
RUN cd n && make install && n 8.10.0 && NODEJS=`which node` && ln -s $NODEJS `dirname $NODEJS`/nodejs8.10

RUN pip install awscli

ADD https://d1onfpft10uf5o.cloudfront.net/greengrass-core/downloads/1.9.1/greengrass-linux-armv7l-1.9.1.tar.gz /
RUN tar xzvf greengrass-linux-armv7l-1.9.1.tar.gz -C /
RUN rm /greengrass-linux-armv7l-1.9.1.tar.gz

ARG GROUP_NAME
ADD foundation/docker/redeploy.sh /
ADD build/oem.GROUP_NAME.tar /greengrass/

RUN [ "cross-build-end" ]

EXPOSE 8883

CMD bash ./redeploy.sh && \
    bash ./cgroupfs-mount.sh && /greengrass/ggc/core/greengrassd start && tail -F /greengrass/ggc/var/log/system/runtime.log
