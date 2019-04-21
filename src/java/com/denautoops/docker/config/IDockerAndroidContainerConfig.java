package com.denautoops.docker.config;

public interface IDockerAndroidContainerConfig {
    String getPathToDockerfile();
    String getHostApkPath();
    String getImageName();
    String getHostVNCPort();
    String getContainerVNCPort();
    String getContainerUserId();
    String getContainerPrepareEnvPath();
    String getContainerAppiumPort();
    String getContainerApkPath();
}
