/**
 * Created by milcho on 10/12/16.
 */

import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class recFriendReqThread implements Runnable {
    Socket socket;
    ServerSocket server = null;
    String str;
    receiveFriendReqListener RFRL = new receiveFriendReqListener();
    private volatile boolean stopRequested;

    public void run(){
        if(!stopRequested){
            try {
                server = new ServerSocket(7778);
                while(true){
                    socket = server.accept();
                    InputStream o = socket.getInputStream();
                    ObjectInput s = new ObjectInputStream(o);
                    str = s.readObject().toString();
                    Platform.runLater(new Runnable(){
                        public void run(){
                            new friendReqNotifyStage(str);}
                    });

                }
            }catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void requestStop(){
        stopRequested = true;
    }
}
