package com.isel.client;

import com.isel.TerminalInput;

import java.io.*;
import java.net.Socket;

public final class TcpClient implements IClient{

    private String serverIp;
    private int serverPort;

    private Socket clientTcpSocket;
    private DataInputStream streamFromServer;
    private DataOutputStream streamToServer;


    public TcpClient(String remoteIp, int remotePort){
        this.serverIp = remoteIp;
        this.serverPort = remotePort;
    }

    @Override
    public void Initialize() throws IOException{

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

        String converted = "";
        TerminalInput terminalInput = TerminalInput.getTerminalInputInstance();
        try
        {
            do
            {
                String input = terminalInput.ReadFromTerminal();
                streamToServer.writeUTF(input);
                streamToServer.flush();

                converted = streamFromServer.readUTF();
                System.out.print(converted);

            }while (converted !="END" || converted != "STOP")
        }
        catch(Exception excp) {
            excp.printStackTrace();
        }

        //After receiving the command we exit
        Stop();
    }

    @Override
    public void Stop() {
        try {
            Close();
        }
        catch (Exception excp){
            //at this point we dont care
            excp.printStackTrace();
        }
    }


    private void Close() throws IOException {
        try {
            if (clientTcpSocket != null){
                clientTcpSocket.close();
                clientTcpSocket = null;
            }

            if (streamToServer != null) {
                streamToServer.close();
                streamToServer= null;
            }

            if (streamFromServer != null) {
                streamFromServer.close();
                streamFromServer = null;
            }
        }
        catch (Exception excp){
            //We dont care at this point, the program is closing
            throw excp;
        }

    }

}
