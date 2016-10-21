/**
 * Created by milcho on 10/12/16.
 */
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class serverSideServerClass {

    final static int PORT = 4568;
    private static ServerSocket server = null;
    private static Socket socket = null;
    static String[] str;
    static String message;
    static int action;
    static DBConnection db;
    static HashMap<String, Boolean> map = new HashMap<>();
    static List<String> friendList = new ArrayList<>();
    static List<String> requestedSearch= new ArrayList<>();
    static List<String> sendToList = new ArrayList<>();
    static boolean flag = false;

    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException{
        try{
            server = new ServerSocket(PORT);
            clearMap();
            while(true){
                try{
                    waitForConnection(action);
                }catch(EOFException eofException){
                    System.out.println("Server ended connection... ");
                }
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
    public static void waitForConnection(int action) throws IOException, ClassNotFoundException, SQLException{
        System.out.println("waiting for Connections ... ");
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){
            public void run() {
                try {
                    if (flag == true){
                        getOnlineUsers();
                        for(int i = 0;i<sendToList.size(); i++){
                            sendFriendList();
                        }
                    }
                    checkIfAnyRequests();
                    clearList();
                    clearMap();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }, 0, 15*1000);

        while(true){
            socket = server.accept();
            InputStream o = socket.getInputStream();
            ObjectInput s = new ObjectInputStream(o);
            message =(String) s.readObject();
            str= message.split(",");
            action = Integer.parseInt(str[0]);
            if(action == 0){
                //keep alive message
                flag = true;
                keepAlive();
                onlineUsersHashMap();
            }
            if (action == 1){
                // create acc
                db = new DBConnection();
                boolean run=checkData();
                if(run){
                    db.connectingToDB();
                    String data = "INSERT INTO USERS VALUES("
                            +"'" + str[2]+"'"+","+"'"+str[3]+"'"+","+"'"+str[4]+"'"+","+"'"+str[5]+"'"+",'FALSE', '" +str[1] +"'"+ ")";
                    db.insertDB(data);
                    db.closeConnectionToDB();
                }
            }
            if(action == 2){
                //login
                loginStreams();
            }
            if(action == 3){
                //chosen acc to request
                checkIfRequestedIsOn();
            }
            if(action == 4){
                //accepted or rejected request
                accRejFrRequest();
            }
            if(action == 5){
                //request search results
                addToFriends();
            }
            if(action == 6){
                changePassword();
            }
        }

    }

    private static boolean checkData() throws ClassNotFoundException, SQLException{
        boolean flag = false;
        db.connectingToDB();
        ResultSet usernames= db.SelectDB("select username from users");
        if(flag == false){
            while(usernames.next()){
                if(str[2].equalsIgnoreCase(usernames.getString(1))){
                    flag = false;
                }else{
                    flag = true;
                }
            }
            db.closeConnectionToDB();
        }
        if(flag == true){
            setupStreams("Account created");
        }else{
            setupStreams("Username in use");
        }
        return flag;
    }
    public static void setupStreams(String message){
        try{
            Socket socket = new Socket(str[1], 4569);

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);

            sO.writeObject(message);
            sO.flush();
            sO.close();
            oS.close();
            socket.close();
        }catch(Exception e){
            System.out.println("UNLUCKY EXCEPTION!!!!");
            System.exit(1);
        }
    }

    public static void loginStreams() throws SQLException, ClassNotFoundException, IOException{
        int flag = 0;
        db = new DBConnection();
        db.connectingToDB();
        Socket socket = new Socket(str[1], 4569);
        String input = str[2]+" "+str[3];
        ResultSet result= db.SelectDB("Select username,password from USERS");
        while(result.next()){
            if(input.equals(result.getString(1)+" "+result.getString(2))){
                flag=1;
            }
        }
        db.closeConnectionToDB();
        OutputStream oS = socket.getOutputStream();
        ObjectOutput sO = new ObjectOutputStream(oS);
        sO.writeObject(flag);
        sO.flush();
        sO.close();
        oS.close();
        socket.close();
    }

    public static void keepAlive() throws ClassNotFoundException, SQLException{
        //updates table with online/ offline users and last IP address
        db = new DBConnection();
        db.connectingToDB();
        String input = str[1];

        db.insertDB("UPDATE USERS SET ONLINE='TRUE', LASTIP='" + str[2]+ "' WHERE USERNAME='" +str[1]+"';");
        db.closeConnectionToDB();
    }

    public static void onlineUsersHashMap() throws ClassNotFoundException, SQLException{
        db = new DBConnection();
        db.connectingToDB();
        ResultSet result=db.SelectDB("SELECT USERNAME, ONLINE FROM USERS ");
        while(result.next()){
            map.put(result.getString(1), result.getBoolean(2));
        }
    }
    public static void clearMap() throws ClassNotFoundException, SQLException{
        flag = false;
        db = new DBConnection();
        db.connectingToDB();
        ResultSet result=db.SelectDB("SELECT USERNAME FROM USERS ");
        while(result.next()){
            map.put(result.getString(1), false);
        }
        db.insertDB("UPDATE USERS SET ONLINE='FALSE';");
        db.closeConnectionToDB();

    }

    public static void sendFriendList() throws ClassNotFoundException, SQLException, UnknownHostException, IOException{
        db = new DBConnection();
        friendList.clear();
        db.connectingToDB();
        ResultSet result1= db.SelectDB("SELECT USERNAME, LASTIP FROM USERS WHERE ONLINE = 'TRUE'");
        while(result1.next()){
            friendList.clear();
            System.out.println("value of result1 is : " + result1.getString(1));
            ResultSet result=db.SelectDB("SELECT FIRSTNAME,LASTNAME,ONLINE, LASTIP FROM FRIENDS, USERS WHERE USERS.USERNAME = FRIENDS.FRIENDS AND FRIENDS.USERS ='" + result1.getString(1) + "';");
            while(result.next()){
                friendList.add(result.getString(1)+","+result.getString(2) + ","+result.getBoolean(3)+ ","+ result.getString(4));
            }
            for(int i = 0 ; i<friendList.size(); i++){
                System.out.println("sendFriendList " + friendList.get(i));
            }


            Socket socket = new Socket(result1.getString(2), 7777);

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);
            sO.writeObject(friendList);

            sO.flush();
            if(sO!=null)
                sO.close();
            if(oS!= null)
                oS.close();
            if(socket!= null)
                socket.close();
        }
        db.closeConnectionToDB();
    }

    public static void getOnlineUsers() throws ClassNotFoundException, SQLException{
        db = new DBConnection();
        db.connectingToDB();
        ResultSet result=db.SelectDB("SELECT LASTIP FROM USERS WHERE ONLINE='TRUE'");
        while(result.next()){
            sendToList.add(result.getString(1));
        }
        for(int i = 0; i<sendToList.size(); i++){
            System.out.println("Send to List: "+sendToList.get(i));
        }
        db.closeConnectionToDB();
    }
    public static void clearList(){
        sendToList.clear();
    }

    public static void checkIfRequestedIsOn() throws ClassNotFoundException, SQLException, UnknownHostException, IOException{
        db = new DBConnection();
        db.connectingToDB();
        ResultSet result = db.SelectDB("SELECT ONLINE FROM USERS WHERE USERNAME = '"+str[2]+"' OR FIRSTNAME  = '"+str[2]+"' OR LASTNAME  = '"+str[2]+"'");
        while(result.next()){
            if(result.getBoolean(1) == true){
                checkIfAnyRequests();
            }else{
                ResultSet result1 = db.SelectDB("SELECT REQUESTER, REQUESTED FROM REQUESTS WHERE REQUESTER = '"+str[1]+"' AND REQUESTED = '" +str[2]+"';");
                while(result1.next()){
                    if(result1.getString(1)==null && result1.getString(2)== null)
                        db.insertDB("INSERT INTO REQUESTS VALUES('"+str[1]+ "', '"+str[2]+", 'FALSE'" +";");
                }
            }
        }
        db.closeConnectionToDB();
    }

    public static void checkIfAnyRequests() throws ClassNotFoundException, SQLException, IOException{
        db = new DBConnection();
        db.connectingToDB();
        ResultSet result = db.SelectDB("SELECT LASTIP, REQUESTER FROM REQUESTS, USERS WHERE REQUESTS.REQUESTED=USERS.USERNAME AND USERS.ONLINE ='TRUE' AND REQUESTS.ACCEPTED ='FALSE'");
        while(result.next()){
            sendDelayedFriendRequest(result.getString(1), result.getString(2));
        }
        db.closeConnectionToDB();
    }
    public static void sendDelayedFriendRequest(String IP, String sender) throws ClassNotFoundException, SQLException, IOException{
        Socket socket = new Socket(IP, 7778);

        OutputStream oS = socket.getOutputStream();
        ObjectOutput sO = new ObjectOutputStream(oS);
        sO.writeObject(sender);
        sO.flush();
        if(sO!=null)
            sO.close();
        if(oS!= null)
            oS.close();
        if(socket!= null)
            socket.close();
    }

    public static void accRejFrRequest() throws ClassNotFoundException, SQLException{
        db = new DBConnection();
        db.connectingToDB();
        Boolean bol = Boolean.valueOf(str[2]);
        if(bol==true){
            db.insertDB("UPDATE REQUESTS SET ACCEPTED='TRUE' WHERE REQUESTED ='"+str[1]+"';");
            ResultSet result=db.SelectDB("SELECT REQUESTER, REQUESTED FROM REQUESTS WHERE REQUESTED ='"+str[1] +"' AND ACCEPTED ='TRUE';");
            db.insertDB("DELETE REQUESTS WHERE REQUESTED ='"+str[1]+"' AND ACCEPTED = 'TRUE';");
            while(result.next()){
                db.insertDB("INSERT INTO FRIENDS VALUES('" +result.getString(1)+"','"+result.getString(2)+"'),('"+result.getString(2)+"','"+result.getString(1)+"');");
            }

        }else{
            db.insertDB("DELETE REQUESTS WHERE REQUESTED ='"+str[1]+"';");
        }
        db.closeConnectionToDB();

    }
    public static void addToFriends() throws ClassNotFoundException, SQLException, UnknownHostException, IOException {
        String IP = null;
        db = new DBConnection();
        requestedSearch.clear();
        db.connectingToDB();
        ResultSet result1 = db.SelectDB("SELECT LASTIP FROM USERS WHERE USERNAME ='"+str[1]+"'");
        while(result1.next()){
            IP = result1.getString(1);
        }
        ResultSet result=db.SelectDB("SELECT USERNAME, FIRSTNAME, LASTNAME FROM USERS WHERE USERNAME LIKE'%"+str[2]+"%' OR FIRSTNAME LIKE '%" +str[2]+ "%' OR LASTNAME LIKE '%" + str[2]+"%';");
        while(result.next()){
            System.out.println(result.getString(1)+","+result.getString(2) + ","+ result.getString(3));
            requestedSearch.add(result.getString(1)+","+result.getString(2) + ","+ result.getString(3));
        }
        db.closeConnectionToDB();
        Socket socket = new Socket(IP, 7779);

        OutputStream oS = socket.getOutputStream();
        ObjectOutput sO = new ObjectOutputStream(oS);
        sO.writeObject(requestedSearch);
        sO.flush();
        if(sO!=null)
            sO.close();
        if(oS!= null)
            oS.close();
        if(socket!= null)
            socket.close();
    }

    public static void changePassword() throws ClassNotFoundException, SQLException{
        db = new DBConnection();
        db.connectingToDB();
        db.insertDB("UPDATE USERS SET PASSWORD ='"+str[2]+"' WHERE USERNAME = '"+str[1]+"';");
        db.closeConnectionToDB();
    }

}