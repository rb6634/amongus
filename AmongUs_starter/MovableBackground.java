/*
 * @Instructor: Alan Mutka
 * @author: Rinor Bugujevci, Uejs Hamja
 * Project: Among us
 * @version: 28/04/2023
 * ISTE: 121.801
 */

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

// background
class MovableBackground extends Pane {

   private int racerPosX = 0;
   private int racerPosY = 0;
   private ImageView aPicView = null;
   private ImageView map = null;

   public MovableBackground() {
      map = new ImageView(Game2DClean.BACKGROUND_IMAGE);
      aPicView = new ImageView(Game2DClean.RGB_MAP);
      this.getChildren().add(aPicView);
      this.getChildren().add(map);
   }

   public Point2D update() {

      return new Point2D(racerPosX, racerPosY);
   }
}