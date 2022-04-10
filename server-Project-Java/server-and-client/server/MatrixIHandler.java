package server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles server.Matrix-related tasks
 */
public class MatrixIHandler implements IHandler {
    private Matrix matrix;
    private Index startIndex;
    private Index endIndex;
    private volatile boolean doWork = true;

    @Override
    public void resetMembers() {
        this.matrix = null;
        this.startIndex = null;
        this.endIndex = null;
        this.doWork = true;
    }

    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {
        /*
        Send data as bytes.
        Read data as bytes then transform to meaningful data
        ObjectInputStream and ObjectOutputStream can read and write both primitives and objects
         */
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);
        boolean doWork = true;

        // handle client's tasks
        while(doWork){

            switch (objectInputStream.readObject().toString()){


                case "matrix" :{
                    // client will now send a 2d array. handler will create a matrix object
                    JSONArray objects  = new JSONArray(objectInputStream.readObject().toString());

                    int[][] tempArray = new int[objects.length()][];
                    for (int i = 0; i < tempArray.length; i++) {
                        String str= objects.get(i).toString();
                        String[] strings = str.replaceAll("\\[", "").replaceAll("]", "").split(",");
                        int[] arr = new int[strings.length];
                        for (int j = 0; j < strings.length; j++) {
                            arr[j] = Integer.valueOf(strings[j]);
                        }
                        tempArray[i] = arr;
                    }
                    System.out.println("Server: Got 2d array");
                    this.matrix = new Matrix(tempArray);
                    this.matrix.printMatrix();
                    break;
                }

                case "getNeighbors":{
                    // handler will receive an index, then compute its neighbors

                    String str= (String)objectInputStream.readObject();
                    String[] strings = str.replaceAll("\\(", "").replaceAll("\\)", "").split(",");
                    int[] arr = new int[strings.length];
                    arr[0] = Integer.valueOf(strings[0]);
                    arr[1] = Integer.valueOf(strings[1]);
                    Index tempIndex = new Index(arr[0], arr[1]);

                    if(this.matrix!=null){
                        List<Index> neighbors = new ArrayList<>(this.matrix.getNeighbors(tempIndex));
                        System.out.println("Server: neighbors of "+ tempIndex + ":  " + neighbors);

                        // send to socket's outputstream
                        objectOutputStream.writeObject(neighbors);
                    }
                    break;
                }

                case "getReachables":{
                    // handler will receive an index, then compute its neighbors
                    String str= (String)objectInputStream.readObject();
                    String[] strings = str.replaceAll("\\(", "").replaceAll("\\)", "").split(",");
                    int[] arr = new int[strings.length];
                    arr[0] = Integer.valueOf(strings[0]);
                    arr[1] = Integer.valueOf(strings[1]);
                    Index tempIndex = new Index(arr[0], arr[1]);
                    if(this.matrix!=null){
                        List<Index> reachables = new ArrayList<>(this.matrix.getReachables(tempIndex));
                        System.out.println("Server: neighbors of "+ tempIndex + ":  " + reachables);

                        // send to socket's outputstream
                        objectOutputStream.writeObject(reachables);
                    }
                }


                case "stop":{
                    doWork = false;
                    break;
                }
            }
        }





    }


}
