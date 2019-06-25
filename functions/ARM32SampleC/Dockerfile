FROM resin/rpi-raspbian

RUN [ "cross-build-start" ]

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git binutils build-essential ca-certificates zip unzip cmake zlib1g-dev libssl-dev curl libcurl4-openssl-dev

RUN git clone https://github.com/aws/aws-sdk-cpp.git
RUN cd aws-sdk-cpp && \
    cmake -j8 . && \
    make -j8 install

RUN git clone https://github.com/aws/aws-greengrass-core-sdk-c.git
RUN cd aws-greengrass-core-sdk-c && \
    mkdir build && cd build && \
    cmake .. && \
    cmake -j8 --build . && \
    make -j8 install

RUN cd aws-greengrass-core-sdk-c/aws-greengrass-core-sdk-c-example && \
    cmake .

RUN [ "cross-build-end" ]
