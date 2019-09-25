import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PwWrongWindow{

    public Stage pwWrongWindowBuild(Stage parent, Stage pwWrongStage){

        Label passwordWrongWindowLabel = new Label("Password wrong!!!");
        Button closeWindowButton = new Button("Ok");

        closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

        closeWindowButton.setStyle("-fx-background-color: rgb(136,140,140)");

        VBox passwordWrongLayout = new VBox();
        passwordWrongLayout.setAlignment(Pos.CENTER);
        passwordWrongLayout.getChildren().add(passwordWrongWindowLabel);
        passwordWrongLayout.getChildren().add(closeWindowButton);
        passwordWrongLayout.setStyle("-fx-background-color: rgb(17,120,120)");

        Scene passwordWrongScene = new Scene(passwordWrongLayout, 230, 100);

        // New window (Stage)
        //Stage thirdWindow = new Stage();
        pwWrongStage.setTitle("Password wrong");
        pwWrongStage.setScene(passwordWrongScene);

        // Specifies the modality for new window.
        //pwWrong.initModality(Modality.WINDOW_MODAL);
        // Modality.WINDOW_MODAL locks the parent window
        // Modality.APPLICATION_MODAL locks all other windows

        // Specifies the owner Window (parent) for new window
        //pwWrong.initOwner(primaryStage);

        // Set position of second window, related to primary window.
        pwWrongStage.setX(parent.getX() + 200);
        pwWrongStage.setY(parent.getY() + 100);

        pwWrongStage.show();
        return pwWrongStage;
    }

}
