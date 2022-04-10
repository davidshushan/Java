package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TcpServer {
    /*
    1. tap www.ynet.co.il in chrome's address bar
    2. click enter

   chrome to send an HTTP GET request in application layer
   1. check if there exists an ip address for www.ynet.co.il
   check in cache: first chrome, OS, default gateway (router)
   2. if the ip address is not in cache: chrome needs to send
   DNS request (in application layer), A-RECORD to a dns server
   3. chrome needs to send a DNS request - it is done over UDP (or TCP)
   4. chrome asks OS to open a UDP socket
   5. after socket is created, only then chrome can send a DNS request in application
   layer
   6. IP is returned from DNS server - a response in application layer
   7. in order to send an HTTP GET request, chrome asks OS to create a TCP socket
   8. after TCP socket tcp is created, chrome can finally send an HTTP Request

  transport layer - multiplexing. send different kinds of data over the communication

  Socket - abstraction for 2-way pipeline of data (of certain kind)
  the kind is determined by the socket number

     */

    private final int port; // initialize in constructor
    private volatile boolean stopServer; // volatile - stopServer variable is saved in RAM memory
    private ThreadPoolExecutor threadPool; // handle each client in a separate thread
    private IHandler requestHandler; // what is the type of clients' tasks

    public TcpServer(int port){
        this.port = port;
        this.threadPool = null;
        stopServer = false;
    }

    public void supportClients(IHandler handler) {
        this.requestHandler = handler;
        /*
         A server can do many things. Dealing with listening to clients and initial
         support is done in a separate thread
         */
        Runnable mainServerLogic = () -> {
            this.threadPool = new ThreadPoolExecutor(3,5,
                    10, TimeUnit.SECONDS, new LinkedBlockingQueue());
            try {
                ServerSocket serverSocket = new ServerSocket(this.port,50); // bind
                while(!stopServer){
                    Socket serverClientConnection = serverSocket.accept();
                    Runnable clientHandling = ()->{
                        System.out.println("Server: Handling a client...");
                        try {
                            requestHandler.handle(serverClientConnection.getInputStream(),
                                    serverClientConnection.getOutputStream());
                        } catch (IOException | ClassNotFoundException ioException) {
                            ioException.printStackTrace();
                        }
                        try {
                            serverClientConnection.getInputStream().close();
//                            serverClientConnection.getOutputStream().close(); //THROW ERROR!
                            serverClientConnection.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    };
                    threadPool.execute(clientHandling);
                }
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        new Thread(mainServerLogic).start();
    }

    public void stop(){
        if(!stopServer){
            stopServer = true;
            if(threadPool!=null)
                threadPool.shutdown();
        }

    }

    public static void main(String[] args) {
        TcpServer webServer = new TcpServer(8020);
        webServer.supportClients(new MatrixIHandler());

    }
}
