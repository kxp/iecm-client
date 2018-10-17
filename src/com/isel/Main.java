package com.isel;

import com.isel.client.ClientFactory;
import com.isel.client.EClientType;
import com.isel.client.IClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        System.out.println("Usage: appname type ip");
		System.out.println("Type: udp, tcp, multicast");   

		InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        EClientType clientType = EClientType.NOTSPECIFIED;

        if (args.length >= 1) {
            try {
                clientType = EClientType.valueOf(args[0].toUpperCase());
            } catch (Exception excp) {
                excp.printStackTrace();
                return;
            }
        } else {
            System.out.println("Select the client type by choosing one of the options: tcp, udp or multicast.");

            try {
                String line = br.readLine();
                clientType = EClientType.valueOf(line.toUpperCase());
            } catch (Exception excp) {
                excp.printStackTrace();
                return;
            }
        }


        String serverIp ="";
        if(clientType != EClientType.MULTICAST){

            if(args.length == 2) {
                serverIp = args[1];
            }else {
                serverIp = TerminalInput.getTerminalInputInstance().ReadServerIP();
            }
        }

        //Instanciates the client with the specified ip
        IClient client = ClientFactory.CreateServer(clientType, serverIp);
        if (client == null){
            return;
        }

        try {

            client.Initialize();
            client.Start();
        }
        catch (Exception excp){
            excp.printStackTrace();
            return;
        }
    }
}
