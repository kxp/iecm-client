package com.isel.client;

public final class MulticastClient implements IClient{

    private String serverIp;
    private int serverPort;

    public MulticastClient(String remoteIp, int remotePort){
        this.serverIp = remoteIp;
        this.serverPort = remotePort;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Stop() {

    }
}
