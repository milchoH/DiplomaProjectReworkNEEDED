/**
 * Created by milcho on 10/12/16.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class LoginStage extends Application {
    Stage window;
    Scene LoginScene;
    GridPane LoginGrid;
    Label nameLabel,passLabel;
    TextField nameTF;
    PasswordField passTF;
    Button LoginButton,RegisterButton;
    GridPane LogedGrid;
    Alert ErrorMessage;

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) throws Exception {
    }

    public LoginStage() {

        window = new Stage();
        window.setTitle("Login");

        LoginGrid = new GridPane();
        LoginGrid.setPadding(new Insets(10, 10, 10, 10));
        LoginGrid.setVgap(8);
        LoginGrid.setHgap(10);

        new setIP();

        nameLabel = new Label("Username");
        GridPane.setConstraints(nameLabel, 0, 0);

        nameTF = new TextField();
        nameTF.setPromptText("Username");
        GridPane.setConstraints(nameTF, 1, 0);

        passLabel = new Label("Password");
        GridPane.setConstraints(passLabel, 0, 1);

        passTF = new PasswordField();
        passTF.setPromptText("Password");
        GridPane.setConstraints(passTF, 1, 1);

        LoginButton = new Button("Login");
        GridPane.setConstraints(LoginButton, 1, 2);

        LoginButton.setOnAction(e ->{
            try {
                sendLoginForValidation();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        RegisterButton = new Button("Register");
        GridPane.setConstraints(RegisterButton, 0, 2);
        RegisterButton.setOnAction(e -> {
            new RegisterStage();
        });

        LoginGrid.getChildren().addAll(nameLabel, nameTF, passLabel, passTF, LoginButton, RegisterButton);

        LogedGrid = new GridPane();
        LogedGrid.setPadding(new Insets(10, 10, 10, 10));
        LogedGrid.setVgap(8);
        LogedGrid.setHgap(10);

        LoginScene = new Scene(LoginGrid, 250, 110);
        window.setScene(LoginScene);
        window.show();
        createFolderInUsers();
    }

    public void sendLoginForValidation() throws ClassNotFoundException, SQLException, IOException{
        String data;
        data = nameTF.getText() + ","+passTF.getText();
        validateLogin(2,data);
        waitForServerResponse();
    }
    public void validateLogin(int action,String message){
        try{
            Socket socket = new Socket(setIP.IP, 4568); // IP to send to and PORT to send by

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);
            sO.writeObject(action+","+InetAddress.getLocalHost().getHostAddress()+","+message);
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
    private void waitForServerResponse() throws IOException, ClassNotFoundException {
        Socket socket;
        ServerSocket server = null;
        int message;
        server = new ServerSocket(4569);
        socket = server.accept();
        InputStream o = socket.getInputStream();
        ObjectInput s = new ObjectInputStream(o);
        message = (int) s.readObject();
        if(message == 1){
            new LoggedStage(nameTF.getText());
            window.close();
            socket.close();
            server.close();
        }else{
            ErrorMessage = new Alert(AlertType.ERROR);
            ErrorMessage.setTitle("Error Dialog");
            ErrorMessage.setHeaderText("Invalid login");
            ErrorMessage.showAndWait();
            socket.close();
            server.close();
        }
    }
    public void createFolderInUsers(){
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "Chat History";
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
