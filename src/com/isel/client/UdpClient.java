package com.isel.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class UdpClient implements IClient {

    private String serverIp;
    private int serverPort;

    private Socket clientTcpSocket;
    private DataInputStream streamFromServer;
    private DataOutputStream streamToServer;

    public UdpClient(String remoteIp, int remotePort){
        this.serverIp = remoteIp;
        this.serverPort = remotePort;
    }

    @Override
    public void Initialize() throws IOException {

        try {
            clientTcpSocket = new Socket(serverIp, serverPort);
            streamFromServer = new DataInputStream(clientTcpSocket.getInputStream());
            streamToServer = new DataOutputStream(clientTcpSocket.getOutputStream());
        }
        catch (Exception excp){
            //If this fails the purpose of the program fails, its a critical error and migth be related with ports already in use.
            throw excp;
        }

    }

    @Override
    public void Start() {

    }

    @Override
    public void Stop() {

    }
}

