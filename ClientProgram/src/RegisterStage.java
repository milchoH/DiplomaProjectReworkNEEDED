/**
 * Created by milcho on 10/12/16.
 */

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class RegisterStage {

    TextField RegisterUsernameTF, RegisterFirstNameTF, RegisterLastNameTF;
    Label RegisterNameLabel, RegisterPasswordLabel, RepeatPasswordLabel,RegisterFirstNameLabel,RegisterLastNameLabel,ErrorMessage;
    PasswordField RegisterPasswordField, RepeatPasswordField;
    Button RegisterClearButton, RegisterCreateButton, RegisterBackButton;
    Stage window;
    GridPane RegisterGrid;
    Scene scene;
    String data;
    ServerSocket server;
    Socket socket;
    String message;
    String addToString = "";
    boolean run;
    boolean flag = false;
    public RegisterStage() {

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Register");

        RegisterGrid = new GridPane();
        RegisterGrid.setPadding(new Insets(10,10,10,10));
        RegisterGrid.setVgap(8);
        RegisterGrid.setHgap(10);

        RegisterNameLabel = new Label("Username: ");
        GridPane.setConstraints(RegisterNameLabel, 0, 0);

        RegisterUsernameTF = new TextField();
        RegisterUsernameTF.setPromptText("Username");
        GridPane.setConstraints(RegisterUsernameTF, 1, 0);

        RegisterPasswordLabel = new Label("Password");
        GridPane.setConstraints(RegisterPasswordLabel, 0, 1);

        RegisterPasswordField = new PasswordField();
        RegisterPasswordField.setPromptText("Password");
        GridPane.setConstraints(RegisterPasswordField, 1, 1);

        RepeatPasswordLabel = new Label("Repeat password");
        GridPane.setConstraints(RepeatPasswordLabel, 0, 2);

        RepeatPasswordField = new PasswordField();
        RepeatPasswordField.setPromptText("Repeat password");
        GridPane.setConstraints(RepeatPasswordField, 1, 2);

        RegisterFirstNameLabel = new Label("First name");
        GridPane.setConstraints(RegisterFirstNameLabel, 0, 5);

        RegisterFirstNameTF = new TextField();
        RegisterFirstNameTF.setPromptText("First Name");
        GridPane.setConstraints(RegisterFirstNameTF, 1, 5);

        RegisterLastNameLabel = new Label("Last name");
        GridPane.setConstraints(RegisterLastNameLabel, 0, 6);

        RegisterLastNameTF = new TextField();
        RegisterLastNameTF.setPromptText("Last Name");
        GridPane.setConstraints(RegisterLastNameTF, 1, 6);

        ErrorMessage = new Label("");
        GridPane.setConstraints(ErrorMessage,0,7);

        RegisterClearButton = new Button("Clear");
        GridPane.setConstraints(RegisterClearButton, 0, 8);
        RegisterClearButton.setOnAction(e -> {

            RegisterUsernameTF.setText("");
            RegisterPasswordField.setText("");
            RegisterFirstNameTF.setText("");
            RegisterLastNameTF.setText("");
            RepeatPasswordField.setText("");
        });

        RegisterCreateButton = new Button("Register");
        GridPane.setConstraints(RegisterCreateButton, 1, 8);


        RegisterBackButton = new Button("Back");
        GridPane.setConstraints(RegisterBackButton, 0, 9);

        RegisterGrid.getChildren().addAll(RegisterNameLabel, RegisterUsernameTF,
                RegisterPasswordLabel, RegisterPasswordField, RepeatPasswordLabel,RepeatPasswordField,ErrorMessage,
                RegisterFirstNameLabel, RegisterFirstNameTF,RegisterLastNameLabel, RegisterLastNameTF, RegisterClearButton,
                RegisterCreateButton, RegisterBackButton);

        scene = new Scene(RegisterGrid,300,350);

        RegisterBackButton.setOnAction(e->{
            window.close();
        });
        RegisterCreateButton.setOnAction(e -> {
            try {
                addNewAccToDB();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        window.setScene(scene);
        window.show();
    }

    public void addNewAccToDB() throws SQLException, ClassNotFoundException, IOException{
        run=checkData();
        if(run){
            data = RegisterUsernameTF.getText() + ","+RegisterPasswordField.getText()+
                    ","+RegisterFirstNameTF.getText()+"," + RegisterLastNameTF.getText();
            System.out.println(data);
            registerStreams(1,data);
            waitForServerResponse();
        }
    }

    public boolean checkData() throws ClassNotFoundException, SQLException{


        if(flag == false){
            if(!RegisterUsernameTF.getText().equals("") && !RegisterPasswordField.getText().equals("") &&
                    !RegisterFirstNameTF.getText().equals("") && !RegisterLastNameTF.getText().equals("") &&
                    !RepeatPasswordField.getText().equals("") ){
                flag = true;
                if(RegisterUsernameTF.getText().matches("[a-zA-Z]+")){
                    RegisterUsernameTF.setStyle("-fx-text-inner-color: black;");
                    flag = true;
                    if(RegisterPasswordField.getText().equals(RepeatPasswordField.getText())){
                        RegisterPasswordField.setStyle("-fx-text-inner-color: black;");
                        RepeatPasswordField.setStyle("-fx-text-inner-color: black;");
                        flag = true;
                    }else{
                        RegisterPasswordField.setStyle("-fx-text-inner-color: red;");
                        RepeatPasswordField.setStyle("-fx-text-inner-color: red;");
                        addToString +="Passwords don't match";
                        flag = false;
                    }
                }else{
                    RegisterUsernameTF.setStyle("-fx-text-inner-color: red;");
                    addToString += "Valid charecters are a-Z";
                    flag= false;
                }
            }else{
                addToString += "Please fill all the fields";
                flag = false;
            }
        }
        ErrorMessage.setText(addToString);
        return flag;
    }

    public void registerStreams(int action,String message){
        try{
            socket = new Socket(setIP.IP, 4568); // IP to send to and PORT to send by

            OutputStream oS = socket.getOutputStream();
            ObjectOutput sO = new ObjectOutputStream(oS);
            sO.writeObject(action+","+ InetAddress.getLocalHost().getHostAddress() +","+message);
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
        server = null;
        server = new ServerSocket(4569);
        socket = server.accept();
        InputStream o = socket.getInputStream();
        ObjectInput s = new ObjectInputStream(o);
        message =(String) s.readObject();
        ErrorMessage.setText(message);
        socket.close();
        server.close();
    }
}
