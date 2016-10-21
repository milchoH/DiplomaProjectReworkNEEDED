/**
 * Created by milcho on 10/12/16.
 */

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AddContactStage {
    Stage window;
    Scene scene;
    BorderPane mainPane;
    HBox topPane;
    VBox centerPane;
    Label searchLabel;
    TextField searchTF;
    Button searchButton, addFriend;
    String str;
    final int PORT = 4568;
    Socket socket;
    ServerSocket server = null;
    TableView<tableViewGetters> table = new TableView<tableViewGetters>();
    List<String> list = new ArrayList<String>();
    tableViewGetters tvg;

    public AddContactStage(){
        window = new Stage();
        window.setTitle("Add new contacts");

        mainPane = new BorderPane();
        topPane = new HBox();
        centerPane = new VBox();

        table.setEditable(false);


        TableColumn<tableViewGetters, String> firstNameCol = new TableColumn("First name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstNameCol"));
        TableColumn<tableViewGetters, String> lastNameCol = new TableColumn("Last name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastNameCol"));
        TableColumn<tableViewGetters, String> accCol = new TableColumn("Account");
        accCol.setCellValueFactory(new PropertyValueFactory<>("accCol"));

        table.getColumns().setAll(firstNameCol,lastNameCol,accCol);

        searchLabel = new Label("Search for new contacts ");

        searchTF = new TextField();
        searchTF.setPromptText("Search");

        searchButton = new Button("Search");
        searchButton.setOnAction(e->{
            table.getItems().clear();
            requestSearchResults();
            try {
                waitForResults();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            for(int i = 0; i<list.size(); i++){
                String[] data = list.get(i).split(",");
                tableViewGetters tvg = new tableViewGetters();
                tvg.setAccCol(data[0]);
                tvg.setFirstNameCol(data[1]);
                tvg.setLastNameCol(data[2]);
                table.getItems().add(tvg);
            }
        });
        addFriend = new Button("Add");
        addFriend.setOnAction(e->{

            requestFriend();
        });
        topPane.getChildren().addAll(searchLabel,searchTF, searchButton,addFriend);

        centerPane.getChildren().add(table);

        mainPane.setCenter(centerPane);
        mainPane.setTop(topPane);
        scene = new Scene(mainPane,380,300);
        window.setScene(scene);
        window.show();
    }

    public void requestSearchResults(){
        str = searchTF.getText();
        try{
            Socket socket = new Socket(setIP.IP, 4568); // IP to send to and PORT to send by

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);
            sO.writeObject("5"+","+LoggedStage.usr +","+str);
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

    public void requestFriend(){
        try{
            tvg = table.getSelectionModel().getSelectedItem();
            str = tvg.accCol;
            Socket socket = new Socket(setIP.IP, 4568); // IP to send to and PORT to send by

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);
            sO.writeObject("3"+","+LoggedStage.usr+","+str);
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
    public void waitForResults() throws ClassNotFoundException{
        try {
            server = new ServerSocket(7779);
            socket = server.accept();
            InputStream o = socket.getInputStream();
            ObjectInput s = new ObjectInputStream(o);
            list.addAll((List<String>) s.readObject());
            server.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

