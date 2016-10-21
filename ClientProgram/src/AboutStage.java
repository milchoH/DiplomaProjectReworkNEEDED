/**
 * Created by milcho on 10/12/16.
 */
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AboutStage {
    Stage window;
    Scene scene;
    GridPane AboutGrid;
    Label createdByLabel,creatorLabel,FakNomerLabel,FNLabel,UniversLabel,MastersLabel,versionLabel,CurrentVersionLabel;
    Button closeButton;

    public AboutStage(){

        window = new Stage();
        window.setTitle("About");

        AboutGrid = new GridPane();
        AboutGrid.setPadding(new Insets(10,10,10,10));
        AboutGrid.setVgap(8);
        AboutGrid.setHgap(10);

        createdByLabel = new Label("Created by");
        GridPane.setConstraints(createdByLabel,0,0);

        creatorLabel = new Label("Milcho Yordanov Hekimov");
        GridPane.setConstraints(creatorLabel,1,0);

        FakNomerLabel = new Label("Faculty number: ");
        GridPane.setConstraints(FakNomerLabel,0,1);

        FNLabel = new Label("153749");
        GridPane.setConstraints(FNLabel,1,1);

        UniversLabel = new Label("Universiti of Ruse");
        GridPane.setConstraints(UniversLabel,0,2);

        MastersLabel = new Label("Masters diploma project");
        GridPane.setConstraints(MastersLabel,1,2);

        versionLabel = new Label("Version: ");
        GridPane.setConstraints(versionLabel,0,3);

        CurrentVersionLabel = new Label("Beta 0.1");
        GridPane.setConstraints(CurrentVersionLabel,1,3);

        closeButton = new Button("Close");
        GridPane.setConstraints(closeButton,0,4);
        closeButton.setOnAction(e ->{
            window.close();
        });

        AboutGrid.getChildren().addAll(createdByLabel,creatorLabel, FakNomerLabel,
                FNLabel,UniversLabel,MastersLabel,versionLabel,CurrentVersionLabel,closeButton);

        scene = new Scene(AboutGrid, 400,140);
        window.setScene(scene);
        window.show();

    }
}

