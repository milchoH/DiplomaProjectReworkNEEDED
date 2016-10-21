/**
 * Created by milcho on 10/12/16.
 */

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditProfileStage {

    Stage window;
    Scene scene;
    GridPane EditProfileGrid;
    Label fullNameLabel,countryLabel,cityLabel,genderLabel,birthDateLabel;
    TextField fullNameTF,countryTF,cityTF;
    ChoiceBox<String> genderChoiceBox;
    Button closeButton,saveButton;
    DatePicker birthDatePicker;


    public EditProfileStage(){
        window = new Stage();
        window.setTitle("Edit profile");

        EditProfileGrid = new GridPane();
        EditProfileGrid.setPadding(new Insets(10,10,10,10));
        EditProfileGrid.setVgap(8);
        EditProfileGrid.setHgap(10);

        fullNameLabel = new Label("Full name");
        GridPane.setConstraints(fullNameLabel,0,0);

        fullNameTF = new TextField();
        fullNameTF.setPromptText("Full name");
        GridPane.setConstraints(fullNameTF,1,0);

        countryLabel = new Label("Country");
        GridPane.setConstraints(countryLabel,0,1);

        countryTF = new TextField();
        countryTF.setPromptText("Country/Region");
        GridPane.setConstraints(countryTF,1,1);

        cityLabel = new Label("City:");
        GridPane.setConstraints(cityLabel,0,2);

        cityTF = new TextField();
        cityTF.setPromptText("City");
        GridPane.setConstraints(cityTF,1,2);

        genderLabel = new Label("Gender: ");
        GridPane.setConstraints(genderLabel,0,3);

        genderChoiceBox = new ChoiceBox<String>();
        genderChoiceBox.getItems().add("Gender");
        genderChoiceBox.getItems().add("Male");
        genderChoiceBox.getItems().add("Female");

        genderChoiceBox.setValue("Gender");
        GridPane.setConstraints(genderChoiceBox,1,3);

        birthDateLabel  = new Label("Birth date: ");
        GridPane.setConstraints(birthDateLabel,0,4);

        birthDatePicker = new DatePicker();
        GridPane.setConstraints(birthDatePicker,1,4);

        closeButton = new Button("Close");
        closeButton.setOnAction(e->{
            window.close();
        });
        GridPane.setConstraints(closeButton,0,5);

        saveButton = new Button("Save");
        saveButton.setOnAction(e->{
            window.close();
        });
        GridPane.setConstraints(saveButton,1,5);

        EditProfileGrid.getChildren().addAll(fullNameLabel, fullNameTF,
                countryLabel,countryTF,cityLabel,cityTF,genderLabel,genderChoiceBox,
                birthDateLabel,birthDatePicker,closeButton,saveButton);

        scene = new Scene(EditProfileGrid,300,300);
        window.setScene(scene);
        window.show();

    }

}