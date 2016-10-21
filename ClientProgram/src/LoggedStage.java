/**
 * Created by milcho on 10/12/16.
 */

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class LoggedStage extends Thread {
    Stage window;
    Scene scene;
    Button addContactsButton,historyButton;
    TreeView<String> usersTreeView;
    Menu firstMenu,contactsMenu,helpMenu;
    MenuItem changePasswordMenuBar,logOutMenuBar,closeMenuItem,addContactMenuBar,historyMenuItem,
            aboutMenu;
    MenuBar menuBar;
    TreeItem<String> root, OnlineUsers,OfflineUsers;
    BorderPane mainPane;
    StackPane CenterLayout;
    HBox botLayout;
    static String usr;
    static String helper = null;
    sendKeepAliveMessage SKAM;
    Socket socket;
    ServerSocket server = null;
    Object message;
    receiveFriendsListener rFL;
    String IP;
    Thread t;
    receiveMessageHandler RMH;
    recFriendReqThread FRT;
    static HashMap<String, String> map = new HashMap<>();
    receiveMessageHandler rmh = new receiveMessageHandler();
    appendTextListener cc;
    String[] str;
    String help;
    HistoryStage hs;

    public LoggedStage(String user) throws ClassNotFoundException, IOException  {
        usr = user;
        window = new Stage();
        handler();
        window.setTitle("Logged as: "+user);
        RMH = new receiveMessageHandler();
        cc = new appendTextListener();

        t = new Thread(RMH);
        t.start();

        FRT = new recFriendReqThread();
        t = new Thread(FRT);
        t.start();

        root = new TreeItem<>();
        root.setExpanded(true);

        OnlineUsers = makeBranch("Online users", root);
        OfflineUsers = makeBranch("Offline users", root);

        usersTreeView = new TreeView<>(root);
        usersTreeView.setShowRoot(false);
        usersTreeView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    TreeItem<String> item = usersTreeView.getSelectionModel().getSelectedItem();
                    try {
                        new HistoryStage(item.getValue());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    helper = item.getValue();
                }
            }
        });

        firstMenu = new Menu("Profile menu");

        changePasswordMenuBar = new MenuItem("Change password");
        changePasswordMenuBar.setOnAction(e->{
            new ChangePasswordStage();
        });

        logOutMenuBar = new MenuItem("Log out");
        logOutMenuBar.setOnAction(e -> {
            new LoginStage();
            window.close();
            closeSKAM();

        });
        closeMenuItem = new MenuItem("Close");
        closeMenuItem.setOnAction(e ->{
            window.close();
            closeSKAM();
        });
        firstMenu.getItems().addAll(new SeparatorMenuItem(), changePasswordMenuBar,
                new SeparatorMenuItem(), logOutMenuBar, closeMenuItem);

        contactsMenu = new Menu("Contacts");
        addContactMenuBar = new MenuItem("Add contact");
        addContactMenuBar.setOnAction(e->{
            new AddContactStage();
        });

        historyMenuItem = new MenuItem("History");
        historyMenuItem.setOnAction(e->{
            try {
                new HistoryStage("History");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        contactsMenu.getItems().addAll(addContactMenuBar, new SeparatorMenuItem(), historyMenuItem);

        helpMenu = new Menu("Help");
        aboutMenu = new MenuItem("About");
        aboutMenu.setOnAction(e->{
            new AboutStage();
        });
        helpMenu.getItems().add(aboutMenu);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(firstMenu, contactsMenu, helpMenu);

        CenterLayout = new StackPane();
        CenterLayout.getChildren().add(usersTreeView);

        botLayout = new HBox();

        addContactsButton = new Button("Add contact");
        addContactsButton.setOnAction(e->{
            new AddContactStage();
        });

        historyButton = new Button("History");
        historyButton.setOnAction(e->{
            try {
                new HistoryStage("History");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        start();

        botLayout.getChildren().addAll(addContactsButton,historyButton);

        mainPane = new BorderPane();
        mainPane.setTop(menuBar);
        mainPane.setCenter(CenterLayout);
        mainPane.setBottom(botLayout);
        scene = new Scene(mainPane,350,300);
        window.setScene(scene);
        window.show();

        createUserFolder();

        cc.appendTextProperty().addListener((v,oldvalue,newvalue)->{
            if(HistoryStage.flag == false){
                Platform.runLater(new Runnable(){
                    public void run(){
                        try {
                            new HistoryStage(RMH.helper);
                            help =  RMH.message;
                            HistoryStage.receivedMessagesArea.appendText(help);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }}
                });
            }
            else{
                help =  RMH.message;
                System.out.println("this is help in Logged Stage"+help);
                HistoryStage.receivedMessagesArea.appendText(help);
            }
        });
    }
    public static TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    public void handler(){
        SKAM = new sendKeepAliveMessage();
        SKAM.addToMessage("0,"+usr);
        Timer timer = new Timer();
        timer.schedule(SKAM, 0, 10*1000);
    }

    public void closeSKAM(){
        SKAM.requestStop();
    }
    public void run(){
        Socket socket;
        server = null;
        List<String> friendMessage = null;
        String[] str = null;
        String helper;
        boolean flag;
        try {
            server = new ServerSocket(7777);
            while(true){
                socket = server.accept();
                InputStream o = socket.getInputStream();
                ObjectInput s = new ObjectInputStream(o);
                friendMessage =  (List<String>) s.readObject();
                OnlineUsers.getChildren().clear();
                OfflineUsers.getChildren().clear();
                for(int i=0; i<friendMessage.size(); i++ ){
                    str = friendMessage.get(i).split(",");
                    flag = Boolean.parseBoolean(str[2]);
                    IP = str[3];
                    helper = str[0] + " " + str[1];
                    map.put(IP, helper);
                    if(flag == true){
                        makeBranch(str[0]+" "+str[1], OnlineUsers);
                    }
                    else{
                        makeBranch(str[0]+" "+str[1], OfflineUsers);
                    }
                }
                System.out.println(map);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void createUserFolder(){
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "Chat History" + File.separator + usr + " History";
        File customDir = new File(path);

        if (customDir.exists()) {
            System.out.println(customDir + " already exists");
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
        } else {
            System.out.println(customDir + " was not created");
        }
    }
}
