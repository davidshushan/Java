package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketHandler implements Runnable {

    public static ArrayList<SocketHandler> clientsArray = new ArrayList<>(); // trough this array we'll be able to broadcast messages to the all requested destinations
    private Socket socket;
    private BufferedReader bufferedReader;          // BufferedReader obj read data in buffered block so until we won't fill the buffer he will not send the message
    private BufferedWriter bufferedWriter;
    private String userName;
    private String password;
    public static HashMap<String,String> userList = new HashMap<>();

    public SocketHandler(Socket socket) {
        try {
            this.socket = socket;
            new OutputStreamWriter(socket.getOutputStream());
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.userName = bufferedReader.readLine();
            this.password = bufferedReader.readLine();
//            clientsArray.add(this);
            System.out.println("new client : " + this.userName);
            System.out.println("new password : " + this.password);
            String result = checkUser(userName, password);
            bufferedWriter.write(result);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            if (!clientsArray.contains(this)){
                clientsArray.add(this);
            }
//                clientsArray.add(this);

            broadCastMessage( this.userName + " has joined the chat!");

        } catch (IOException e) {
            closeStream(socket, bufferedReader, bufferedWriter);

        }
    }

    @Override
    public void run() {
        String message;
//                clientsArray.add(this);
        while (socket.isConnected()) {
            try {

                message = bufferedReader.readLine();
                if (message==null){
                    break;
                }
                if (message.equals("close")){
                    System.out.println("disconnecting user - " + userName);
                    closeStream(socket,bufferedReader,bufferedWriter);
                    System.out.println("user - " + userName + " has been disconnected");

                    break;
                }
                if (message.equals("get clients list")) {
                    String list = getList();
//                    System.out.println(list);
                    bufferedWriter.write(list);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    System.out.println(list + "-----------"+ bufferedWriter.toString());
                    continue;
//                    break;
                }
                if(message.contains("<private>")){

                    for (SocketHandler client: clientsArray){

                        if(message.contains("<private><"+client.userName+">")){
                            message = message.replaceAll("<private><"+client.userName+">","");
                            client.bufferedWriter.write(message);
                            client.bufferedWriter.newLine();
                            client.bufferedWriter.flush();
                            break;
                        }
                    }
                }
//                }
                else{
                    broadCastMessage(message);
                }

            } catch (IOException e) {
                closeStream(socket, bufferedReader, bufferedWriter);
                e.printStackTrace();
                break;
            }
        }
    }
    public String checkUser(String userName, String password){
        if (userList.containsKey(userName)){
            if (userList.get(userName).equals(password)){
                return "logging in...";
            }
            return "password is WRONG!!";
        }
        else {
            userList.put(userName,password);
            return "welcome " + userName + " - you are a new user!";
        }

    }
    public String getList() {
        String users = "ONLINE USERS: ";
        for (SocketHandler client : clientsArray) {
            if (!client.userName.equals(userName)) {
                users += (client.userName + ",");
            }
        }

        return users;
    }

    public void broadCastMessage(String message) {
        for (SocketHandler client : clientsArray) {
            try {

                if (!client.userName.equals(userName)) {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();

                }
            } catch (IOException e) {
//                e.printStackTrace();
             closeStream(socket, bufferedReader, bufferedWriter);
            }
        }

    }

    public void removeClient() {
        clientsArray.remove(this);
        try {
            socket.close();
            broadCastMessage(userName + " has disconnected !!! ");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeStream(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClient();
        try {
            if (bufferedReader != null) bufferedReader.close();

            if (bufferedWriter != null) bufferedWriter.close();

            if (socket != null) socket.close();


        } catch (IOException e) {
            System.out.println("failed to close connection");
            e.printStackTrace();
        }


    }
}
