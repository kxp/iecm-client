package com.isel.client;

public class ClientFactory {

    //I am gonna store all the ports here just for agregate the port defenitions.
    private static int TcpPort = 11000;
    private static int UdpPort = 11001;
    private static int MulticastPort = 11002;

    private static String MulticastIp = "224.0.0.3";

    public static IClient CreateServer (EClientType connectionType, String serverIp) {

        IClient client = null;

        switch (connectionType) {
            case TCP:
                client = new TcpClient(serverIp, TcpPort);
                break;
            case UDP:
                client = new UdpClient(serverIp, UdpPort);
                break;
            case MULTICAST:
                client = new MulticastClient(MulticastIp,MulticastPort);
                break;
        }

        return client;
    }
}
