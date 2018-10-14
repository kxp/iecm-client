package com.isel.client;

import com.isel.TerminalInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public final class MulticastClient implements IClient{

    private String serverIp;
    private int serverPort;

    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;

    public MulticastClient(String remoteIp, int remotePort){
        this.serverIp = remoteIp;
        this.serverPort = remotePort;
    }

    @Override
    public void Initialize() throws IOException {

        try {
            this.multicastSocket = new MulticastSocket(this.serverPort);
            this.multicastGroup = InetAddress.getByName(serverIp);
            this.multicastSocket.joinGroup(this.multicastGroup);

            System.out.println("You are connected to the chat room at " + serverIp + ":" + serverPort);
            RegisterUser();
            }
        catch (Exception excp){
            //If this fails the purpose of the program fails, its a critical error and migth be related with ports already in use.
            throw excp;
        }

    }

    @Override
    public void Start() {
        //we need to create a thread to read from the socket and print in the command line
        do {

            String message = TerminalInput.getTerminalInputInstance().ReadMessage();
            SendMessage(message);

        }while (true);

    }

    @Override
    public void Stop() {

        if (this.multicastSocket == null)
            return;

        System.out.println("You are now disconnecting from the server");
        try {
            this.multicastSocket.close();
            this.multicastSocket.leaveGroup(this.multicastGroup);
        }
        catch (Exception excp) {
            excp.printStackTrace();
        }
    }

    private void RegisterUser(){

        String userName = TerminalInput.getTerminalInputInstance().ReadUser();
        SendMessage(userName);

    }

    private  void SendMessage(String message){

        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, this.multicastGroup, serverPort);

            socket.send(packet);
            socket.close();

        }catch (Exception excp){
            excp.printStackTrace();
        }

    }



}


