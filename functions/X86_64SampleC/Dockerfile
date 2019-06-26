FROM timmattison/aws-greengrass-core-x86-native-build-environment

RUN cd aws-greengrass-core-sdk-c/aws-greengrass-core-sdk-c-example && \
    cmake -j8 . && \
    make -j8
