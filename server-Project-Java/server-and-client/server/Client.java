package server;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.json.JSONObject;


public class Client {
    public static void writeJsonFile(JSONObject jsonObject){
        try (FileWriter file = new FileWriter("clientCommands.json")) {
            file.write(jsonObject.toString());
            file.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject readJsonFile(){
        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader("clientCommands.json"))
        {
            Gson gson = new Gson();
            //Read JSON file

            JSONObject jsonObject = new JSONObject(gson.toJson(jsonParser.parse(reader)));
            return jsonObject;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket =new Socket("127.0.0.1",8020);
        System.out.println("client: Created Socket");

        ObjectOutputStream toServer=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream fromServer=new ObjectInputStream(socket.getInputStream());

        // sending #1 matrix
        int[][] source = {
                {0, 1, 0},
                {0, 1, 1},
                {1, 0, 1},
        };

        //send "matrix" command then write 2d array to socket
//        toServer.writeObject("matrix");
//        toServer.writeObject(source);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseMessage","matrix");
        jsonObject.put("array",source );

        writeJsonFile(jsonObject);
        jsonObject = readJsonFile();

        toServer.writeObject(jsonObject.get("caseMessage"));
        toServer.writeObject(jsonObject.get("array").toString());


        //send "neighbors" command then write an index to socket
        jsonObject.put("caseMessage1","getNeighbors");
        jsonObject.put("index",new Index(1,1));
        writeJsonFile(jsonObject);

        jsonObject = readJsonFile();

        toServer.writeObject(jsonObject.get("caseMessage1").toString());
        toServer.writeObject(jsonObject.get("index").toString());
        System.out.println("****************");
//        toServer.writeObject(new Index(1,1));

        // get neighboring indices as list
        List<Index> AdjacentIndices =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("from server - Neighboring Indices are: "+ AdjacentIndices);

        //send "reachables" command then write an index to socket
//        toServer.writeObject("getReachables");
//        toServer.writeObject(new Index(1,1));
        jsonObject.put("caseMessage2","getReachables");
        jsonObject.put("index2",new Index(1,1));
        writeJsonFile(jsonObject);
        jsonObject = readJsonFile();
        toServer.writeObject(jsonObject.get("caseMessage2").toString());
        toServer.writeObject(jsonObject.get("index2").toString());
//        toServer.writeObject(obj.toString());
//        toServer.writeObject(jsonObject.toString());
        System.out.println("****************");

        // get reachable indices as list
        List<Index> reachables =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("from server - Reachable Indices are:  "+ reachables);
        System.out.println("****************");

//        toServer.writeObject("stop");
        jsonObject.put("caseMessage3","stop");
        writeJsonFile(jsonObject);
        toServer.writeObject(jsonObject.get("caseMessage3").toString());
//        toServer.writeObject(jsonObject.toString());
        System.out.println("client: Close all streams");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("client: Closed operational socket");
    }
}

