
/**
 * @author: Uejsi Hamja
 */
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AmongUsTasks extends Application {

    private boolean wiresTaskCompleted = false;
    private boolean cardSwipeTaskCompleted = false;
    private boolean alignEnginesTaskCompleted = false;
    private boolean sortSamplesTaskCompleted = false;

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

        Button btnEngines = new Button("Align Engines");
        btnEngines.setOnAction(e -> alignEngines(primaryStage));

        Button btnSort = new Button("Sort Samples");
        btnSort.setOnAction(e -> sortSamples(primaryStage));

        VBox root = new VBox(lblTitle, btnWires, btnCard, btnEngines, btnSort);
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

    private void alignEngines(Stage primaryStage) {
        Label instructionLabel = new Label("Align the engines by setting the slider to the correct value: (It's not 50 :) )");

        Slider slider = new Slider(0, 100, 0);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(e -> {
            int correctValue = 50;
            int sliderValue = (int) slider.getValue();
            if (sliderValue == correctValue) {
                alignEnginesTaskCompleted = true;
                checkTasksCompleted(primaryStage);
            } else {
                 showAlert("Incorrect slider value. Try again.");
            }
        });

        VBox root = new VBox(instructionLabel, slider, btnSubmit);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);

        primaryStage.setScene(new Scene(root, 400, 300));
    }

    private void sortSamples(Stage primaryStage){
        Label instructionLabel = new Label("Sort the samples into the correct categories:");
        Label lblSample = new Label();

        List<String> samples = new ArrayList<>(Arrays.asList("Leaf", "Rock", "Wood", "Metal"));
        Collections.shuffle(samples);
        AtomicInteger sampleIndex = new AtomicInteger(0);
        lblSample.setText(samples.get(sampleIndex.get()));

        Button btnOrganic = new Button("Organic");
        btnOrganic.setOnAction(e -> {
            if (lblSample.getText().equals("Leaf") || lblSample.getText().equals("Wood")) {
                sampleIndex.incrementAndGet();
                if (sampleIndex.get() == samples.size()) {
                    sortSamplesTaskCompleted = true;
                    checkTasksCompleted(primaryStage);
                } else {
                    lblSample.setText(samples.get(sampleIndex.get()));
                }
            } else {
                showAlert("Incorrect category. Try again.");
            }
        });

        Button btnInorganic = new Button("Inorganic");
        btnInorganic.setOnAction(e -> {
            if (lblSample.getText().equals("Rock") || lblSample.getText().equals("Metal")) {
                sampleIndex.incrementAndGet();
                if (sampleIndex.get() == samples.size()) {
                    sortSamplesTaskCompleted = true;
                    checkTasksCompleted(primaryStage);
                } else {
                    lblSample.setText(samples.get(sampleIndex.get()));
                }
            } else {
                showAlert("Incorrect category. Try again.");
            }
        });

        HBox buttonBox = new HBox(btnOrganic, btnInorganic);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(15);

        VBox root = new VBox(instructionLabel, lblSample, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);

        primaryStage.setScene(new Scene(root,400, 300));
    }

    private void checkTasksCompleted(Stage primaryStage) {
        if (wiresTaskCompleted && cardSwipeTaskCompleted && alignEnginesTaskCompleted && sortSamplesTaskCompleted) {
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

    //show alert method
    private void showAlert(String message){
        Stage alerStage = new Stage();
        alerStage.setTitle("Alert");

        Label lblAlert = new Label(message);
        Button btnClose = new Button("Close");
        btnClose.setOnAction(e -> alerStage.close());

        VBox root = new VBox(lblAlert,btnClose);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);

        alerStage.setScene(new Scene(root,300,200));
        alerStage.show();
    }
}
