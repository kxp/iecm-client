package com.isel.client;

import com.isel.TerminalInput;

import java.io.*;
import java.net.Socket;

public final class TcpClient implements IClient{

    private String serverIp;
    private int serverPort;

    private Socket clientTcpSocket;
    private DataOutputStream streamToServer;
    private BufferedReader dataFromServer;

    public TcpClient(String remoteIp, int remotePort){
        this.serverIp = remoteIp;
        this.serverPort = remotePort;
    }

    @Override
    public void Initialize() throws IOException{

        try {
            clientTcpSocket = new Socket(serverIp, serverPort);
            dataFromServer = new BufferedReader( new InputStreamReader(clientTcpSocket.getInputStream()));
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

                converted = dataFromServer.readLine();
                System.out.println(converted);

            }while (converted.equals("END") == false  && converted.equals("STOP") == false);
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
            System.out.println("The server is stopping");
            Close();
        }
        catch (Exception excp){
            //at this point we dont care
            excp.printStackTrace();
        }
    }


    private void Close() throws IOException {
        try {

            if (streamToServer != null) {
                streamToServer.close();
                streamToServer= null;
            }

            if (dataFromServer != null) {
                dataFromServer.close();
                dataFromServer = null;
            }

            if (clientTcpSocket != null){
                clientTcpSocket.close();
                clientTcpSocket = null;
            }
        }
        catch (Exception excp){
            //We dont care at this point, the program is closing
            throw excp;
        }

    }

}
