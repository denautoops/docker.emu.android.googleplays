# docker.emu.android.googleplays
Java implementation for work with docker containers with android emulator, which system based by 'Google Plays'.

# container contains

  - Android x86 emulator with API v26, v28 (the following versions will be distributed in the future: v24 - v28);
  - Appium;
  - VNC;
  - Other linux sowtware... (later will be cleaned to reduce the size of the container).
  
# run
  
  Host system:
    Works only with unix systems. Docker should be installed. KVM is enabled (necessary).
  
  Configure:
    Add APK host path to container.apk.path var in docker_android_container.properties file.
    
  Add source code to project and for startup docker container use next code:

```java
DockerAndroidContainerManager dockerAndroidContainerManager = newockerAndroidContainerManager(T extends IDockerAndroidContainerConfig);
//change T to IDockerAndroidContainerConfig implementation

dockerAndroidContainerManager.startup();
```
  For UI vieweing use VNC viewer. Connect to container VNC server:
    localhost:5900
     
    
  
  
