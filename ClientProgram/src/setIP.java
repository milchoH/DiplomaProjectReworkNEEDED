import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by milcho on 10/12/16.
 */
public class setIP {
    static String IP;
    Stage window;
    Label setIPLabel;
    Button okButton;
    TextField ipTF;
    GridPane setIPGrid;
    public setIP(){
        window = new Stage();
        window.setTitle("set IP");

        setIPGrid = new GridPane();
        setIPGrid.setPadding(new Insets(10,10,10,10));
        setIPGrid.setVgap(8);
        setIPGrid.setHgap(10);

        setIPLabel = new Label("Set IP");
        GridPane.setConstraints(setIPLabel,0,0);

        ipTF = new TextField();
        ipTF.setPromptText("Set IP");
        GridPane.setConstraints(ipTF,0,1);

        okButton = new Button("OK");
        okButton.setOnAction(e->{
            IP = ipTF.getText();
        });
    }
}
