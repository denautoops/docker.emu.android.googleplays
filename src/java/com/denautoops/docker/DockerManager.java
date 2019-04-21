package com.denautoops.docker;

import com.denautoops.docker.models.DockerContainer;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DockerManager {

    private static final int EXEC_CMD_TIMEOUT = 1;

    static DockerClient getDockerClient(){
        return DockerClientBuilder.getInstance().build();
    }

    static List<PortBinding> getPortBinding(String hostPort, String containerPort){
        List<PortBinding> ports = new ArrayList<>();
        ports.add(PortBinding.parse(String.format(DockerConstant.PORT_BINDING_PATTERN, hostPort, containerPort)));
        return ports;
    }

    static String execCmd(DockerContainer container, String... cmd){
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        ExecCreateCmdResponse execCreateCmdResponse = getDockerClient().execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(cmd)
                .exec();
        try {
            getDockerClient().execStartCmd(execCreateCmdResponse.getId()).exec(
                    new ExecStartResultCallback(stdout, stderr)).awaitCompletion(EXEC_CMD_TIMEOUT, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stdout.toString();
    }
}
