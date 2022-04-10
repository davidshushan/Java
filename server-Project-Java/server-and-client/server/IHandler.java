package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHandler {
    public abstract void handle(InputStream fromClient,
                                OutputStream toClient) throws IOException, ClassNotFoundException;
    public abstract void resetMembers();
}
