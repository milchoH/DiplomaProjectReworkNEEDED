/**
 * Created by milcho on 10/12/16.
 */

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HistoryStage {
    Stage window;
    Scene scene;
    TextArea sendMessages;
    static TextArea receivedMessagesArea;
    Button sendButton;
    TreeView<String> usersTreeView;
    VBox leftLayout,centerLayout;
    BorderPane mainPane;
    LoggedStage ls;
    String user;
    Calendar cal = Calendar.getInstance();
    String str;
    Thread t;
    DateFormat dateFormat;
    ServerClass sr;
    FileWriter writer;
    String path;
    File file;
    static boolean flag = false;
    TreeItem<String> root;

    public HistoryStage(String usr) throws IOException{
        window = new Stage();
        user = usr;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        sr = new ServerClass();

        window.setTitle(usr);

        window.setMinWidth(600);
        window.setMinHeight(300);

        usersTreeView = new TreeView<>(root);
        usersTreeView.setShowRoot(false);
        //  usersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        root = new TreeItem<>();
        root.setExpanded(true);

        leftLayout = new VBox(20);
        leftLayout.setPadding(new Insets(20,20,20,20));
        leftLayout.getChildren().addAll(usersTreeView);

        receivedMessagesArea = new TextArea();
        receivedMessagesArea.setPrefRowCount(10);
        receivedMessagesArea.setPrefColumnCount(100);
        receivedMessagesArea.setWrapText(true);
        receivedMessagesArea.setPrefWidth(150);
        receivedMessagesArea.setPrefHeight(1000);
        receivedMessagesArea.setEditable(false);

        sendMessages = new TextArea();
        sendMessages.setPrefRowCount(10);
        sendMessages.setPrefColumnCount(100);
        sendMessages.setWrapText(true);
        sendMessages.setPrefWidth(150);
        sendMessages.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER) {
                if(sendMessages.getText() != null && sendMessages.getText() != ""){
                    str = LoggedStage.usr +" ["+dateFormat.format(cal.getTime())+"] "+ ": " +sendMessages.getText() + "\n";
                    sr.setupStreams(str);
                    try {
                        writeHistoryFiles();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    sendMessages.clear();
                }else {
                    sendMessages.clear();
                }
            }
        });
        sendButton = new Button("Send");
        sendButton.setOnAction(e->{
            if(sendMessages.getText() != null && sendMessages.getText() != ""){
                str = LoggedStage.usr +" ["+dateFormat.format(cal.getTime())+"] "+ ": " +sendMessages.getText() + "\n";
                sr.setupStreams(str);
                try {
                    writeHistoryFiles();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
                sendMessages.clear();
            }else {
                sendMessages.clear();
            }
        });
        centerLayout = new VBox(20);
        centerLayout.setPadding(new Insets(20,20,20,20));
        centerLayout.getChildren().addAll(receivedMessagesArea,sendMessages,
                sendButton);
        mainPane = new BorderPane();
        mainPane.setLeft(leftLayout);
        mainPane.setCenter(centerLayout);
        scene = new Scene(mainPane, 1000,250);
        window.setScene(scene);
        window.show();
        flag = true;

        window.setOnCloseRequest(e-> {
            flag = false;
        });

        try {
            createOverallHistoryFile();
            createHistoryFiles();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public void createHistoryFiles() throws IOException{
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "Chat History" + File.separator + LoggedStage.usr + " History";
        file = new File(path+File.separator+ user+".txt");

        if (file.exists()) {
            System.out.println(file + " already exists");
        } else if (file.createNewFile()) {
            System.out.println(file + " was created");
        } else {
            System.out.println(file + " was not created");
        }
    }
    public void createOverallHistoryFile() throws IOException{
        path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "Chat History" + File.separator + LoggedStage.usr + " History";
        file = new File(path+File.separator+"timingLog.txt" );

        if (file.exists()) {
            System.out.println(file + " already exists");
        } else if (file.createNewFile()) {
            System.out.println(file + " was created");
        } else {
            System.out.println(file + " was not created");
        }
    }
    public void writeHistoryFiles() throws IOException{
        path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "Chat History" + File.separator + LoggedStage.usr + " History"+ File.separator+user+".txt" ;
        writer = new FileWriter(path, true);
        writer.write(str);
        writer.flush();
        writer.close();
    }
    public void writeOverallHistoryFile(){

    }

}
