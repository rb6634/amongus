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

      initializeScene();

   }

   // start the game scene
   public void initializeScene() {

      masterCrewmate = new CrewmateRacer(true);
      for (int i = 0; i < 5; i++) {
         CrewmateRacer cR = new CrewmateRacer(false);
         robotCrewmates.add(cR);
      }
      // create background
      movableBackground = new MovableBackground();

      // add background
      this.root.getChildren().add(movableBackground);
      // add to the root
      this.root.getChildren().add(masterCrewmate);
      this.root.getChildren().addAll(robotCrewmates);

      // display the window
      scene = new Scene(root, 800, 500);
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

      timer = new AnimationTimer() {
         @Override
         public void handle(long now) {
            masterCrewmate.update();

            for (int i = 0; i < robotCrewmates.size(); i++) {
               robotCrewmates.get(i).update();
            }

            movableBackground.update();
         }
      };
      timer.start();
   }

   // inner class
   class CrewmateRacer extends Pane {
      private int racerPosX = 0;
      private int racerPosY = 0;
      private ImageView aPicView = null;
      private boolean isMaster = true;

      public CrewmateRacer(boolean isMaster) {
         this.isMaster = isMaster;
         if (isMaster) {
            aPicView = new ImageView(CREWMATE_IMAGE);
            racerPosX = (int) (400 - aPicView.getFitWidth() / 2);
            aPicView.setTranslateX(racerPosX); // (int)(root.getWidth()/2);
            racerPosY = (int) (250 - aPicView.getFitHeight() / 2);
            aPicView.setTranslateY(racerPosY); // (int)(root.getHeight()/2);
         } else
            aPicView = new ImageView(CREWMATE_RUNNERS);
         this.getChildren().add(aPicView);
      }

      public void update() {

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

      }
   }

   // background
   class MovableBackground extends Pane {
      private int racerPosX = 100;
      private int racerPosY = -100;
      private int integerPosX = 0;
      private int integerPosY = 0;
      private ImageView aPicView = null;
      private ImageView map = null;

      public MovableBackground() {
         aPicView = new ImageView(RGB_MAP);
         this.getChildren().add(aPicView);
         map = new ImageView(BACKGROUND_IMAGE);
         this.getChildren().add(map);
      }

      public void update() {
         double speed = 10;
         if (moveDown) 
            racerPosY -= speed;
         if (moveUP)
            racerPosY += speed;
         if (moveLeft)
            racerPosX += speed;
         if (moveRight)
            racerPosX -= speed;
         integerPosX = 400 - racerPosX;
         integerPosY = 250 - racerPosY;
         Color color = backgroundCollision.getPixelReader().getColor(integerPosX, integerPosY);
         System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());

         if (color.getGreen() < 0.1) {
            System.out.println("collesion");
            if (moveDown)
               racerPosY += speed;
            else if (moveUP)
               racerPosY -= speed;
            else if (moveLeft)
               racerPosX -= speed;
            else if (moveRight)
               racerPosX += speed;

         }

         aPicView.setTranslateX(racerPosX);
         aPicView.setTranslateY(racerPosY);
         map.setTranslateX(racerPosX);
         map.setTranslateY(racerPosY);
      }
   }

} // end class Races rinor