/**
 * Created by milcho on 10/12/16.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerClass implements Runnable  {
    appendTextListener cc = new appendTextListener();
    receiveFriendsListener rFL = new receiveFriendsListener();
    final int PORT = 8888;
    ServerSocket server = null;
    Socket socket = null;
    String message;
    String usr;
    Map<String, String> map = new HashMap<String, String>();

    public void run() {
        try{
            server = new ServerSocket(PORT);
            while(true){
                try{
                    waitForConnection();
                }catch(EOFException eofException){
                    System.out.println("Server closed connection ... ");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    public void setupStreams(String message){
        map = LoggedStage.map;
        usr = (String) getKeyFromValue(map, LoggedStage.helper);
        try{
            socket = new Socket(usr, 8888);

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);

            sO.writeObject(message);
            sO.flush();
            if(sO!=null)
                sO.close();
            if(oS!= null)
                oS.close();
            if(socket!= null)
                socket.close();
        }catch(Exception e){
            System.out.println("Error in serialization setupStreams class: ServerClassSecondTest");
            System.exit(1);
        }
    }
    public void waitForConnection() throws IOException, ClassNotFoundException{
        while(true){
            socket = server.accept();
            InputStream o = socket.getInputStream();
            ObjectInput s = new ObjectInputStream(o);
            message =  s.readObject().toString();
            System.out.println(message);
            int change = cc.getAppendText();
            change++;
            cc.setAppendText(change);
        }
    }
    public void registerStreams(int action,String message){
        try{
            socket = new Socket(setIP.IP, 4568);

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);

            sO.writeObject(action+","+message);
            sO.flush();
            if(sO!=null)
                sO.close();
            if(oS!= null)
                oS.close();
            if(socket!= null)
                socket.close();
        }catch(Exception e){
            System.out.println("Error in serialization registerStreams class: ServerClassSecondTest");
            System.exit(1);
        }
    }
    public Object getKeyFromValue(Map<String, String> hm, String value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
