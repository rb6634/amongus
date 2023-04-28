
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AmongUsTasks extends Application {

    // Booleanvariables to keep track of completed tasks
    private boolean wiresTaskCompleted = false;
    private boolean alignEnginesTaskCompleted = false;
    private boolean sortSamplesTaskCompleted = false;
    private boolean memoryPuzzleTaskCompleted = false;

    // A list that keeps track of revealed buttons for the memory puzzle task
    private List<Button> revealedButtons = new ArrayList<>();
    // counter for the mached buttons in the memory puzzle
    private int matchedPairs = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // GUI elements for the main menu
        primaryStage.setTitle("Among Us Tasks");

        Label lblTitle = new Label("Among Us - Tasks");
        lblTitle.setStyle("-fx-font-size: 23;");

        Button btnWires = new Button("Fix Wires");
        btnWires.setOnAction(e -> fixWires(primaryStage));

        Button btnEngines = new Button("Align Engines");
        btnEngines.setOnAction(e -> alignEngines(primaryStage));

        Button btnSort = new Button("Sort Samples");
        btnSort.setOnAction(e -> sortSamples(primaryStage));

        Button btnPuzzle = new Button("Memory Puzzle");
        btnPuzzle.setOnAction(e -> memoryPuzzle(primaryStage));

        VBox root = new VBox(lblTitle, btnWires, btnEngines, btnSort, btnPuzzle);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // Creating the Fix Wires Task
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

    // Creating the Align Engines task
    private void alignEngines(Stage primaryStage) {
        Label instructionLabel = new Label(
                "Align the engines by setting the slider to the correct value: (It's not 50 :) )");

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

    // Create the Sort Samples task
    private void sortSamples(Stage primaryStage) {
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

        primaryStage.setScene(new Scene(root, 400, 300));
    }

    // Creating the Memory Puzzle task
    private void memoryPuzzle(Stage primaryStage) {
        Label instructionLabel = new Label("Match the buttons with the same color:");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);

        // Creating a list of colors and shuffeling it
        List<String> colors = Arrays.asList("red", "red", "blue", "blue", "green", "green", "yellow", "yellow");
        Collections.shuffle(colors);

        // Create buttons for each color and add them to the grid
        for (int i = 0; i < 8; i++) {
            Button button = new Button("?");
            button.setStyle("-fx-base: lightgrey;");
            button.setUserData(colors.get(i));
            button.setMinSize(60, 60);
            button.setMaxSize(60, 60);
            //
            int col = i % 4;
            int row = i / 4;

            // when the button is clicked
            button.setOnAction(e -> {
                button.setStyle("-fx-base: " + button.getUserData() + ";");
                button.setText("");

                revealedButtons.add(button);

                // Checking if two buttons are clicked and if they have the same color
                if (revealedButtons.size() == 2) {
                    if (revealedButtons.get(0).getUserData().equals(revealedButtons.get(1).getUserData())) {
                        matchedPairs++;
                        revealedButtons.clear();

                        if (matchedPairs == 4) {
                            memoryPuzzleTaskCompleted = true;
                            checkTasksCompleted(primaryStage);
                        }
                    } else {
                        // if the buttons are different colors disable them again
                        Button firstButton = revealedButtons.get(0);
                        Button secondButton = revealedButtons.get(1);
                        firstButton.setDisable(true);
                        secondButton.setDisable(true);
                        //

                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            javafx.application.Platform.runLater(() -> {
                                firstButton.setStyle("-fx-base: lightgrey;");
                                firstButton.setText("?");
                                firstButton.setDisable(false);
                                secondButton.setStyle("-fx-base: lightgrey;");
                                secondButton.setText("?");
                                secondButton.setDisable(false);
                                revealedButtons.clear();
                            });
                        }).start();
                    }
                }
            });
            grid.add(button, col, row);
        }
        VBox root = new VBox(instructionLabel, grid);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        primaryStage.setScene(new Scene(root, 400, 300));
    }

    // Checking if all the tasks have been completed
    private void checkTasksCompleted(Stage primaryStage) {
        if (wiresTaskCompleted && alignEnginesTaskCompleted && sortSamplesTaskCompleted && memoryPuzzleTaskCompleted) {
            Label completedLabel = new Label("Congratulations! All tasks completed!");
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

    // show alert method
    private void showAlert(String message) {
        Stage alerStage = new Stage();
        alerStage.setTitle("Alert");

        Label lblAlert = new Label(message);
        Button btnClose = new Button("Close");
        btnClose.setOnAction(e -> alerStage.close());

        VBox root = new VBox(lblAlert, btnClose);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);

        alerStage.setScene(new Scene(root, 300, 200));
        alerStage.show();
    }

}
