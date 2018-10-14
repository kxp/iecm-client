package com.isel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalInput {

    private static TerminalInput terminalInputInstance;
    //private static object locker;

    private InputStreamReader inputReader;
    private BufferedReader bufferedReader;


    public static TerminalInput getTerminalInputInstance() {

        if(terminalInputInstance != null)
            return terminalInputInstance;

        //lock the thing

        terminalInputInstance = new TerminalInput();

        return terminalInputInstance;
    }


    private TerminalInput() {
        this.inputReader = new InputStreamReader(System.in);
        this.bufferedReader = new BufferedReader(inputReader);
    }

    public String ReadFromTerminal(){

        System.out.println("Insert the sentence:");
        String line = "";
        try {

            line = bufferedReader.readLine();
        }
        catch (Exception excp){
            excp.printStackTrace();
        }

        return line;
    }

    public String ReadUser(){

        System.out.println("Insert your username:");
        String line = "";
        try {

            line = bufferedReader.readLine();
        }
        catch (Exception excp){
            excp.printStackTrace();
        }

        return line;
    }

    public String ReadMessage(){

        System.out.print("Message:");
        String line = "";
        try {

            line = bufferedReader.readLine();
        }
        catch (Exception excp){
            excp.printStackTrace();
        }

        return line;
    }

}
