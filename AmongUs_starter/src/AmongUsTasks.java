/**
 * @author: Uejsi Hamja
 */
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class  AmongUsTasks extends Application{

    private boolean wiresTaskCompleted = false;
    private boolean cardSwipeTaskCompleted = false;

    public static void main(String[] args) {
    launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Among Us Tasks");

        Label lblTitle = new Label("Among Us - Tasks");
        lblTitle.setStyle("-fx-font-size: 23;");

        Button btnWires = new Button("Fix Wires");
        btnWires.setOnAction(e -> fixWires(primaryStage));

        Button btnCard = new Button("Swipe Card");
        btnCard.setOnAction(e -> swipeCard(primaryStage));


        VBox root = new VBox(lblTitle, btnWires, btnCard);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fixWires(Stage primaryStage) {
        Label instructionLabel = new Label("Click the wires to fix them:");

        ImageView wiresImageView = new ImageView(new Image("wires.png"));
        wiresImageView.setOnMouseClicked(e -> {
            wiresTaskCompleted = true;
            checkTasksCompleted(primaryStage);
        });

        VBox root = new VBox(instructionLabel, wiresImageView);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        primaryStage.setScene(new Scene(root, 400, 300));
    }

    private void swipeCard(Stage primaryStage) {
        Label instructionLabel = new Label("Click the card to swipe it:");

        ImageView cardImageView = new ImageView(new Image("card.png"));
        cardImageView.setOnMouseClicked(e -> {
            cardSwipeTaskCompleted = true;
            checkTasksCompleted(primaryStage);
        });

        VBox root = new VBox(instructionLabel, cardImageView);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        primaryStage.setScene(new Scene(root, 400, 300));
    }

    private void checkTasksCompleted(Stage primaryStage) {
        if (wiresTaskCompleted && cardSwipeTaskCompleted) {
            Label completedLabel = new Label("All tasks completed!");
            completedLabel.setStyle("-fx-font-size: 20;");

            Button exitButton = new Button("Exit");
            exitButton.setOnAction(e -> primaryStage.close());

            VBox root = new VBox(completedLabel, exitButton);
            root.setAlignment(Pos.CENTER);
            root.setSpacing(20);

            primaryStage.setScene(new Scene(root, 400, 300));
        } else {
            start(primaryStage);
        }
    }
}
