package com.denautoops.docker.models;

public class DockerImage {
    private String id;
    private String name;

    public DockerImage(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
