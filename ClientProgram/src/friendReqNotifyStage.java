/**
 * Created by milcho on 10/12/16.
 */
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class friendReqNotifyStage {
    Stage window;
    Scene scene;
    BorderPane mainPane;
    HBox botPane;
    Label notifyLabel;
    Button acceptButton, rejectButton;
    ServerClass SCST;

    public friendReqNotifyStage(String usr){

        window = new Stage();
        window.setTitle("Friend request notification");
        SCST = new ServerClass();

        mainPane = new BorderPane();
        botPane = new HBox();

        notifyLabel = new Label(usr+" wants to be your friend");
        acceptButton = new Button("Accept");
        acceptButton.setOnAction(e->{
            SCST.registerStreams(4, LoggedStage.usr+",true");
            window.close();
        });
        rejectButton = new Button("Reject");
        rejectButton.setOnAction(e->{
            SCST.registerStreams(4, LoggedStage.usr+",false");
            window.close();
        });

        botPane.getChildren().addAll(acceptButton, rejectButton);

        mainPane.setBottom(botPane);
        mainPane.setCenter(notifyLabel);
        scene = new Scene(mainPane,150,150);
        window.setScene(scene);
        window.show();
    }

}
