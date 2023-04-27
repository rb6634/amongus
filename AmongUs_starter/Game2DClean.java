import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * AmongUSStarter with JavaFX and Threads
 * Loading imposters
 * Loading background
 * Control actors and backgrounds
 * Create many number of imposters - random controlled
 * RGB based collision
 * Collsion between two imposters
 */

public class Game2DClean extends Application implements EventHandler<ActionEvent> {

   // Window attributes
   private Stage stage;
   Scene scene;
   private StackPane root;
   private static final String IP_ADRESS = "127.0.0.1";
   private static final int SERVER_PORT = 2000;
   private ObjectOutputStream oos = null;
   private ObjectInputStream ois = null;
   private CrewmateRacer onlinePlayer = null;
   private boolean connected = false;

   private static String[] args;

   final static String CREWMATE_IMAGE = "ply1.png"; // file with icon for a racer
   final static String CREWMATE_RUNNERS = "runner1.png"; // file with icon for a racer
   final static String BACKGROUND_IMAGE = "fullmap.png";
   //
   final static String RGB_MAP = "mapcolor.png"; // rgb based map that we will use for collision

   private TextField chatInputField;
   private Button sendButton;
   private ListView<String> chatListView;
   private Button btnTask1 = null;
   private Button btnTask2 = null;
   private Button btnTask3 = null;
   private Button btnTask4 = null;
   private TextField tfPlayerName;
   private Button btnLogin = null;

   // crewmates
   CrewmateRacer masterCrewmate = null;
   List<CrewmateRacer> robotCrewmates = new ArrayList<>();

   // movable background
   MovableBackground movableBackground = null;

   // Animation timer
   AnimationTimer timer = null;
   int counter = 0;
   boolean moveUP = false, moveDown = false, moveRight = false, moveLeft = false;

   // background detection/collision
   Image backgroundCollision = null;

   protected String name;

   // main program
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

   // start() method, called via launch
   public void start(Stage _stage) {
      // stage seteup
      stage = _stage;
      stage.setTitle("Game2D Starter");
      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });

      // root pane
      root = new StackPane();
      this.root.setAlignment(Pos.TOP_LEFT);

      startingScreen();

      // initializeScene();

   }

   public void startingScreen() {

      VBox login = new VBox();
      login.setAlignment(Pos.CENTER);
      login.setSpacing(10);
      scene = new Scene(login, 800, 500);
      tfPlayerName = new TextField();
      btnLogin = new Button("Log in");
      login.getChildren().addAll(tfPlayerName, btnLogin);
      stage.setScene(scene);
      stage.show();
      btnLogin.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
            // TODO Auto-generated method stub
            name = tfPlayerName.getText();
            initializeScene();
         }

      });

   }

   // start the game scene
   public void initializeScene() {

      scene = new Scene(root, 800, 500);

      masterCrewmate = new CrewmateRacer(this, true);
      // onlinePlayer = new CrewmateRacer(false);
      // create background
      movableBackground = new MovableBackground();

      movableBackground.setTranslateX(-200);
      movableBackground.setTranslateY(-200);

      // add background
      this.root.getChildren().add(movableBackground);
      for (int i = 0; i < 5; i++) {
         CrewmateRacer cR = new CrewmateRacer(this, false);
         robotCrewmates.add(cR);
         this.root.getChildren().addAll((cR));
      }
      // add to the root
      this.root.getChildren().add(masterCrewmate);
      // this.root.getChildren().addAll(robotCrewmates);

      // display the window
      // Add chat input field and send button
      chatInputField = new TextField();
      chatInputField.setPromptText("Type your message...");
      chatInputField.setPrefWidth(100);
      chatInputField.setPrefHeight(100);
      chatInputField.setTranslateX(20);
      chatInputField.setTranslateY(430);

      sendButton = new Button("Send Message");
      btnTask1 = new Button("Task 1");
      btnTask2 = new Button("Task 2");
      btnTask3 = new Button("Task 3");
      btnTask4 = new Button("Click to complete the tasks!");
      sendButton.setTranslateX(330);
      sendButton.setTranslateY(430);

      // Add chat input field and send button to the root

      this.root.getChildren().addAll(chatInputField, sendButton, btnTask1, btnTask2, btnTask3, btnTask4);
      chatInputField.setVisible(false);
      btnTask1.setOnAction(this);
      btnTask2.setOnAction(this);
      btnTask3.setOnAction(this);
      btnTask4.setOnAction(this);

      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

      sendButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            if (chatInputField.isVisible()) {
               sendChatMessage();
               chatInputField.setVisible(false);
            } else
               chatInputField.setVisible(true);

         }

         private void sendChatMessage() {
            try {
               oos.writeObject(new ChatMessage(name, chatInputField.getText()));
               oos.flush();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      });

      // chatInputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      // @Override
      // public void handle(KeyEvent event) {
      // if (event.getCode() == KeyCode.ENTER) {
      // sendChatMessage();
      // }
      // }

      // private void sendChatMessage() {
      // }
      // });

      // chatInputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      // @Override
      // public void handle(KeyEvent event) {
      // if (event.getCode() == KeyCode.ENTER) {
      // sendChatMessage();
      // }
      // }

      // private void sendChatMessage() {
      // try {
      // oos.writeUTF(chatInputField.getText());
      // } catch (IOException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }
      // }
      // });

      // KEYBOARD CONTROL
      scene.setOnKeyPressed(
            new EventHandler<KeyEvent>() {
               @Override
               public void handle(KeyEvent event) {
                  switch (event.getCode()) {
                     case UP:
                        moveUP = true;
                        break;
                     case DOWN:
                        moveDown = true;
                        break;
                     case LEFT:
                        moveLeft = true;
                        break;
                     case RIGHT:
                        moveRight = true;
                        break;
                  }

               }
            });

      scene.setOnKeyReleased(
            new EventHandler<KeyEvent>() {
               @Override
               public void handle(KeyEvent event) {
                  switch (event.getCode()) {
                     case UP:
                        moveUP = false;
                        break;
                     case DOWN:
                        moveDown = false;
                        break;
                     case LEFT:
                        moveLeft = false;
                        break;
                     case RIGHT:
                        moveRight = false;
                        break;
                  }

               }
            });

      // background collision
      backgroundCollision = new Image(RGB_MAP);// MASK IMAGE

      try {
         Socket socket = new Socket(IP_ADRESS, SERVER_PORT);
         oos = new ObjectOutputStream(socket.getOutputStream());
         ois = new ObjectInputStream(socket.getInputStream());
         connected = true;
         oos.writeObject("getindex");
         oos.flush();
      } catch (UnknownHostException e) {
         connected = false;
         e.printStackTrace();
      } catch (IOException e) {
         connected = false;
         e.printStackTrace();
      }

      timer = new AnimationTimer() {
         @Override
         public void handle(long now) {

            Point2D location = masterCrewmate.update();

            if (connected) {

               try {
                  oos.writeObject(new playerLocation((int) location.getX(), (int) location.getY()));
                  oos.flush();

               } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }

         }

      };
      timer.start();

      ////
      ////////
      Game2DClean game = this;
      ////
      ////
      ////

      Thread serverRecieveThread = new Thread() {

         @Override
         public void run() {

            while (true) {

               try {
                  Object obj = ois.readObject();

                  if (obj instanceof playerLocation) {

                     playerLocation otherPlayerLocation = (playerLocation) obj;
                     System.out.println("Other players coords: " + otherPlayerLocation.getX() + " "
                           + otherPlayerLocation.getY());

                     //////
                     /////
                     /////
                     ////
                     try {

                        // otherPlayerLocation getIndex() is always zero?????
                        CrewmateRacer racer = robotCrewmates.get(1);
                        racer.getaPicView().setTranslateX(otherPlayerLocation.getX());
                        racer.getaPicView().setTranslateY(otherPlayerLocation.getY());

                        System.out.println("Other index: " + otherPlayerLocation.getIndex());

                        racer.setTranslateX(otherPlayerLocation.getX());
                        racer.setTranslateY(otherPlayerLocation.getY());

                     } catch (Exception e) {
                        CrewmateRacer racer2 = new CrewmateRacer(game, otherPlayerLocation.getX(),
                              otherPlayerLocation.getY());
                        robotCrewmates.add(racer2);
                     }

                  }
                  if (obj instanceof ChatMessage) {

                     ChatMessage message = (ChatMessage) obj;
                     Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                           Alert alert = new Alert(AlertType.INFORMATION,
                                 message.getSender() + ": " + message.getMessage());
                           alert.setHeaderText("You got a new chat!");
                           alert.showAndWait();
                        }

                     });
                     System.out.println(message.getSender() + ": " + message.getMessage());

                  }
               } catch (ClassNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               } catch (IOException e) {
                  // TODO Auto-generated catch blom
                  e.printStackTrace();
               }
            }
         }

      };

      serverRecieveThread.start();

   }

   @Override
   public void handle(ActionEvent event) {
      Object obj = event.getSource();
      if (obj instanceof Button) {
         Button button = (Button) obj;

         switch (button.getText()) {
            case "Click to complete the tasks!":
               AmongUsTasks amongus = new AmongUsTasks();
               Stage tasks = new Stage();
               amongus.start(tasks);

               break;

            default:
               break;
         }
      }
   }

} // end class Races
