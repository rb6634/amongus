import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import javafx.event.EventHandler;
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

public class Game2DClean extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private StackPane root;
   private static final String IP_ADRESS = "127.0.0.1";
   private static final int SERVER_PORT = 2000;
   private ObjectOutputStream oos = null;
   private ObjectInputStream ois = null;
   private CrewmateRacer onlinePlayer = null;

   private static String[] args;

   private final static String CREWMATE_IMAGE = "ply1.png"; // file with icon for a racer
   private final static String CREWMATE_RUNNERS = "runner1.png"; // file with icon for a racer
   private final static String BACKGROUND_IMAGE = "fullmap.png";
   //
   private final static String RGB_MAP = "mapcolor.png"; // rgb based map that we will use for collision

   // crewmates
   CrewmateRacer masterCrewmate = null;
   ArrayList<CrewmateRacer> robotCrewmates = new ArrayList<>();

   // movable background
   MovableBackground movableBackground = null;

   // Animation timer
   AnimationTimer timer = null;
   int counter = 0;
   boolean moveUP = false, moveDown = false, moveRight = false, moveLeft = false;

   // background detection/collision
   Image backgroundCollision = null;

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

      initializeScene();

   }

   // start the game scene
   public void initializeScene() {

      scene = new Scene(root, 800, 500);

      masterCrewmate = new CrewmateRacer(true);
      // onlinePlayer = new CrewmateRacer(false);
      for (int i = 0; i < 5; i++) {
         CrewmateRacer cR = new CrewmateRacer(false);
         robotCrewmates.add(cR);
      }
      // create background
      movableBackground = new MovableBackground();

      movableBackground.setTranslateX(-200);
      movableBackground.setTranslateY(-200);

      // add background
      this.root.getChildren().add(movableBackground);
      // add to the root
      this.root.getChildren().add(masterCrewmate);
      // this.root.getChildren().addAll(robotCrewmates);

      // display the window

      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

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
         oos.writeObject("getIndex");
         oos.flush();
      } catch (UnknownHostException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      timer = new AnimationTimer() {
         @Override
         public void handle(long now) {
            // masterCrewmate.update();
            // masterCrewmate.update();

            Point2D location = masterCrewmate.update();

            if (moveUP || moveDown || moveLeft || moveRight) {
               // if (connected) {

               try {
                  oos.writeObject(new playerLocation((int) location.getX(), (int) location.getY()));
                  oos.flush();
                  try {
                     Object obj = ois.readObject();

                     if (obj instanceof playerLocation) {

                        playerLocation otherPlayerLocation = (playerLocation) obj;
                        System.out.println(

                              "Other players coords: " + otherPlayerLocation.getX() + " " + otherPlayerLocation.getY());
                     }
                  } catch (ClassNotFoundException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
               // }
            }

            // for (int i = 0; i < robotCrewmates.size(); i++) {
            // robotCrewmates.get(i).update();
            // }
         }
      };
      timer.start();
   }

   // inner class
   class CrewmateRacer extends Pane {

      private int racerPosX = 0;
      private int racerPosY = 0;

      private int lastRacerPosX = 0;
      private int lastRacerPosY = 0;

      private int lastBackgroundPosX = 0;
      private int lastBackgroundPosY = 0;

      private ImageView aPicView = null;
      private boolean isMaster = true;
      private int index;

      public int getIndex() {
         return index;
      }

      public void setIndex(int index) {
         this.index = index;
      }

      public CrewmateRacer(boolean isMaster) {
         this.isMaster = isMaster;
         Image crewMateImage = new Image(CREWMATE_IMAGE);
         if (isMaster) {

            aPicView = new ImageView(CREWMATE_IMAGE);
            racerPosX = (int) ((scene.getWidth() / 2) - (crewMateImage.getWidth() / 2));
            aPicView.setX(racerPosX); // (int)(root.getWidth()/2);

            this.racerPosY = (int) ((scene.getHeight() / 2) - (crewMateImage.getHeight() / 2));
            aPicView.setY(racerPosY); // (int)(root.getHeight()/2);

         } else
            aPicView = new ImageView(CREWMATE_RUNNERS);

         racerPosX += 200;
         racerPosY += 200;
         this.getChildren().add(aPicView);
      }

      public Point2D update() {

         // double speed = 10;

         // if(isMaster){//MASTER CONTROL

         // //get pixel

         // Color color = backgroundCollision.getPixelReader().getColor(racerPosX,
         // racerPosY);
         // //System.out.println(color.getRed() + " " + color.getGreen() + " " +
         // color.getBlue());

         // //get distance
         // int targetX=0;
         // int targetY = 0;

         // double dist = Math.sqrt( Math.pow(racerPosX-targetX,2)
         // + Math.pow(racerPosY-targetY,2));
         // System.out.println(dist);

         // if(color.getRed()>0.6){
         // speed=10;
         // }
         // else speed=2;

         // if(moveDown) racerPosY+=speed;
         // if(moveUP) racerPosY-=speed;
         // if(moveLeft) racerPosX-=speed;
         // if(moveRight) racerPosX+=speed;
         // }
         // else{//ALL THE OTHERS
         // racerPosX += Math.random()*speed;
         // racerPosY += (Math.random()-0.2)*speed;
         // }

         // // aPicView.setTranslateX(racerPosX);
         // // aPicView.setTranslateY(racerPosY);

         // if(racerPosX>root.getWidth()) racerPosX=0;
         // if(racerPosY>root.getHeight()) racerPosY=0;
         // if(racerPosX<0) racerPosX=0;
         // if(racerPosY<0) racerPosY=0;

         movableBackground.update();

         double speed = 10;

         int backgroundPosX = (int) movableBackground.getTranslateX();
         int backgroundPosY = (int) movableBackground.getTranslateY();

         if (this.checkCollision(racerPosX, racerPosY) ||
               this.checkCollision(racerPosX + 60, racerPosY) ||
               this.checkCollision(racerPosX + 60, racerPosY + 130) ||
               this.checkCollision(racerPosX, racerPosY + 130)) {
            System.out.println("collesion");

            if (moveDown) {
               racerPosY -= speed;
               moveDown = false;
               backgroundPosY = lastBackgroundPosY;

            } else if (moveUP) {
               racerPosY += speed;
               moveUP = false;
               backgroundPosY = lastBackgroundPosY;

            } else if (moveLeft) {
               racerPosX += speed;
               moveLeft = false;
               backgroundPosX = lastBackgroundPosX;

            } else if (moveRight) {
               racerPosX -= speed;
               moveRight = false;
               backgroundPosX = lastBackgroundPosX;

            }

         } else {

            lastRacerPosX = racerPosX;
            lastRacerPosY = racerPosY;
            lastBackgroundPosY = backgroundPosY;
            lastBackgroundPosX = backgroundPosX;

         }

         if (moveDown) {
            racerPosY += speed;
            backgroundPosY -= speed;

         }
         if (moveUP) {
            racerPosY -= speed;
            backgroundPosY += speed;

         }
         if (moveLeft) {
            racerPosX -= speed;
            backgroundPosX += speed;

         }
         if (moveRight) {
            racerPosX += speed;
            backgroundPosX -= speed;

         }

         movableBackground.setTranslateX(backgroundPosX);
         movableBackground.setTranslateY(backgroundPosY);

         return new Point2D(racerPosX, racerPosY);
      }

      private boolean checkCollision(int x, int y) {
         Color color = backgroundCollision.getPixelReader().getColor(x,
               y);
         return color.getRed() >= 0.8 && color.getGreen() < 0.5;
      }

   }

   // background
   class MovableBackground extends Pane {

      private int racerPosX = 0;
      private int racerPosY = 0;
      private ImageView aPicView = null;
      private ImageView map = null;

      public MovableBackground() {
         map = new ImageView(BACKGROUND_IMAGE);
         aPicView = new ImageView(RGB_MAP);
         this.getChildren().add(aPicView);
         this.getChildren().add(map);
      }

      public Point2D update() {

         return new Point2D(racerPosX, racerPosY);
      }
   }

} // end class Races
