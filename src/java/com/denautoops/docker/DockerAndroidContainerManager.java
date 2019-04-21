package com.denautoops.docker;

import com.denautoops.docker.config.IDockerAndroidContainerConfig;
import com.denautoops.docker.models.DockerContainer;
import com.denautoops.docker.models.DockerImage;
import com.denautoops.docker.utils.DockerContainerUtil;
import com.denautoops.docker.utils.DockerImageUtil;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import java.io.File;
import java.util.Objects;

public class DockerAndroidContainerManager extends DockerManager {

    private IDockerAndroidContainerConfig iDockerAndroidContainerConfig;
    private DockerContainer dockerContainer;
    private DockerImage dockerImage;

    public DockerAndroidContainerManager(IDockerAndroidContainerConfig dockerAndroidContainerConfig){
        iDockerAndroidContainerConfig = dockerAndroidContainerConfig;
    }

    public boolean startup(){
        dockerImage = buildImage(iDockerAndroidContainerConfig);
        dockerContainer = createContainer(dockerImage, iDockerAndroidContainerConfig);
        return DockerContainerUtil.startDockerContainer(getDockerClient(), dockerContainer);
    }

    private DockerContainer createContainer(DockerImage dockerImage, IDockerAndroidContainerConfig iDockerAndroidContainerConfig){
        CreateContainerResponse containerResponse = getDockerClient().createContainerCmd(dockerImage.getId())
                .withUser(iDockerAndroidContainerConfig.getContainerUserId())
                .withTty(true)
                .withPrivileged(true)
                .withPortBindings(getPortBinding(iDockerAndroidContainerConfig.getHostVNCPort(), iDockerAndroidContainerConfig.getContainerVNCPort()))
                .withAttachStderr(false)
                .withAttachStdin(false)
                .withAttachStdout(false)
                .withCmd(DockerConstant.BASH, iDockerAndroidContainerConfig.getContainerPrepareEnvPath(), iDockerAndroidContainerConfig.getContainerAppiumPort())
                .exec();

        InspectContainerResponse inspectContainer = getDockerClient().inspectContainerCmd(containerResponse.getId()).exec();
        return new DockerContainer(containerResponse.getId(), inspectContainer.getName(),
                inspectContainer.getNetworkSettings());
    }

    private DockerImage buildImage(IDockerAndroidContainerConfig iDockerAndroidContainerConfig){
        String imageId = getDockerClient().buildImageCmd()
                .withDockerfile(new File(iDockerAndroidContainerConfig.getPathToDockerfile()+DockerConstant.DOCKERFILE))
                .withBuildArg(DockerConstant.HOST_APK_PATH, "/"+iDockerAndroidContainerConfig.getHostApkPath())
                .withBuildArg(DockerConstant.CONTAINER_APK_PATH, iDockerAndroidContainerConfig.getContainerApkPath())
                .withTag(iDockerAndroidContainerConfig.getImageName())
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        return new DockerImage(imageId, iDockerAndroidContainerConfig.getImageName());
    }

    public boolean cleanup(){
        DockerContainerUtil.stopDockerContainer(getDockerClient(), getDockerContainer());
        return DockerContainerUtil.removeDockerContainer(getDockerClient(), getDockerContainer()) &&
                DockerImageUtil.removeImage(getDockerClient(), getDockerImage());
    }

    public boolean fullCleanup(){
        return DockerContainerUtil.cleanDockerContainers(getDockerClient()) && DockerImageUtil.removeImageByName(getDockerClient(), iDockerAndroidContainerConfig.getImageName());
    }

    public String getAppiumUrl(){
        String containerIp = null;
        for (Container container : getDockerClient().listContainersCmd().exec()) {
            if (container.getId().equals(getDockerContainer().getId())){
                containerIp = Objects.requireNonNull(container.getNetworkSettings()).getNetworks().get(DockerConstant.BRIDGE).getIpAddress();
            }
        }
        return String.format(DockerConstant.APPIUM_URL_PATTERN, containerIp, iDockerAndroidContainerConfig.getContainerAppiumPort());
    }

    public DockerContainer getDockerContainer() {
        return dockerContainer;
    }

    public DockerImage getDockerImage() {
        return dockerImage;
    }

    public String execCmdInContainer(String... cmd){
        return execCmd(getDockerContainer(), cmd);
    }
}
