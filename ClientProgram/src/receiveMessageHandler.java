/**
 * Created by milcho on 10/12/16.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class receiveMessageHandler implements Runnable {
    final int PORT = 8888;
    ServerSocket server = null;
    Socket socket = null;
    String message = new String();
    static	String helper;
    String[] str;
    appendTextListener cc = new appendTextListener();
    private volatile boolean stopRequested = false;
    public void run(){
        if(!stopRequested){
            try {
                server = new ServerSocket(PORT);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            while(true){
                try {
                    socket = server.accept();
                    InputStream o = socket.getInputStream();
                    ObjectInput s = new ObjectInputStream(o);
                    message =  s.readObject().toString();
                    System.out.println("This is the message :"+message);
                    int end = message.indexOf("[");
                    System.out.println(message.substring(0, end-1));
                    //	str = message.split("[");
                    helper = message.substring(0, end-1);
                    //	System.out.println("str[0] in receive message handler "+str[0]);
                    System.out.println("helper in receive message handler "+helper);
                    int change = cc.getAppendText();
                    change++;
                    cc.setAppendText(change);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void requestStop() throws IOException{
        stopRequested = true;
        if (server != null)
            server.close();
    }
    public void requestStart(){
        stopRequested = false;
    }
}
