package com.denautoops.docker.utils;

import com.denautoops.docker.models.DockerContainer;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Container;

import java.util.List;

public class DockerContainerUtil {

    public static boolean startDockerContainer(DockerClient dockerClient, DockerContainer dockerContainer){
        try {
            dockerClient.startContainerCmd(dockerContainer.getId()).exec();
        } catch (DockerException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean stopDockerContainer(DockerClient dockerClient, DockerContainer dockerContainer){
        try {
            if (dockerContainer != null){
                dockerClient.stopContainerCmd(dockerContainer.getId()).exec();
            }
        } catch (DockerException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean removeDockerContainer(DockerClient dockerClient, DockerContainer dockerContainer){
        try {
            if (dockerContainer != null){
                dockerClient.removeContainerCmd(dockerContainer.getId()).exec();
            }
        } catch (DockerException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean cleanDockerContainers(DockerClient dockerClient){
        for (Container container : getContainers(dockerClient)) {
            DockerContainer dockerContainer = new DockerContainer(container.getId(), dockerClient.inspectContainerCmd(container.getId()).exec().getName(),
                    dockerClient.inspectContainerCmd(container.getId()).exec().getNetworkSettings());
            stopDockerContainer(dockerClient, dockerContainer);
            if (removeDockerContainer(dockerClient, dockerContainer)){
                return false;
            }

        }
        return true;
    }

    private static List<Container> getContainers(DockerClient dockerClient){
        return dockerClient.listContainersCmd().exec();
    }

}
