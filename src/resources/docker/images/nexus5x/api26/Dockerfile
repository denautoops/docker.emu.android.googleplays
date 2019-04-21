FROM dmartynov17/nexus5x:8.0
ARG hostApkPath
ARG containerApkPath

# Android SDK
ENV ANDROID_HOME /root/Android/Sdk
ENV PATH /root/Android/Sdk/platform-tools:/root/Android/Sdk/tools/bin:/root/Android/Sdk/emulator:/root/Android/Sdk/tools:$PATH
ENV LD_LIBRARY_PATH ${ANDROID_HOME}/emulator/lib64:${ANDROID_HOME}/emulator/lib64/gles_swiftshader
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV ANDROID_AVD_HOME /root/.android/avd/
ENV ANDROID_EMULATOR_HOME /root/.android/

ADD /prepare-env.sh /home/prepare-env.sh
ADD ["$hostApkPath", "$containerApkPath"]

CMD chmod +x /home/prepare-env.sh
