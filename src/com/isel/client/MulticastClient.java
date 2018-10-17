package com.isel.client;

import com.isel.TerminalInput;
import java.io.IOException;
import java.net.*;

public final class MulticastClient implements IClient{

    private String serverIp;
    private int serverPort;
    private boolean running;

    //used to receive the messages from the board
    private Thread listenerThread;
    private String myIp;

    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;
    private DatagramSocket sendingSocket;
    private String userName;

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

            //lets reuse the same socket to send messages.
            this.sendingSocket = new DatagramSocket();
            this.running = true;
            this.myIp =  InetAddress.getLocalHost().getHostAddress();
            this.userName = TerminalInput.getTerminalInputInstance().ReadUser();


            System.out.println("You are connected to the chat room at " + serverIp + ":" + serverPort);
            System.out.println("Your IP is: " + this.myIp);
            System.out.println("Write your message and press enter");
            }
        catch (Exception excp){
            //If this fails the purpose of the program fails, its a critical error and migth be related with ports already in use.
            throw excp;
        }

    }

    @Override
    public void Start() {

        listenerThread = new Thread(this::Listener);
        listenerThread.start();
        //we need to create a thread to read from the socket and print in the command line
        do {

            String message = TerminalInput.getTerminalInputInstance().ReadMessage();
            String completeMessage = this.userName + ":" + message;
            SendMessage(completeMessage);

        }while (running);
    }

    @Override
    public void Stop() {

        if (this.multicastSocket == null)
            return;

        //stops the listener thread.
        if ( this.listenerThread != null && this.listenerThread.isAlive()){
            listenerThread.stop();
        }

        System.out.println("You are now disconnecting from the server");
        try {

            sendingSocket.close();
            this.multicastSocket.close();
            this.multicastSocket.leaveGroup(this.multicastGroup);
        }
        catch (Exception excp) {
            excp.printStackTrace();
        }
    }


    //private methods


    private void Listener(){

        try {

            do {
                byte[] rcvdata = new byte[512];
                DatagramPacket udpPacket = new DatagramPacket(rcvdata, rcvdata.length);

                this.multicastSocket.receive(udpPacket);
                String pktIP = udpPacket.getAddress().toString();
                if(pktIP.equals(this.myIp) == true){
                    continue;
                }
                String message = new String(udpPacket.getData()).trim();
                System.out.println(message);

            } while (running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }


    private  void SendMessage(String message){

        try {
            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, this.multicastGroup, serverPort);

            this.sendingSocket.send(packet);

        }catch (Exception excp){
            excp.printStackTrace();
        }
    }


}


