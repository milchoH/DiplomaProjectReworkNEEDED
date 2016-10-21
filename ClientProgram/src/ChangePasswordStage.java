/**
 * Created by milcho on 10/12/16.
 */
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChangePasswordStage {
    Stage window;
    Scene scene;
    GridPane changePasswordGrid;
    Label oldPasswordLabel,repeatOldPasswordLabel,newPasswordLabel,repeatNewPasswordLabel;
    PasswordField oldPasswordField,repeatOldPasswordField,newPasswordField,repeatNewPasswordField;
    Button closeButton, saveButton;
    ServerClass sc;

    public ChangePasswordStage(){

        sc = new ServerClass();
        window = new Stage();
        window.setTitle("Change password");

        changePasswordGrid = new GridPane();
        changePasswordGrid.setPadding(new Insets(10,10,10,10));
        changePasswordGrid.setVgap(8);
        changePasswordGrid.setHgap(10);

        oldPasswordLabel = new Label("Current password");
        GridPane.setConstraints(oldPasswordLabel,0,0);

        oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Current password");
        GridPane.setConstraints(oldPasswordField,1,0);

        repeatOldPasswordLabel = new Label("Repeat current password");
        GridPane.setConstraints(repeatOldPasswordLabel,0,1);

        repeatOldPasswordField = new PasswordField();
        repeatOldPasswordField.setPromptText("Repeat old password");
        GridPane.setConstraints(repeatOldPasswordField,1,1);

        newPasswordLabel = new Label("New password");
        GridPane.setConstraints(newPasswordLabel,0,2);

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Type new password here");
        GridPane.setConstraints(newPasswordField,1,2);

        repeatNewPasswordLabel = new Label("Repeat new password");
        GridPane.setConstraints(repeatNewPasswordLabel,0,3);

        repeatNewPasswordField = new PasswordField();
        repeatNewPasswordField.setPromptText("Repeat new password");
        GridPane.setConstraints(repeatNewPasswordField,1,3);

        closeButton = new Button("Close");
        closeButton.setOnAction(e->{
            window.close();
        });
        GridPane.setConstraints(closeButton,0,4);

        saveButton = new Button("Save");
        saveButton.setOnAction(e->{
            if(checkIfPasswordsAreEqual()){
                sc.registerStreams(6,LoggedStage.usr+","+ newPasswordField.getText());
                window.close();
            }
        });
        GridPane.setConstraints(saveButton,1,4);

        changePasswordGrid.getChildren().addAll(oldPasswordLabel,oldPasswordField,
                repeatOldPasswordLabel,repeatOldPasswordField,newPasswordLabel,newPasswordField,
                repeatNewPasswordLabel,repeatNewPasswordField,saveButton,closeButton);

        scene = new Scene(changePasswordGrid,330,180);
        window.setScene(scene);
        window.show();
    }

    public boolean checkIfPasswordsAreEqual(){
        if(oldPasswordField.getText().equals(repeatOldPasswordField.getText())&& newPasswordField.getText().equals(repeatNewPasswordField.getText())){
            return true;
        }
        else{
            return false;
        }
    }
}
