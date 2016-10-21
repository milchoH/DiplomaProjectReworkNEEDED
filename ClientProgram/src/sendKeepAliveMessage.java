/**
 * Created by milcho on 10/12/16.
 */
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.TimerTask;

public class sendKeepAliveMessage extends TimerTask {
    String KEEP_ALIVE_MESSAGE = new String();
    private volatile boolean stopRequested;

    @Override
    public void run() {
        if(!stopRequested){
            try{
                Socket socket = new Socket(setIP.IP, 4568);

                OutputStream oS = socket.getOutputStream();
                ObjectOutput sO = new ObjectOutputStream(oS);

                sO.writeObject(KEEP_ALIVE_MESSAGE +","+ InetAddress.getLocalHost().getHostAddress());
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
    }
    public void addToMessage(String usr){
        KEEP_ALIVE_MESSAGE = usr;
    }
    public void requestStop(){
        stopRequested = true;
    }
}
