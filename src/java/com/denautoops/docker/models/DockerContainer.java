package com.denautoops.docker.models;

import com.github.dockerjava.api.model.NetworkSettings;

public class DockerContainer {
    private String id;
    private String name;
    private NetworkSettings networkSettings;

    public DockerContainer(String id, String name, NetworkSettings networkSettings) {
        this.id = id;
        this.name = name;
        this.networkSettings = networkSettings;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }
}
