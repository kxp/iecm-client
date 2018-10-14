package com.isel.client;

import com.isel.TerminalInput;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public final class UdpClient implements IClient {

    private String serverIp;
    private int serverPort;

    private DatagramSocket clientUDPSocket;
    private InetAddress destInetAddress;

    public UdpClient(String remoteIp, int remotePort){
        this.serverIp = remoteIp;
        this.serverPort = remotePort;
    }

    @Override
    public void Initialize() throws IOException {

        try {
            this.clientUDPSocket = new DatagramSocket();
            this.destInetAddress = InetAddress.getByName(this.serverIp);
        }
        catch (Exception excp){
            //If this fails the purpose of the program fails, its a critical error and migth be related with ports already in use.
            throw excp;
        }

    }

    @Override
    public void Start() {

        TerminalInput terminalInput = TerminalInput.getTerminalInputInstance();

        String converted = "";
        try {
            do {
                String input = terminalInput.ReadFromTerminal();
                byte[] sendData = input.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.destInetAddress, this.serverPort);
                this.clientUDPSocket.send(sendPacket);

                byte[] receiveData = new byte[512];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                this.clientUDPSocket.receive(receivePacket);

                converted = new String(receivePacket.getData());
                converted = converted.trim();
                System.out.println(converted);
            } while (converted.equals("END") == false && converted.equals("STOP") == false);

        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

    @Override
    public void Stop() {
        this.clientUDPSocket.close();
    }
}

