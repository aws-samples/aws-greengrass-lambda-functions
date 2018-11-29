FROM project31/aarch64-alpine-qemu

RUN [ "cross-build-start" ]

RUN adduser -S ggc_user
RUN addgroup -S ggc_group

RUN apk add --update sqlite python git binutils wget iproute2 ca-certificates python-dev jq curl make bash gcc g++ linux-headers

RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
RUN python get-pip.py

RUN curl https://raw.githubusercontent.com/tianon/cgroupfs-mount/951c38ee8d802330454bdede20d85ec1c0f8d312/cgroupfs-mount > cgroupfs-mount.sh
RUN chmod +x cgroupfs-mount.sh

RUN git clone https://github.com/nodejs/node.git && cd node && git checkout tags/v6.10.3 -b v6.10.3
RUN cd node && ./configure && make -j4 install
#RUN cd n && make install && n 6.10.3 && NODEJS=`which node` && ln -s $NODEJS `dirname $NODEJS`/nodejs6.10

RUN pip install awscli

ADD https://d1onfpft10uf5o.cloudfront.net/greengrass-core/downloads/1.6.0/greengrass-linux-aarch64-1.6.0.tar.gz /
RUN tar xzvf greengrass-linux-aarch64-1.6.0.tar.gz -C /
RUN rm /greengrass-linux-aarch64-1.6.0.tar.gz

ARG GROUP_NAME
ADD foundation/docker/redeploy.sh /
ADD build/oem.$GROUP_NAME.tar /greengrass/

RUN [ "cross-build-end" ]

EXPOSE 8883

CMD bash ./redeploy.sh && \
    bash ./cgroupfs-mount.sh && /greengrass/ggc/core/greengrassd start && tail -F /greengrass/ggc/var/log/system/runtime.log
