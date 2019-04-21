package com.denautoops.docker.utils;

import com.denautoops.docker.models.DockerImage;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Image;

import java.util.List;

public class DockerImageUtil {

    public static boolean removeImage(DockerClient dockerClient, DockerImage dockerImage){
        try {
            if (dockerImage != null){
                dockerClient.removeImageCmd(dockerImage.getId());
            }
        } catch (DockerException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean removeImageByName(DockerClient dockerClient, String imageName){
        for (Image image : getImages(dockerClient)) {
            if (image.getRepoTags() != null){
                if (image.getRepoTags()[0].contains(imageName)){
                    try {
                        dockerClient.removeImageCmd(image.getId()).exec();
                    } catch (DockerException e){
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static List<Image> getImages(DockerClient dockerClient){
        return dockerClient.listImagesCmd().exec();
    }

}
