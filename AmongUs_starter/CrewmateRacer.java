import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

// inner class
class CrewmateRacer extends Pane {

    /**
     *
     */
    private final Game2DClean game2dClean;
    private int racerPosX = 0;
    private int racerPosY = 0;

    private int lastRacerPosX = 0;
    private int lastRacerPosY = 0;

    private int lastBackgroundPosX = 0;
    private int lastBackgroundPosY = 0;

    private ImageView aPicView = null;

    public ImageView getaPicView() {
        return aPicView;
    }

    private boolean isMaster = true;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CrewmateRacer(Game2DClean game2dClean, boolean isMaster) {
        this.game2dClean = game2dClean;
        this.isMaster = isMaster;
        Image crewMateImage = new Image(Game2DClean.CREWMATE_IMAGE);
        if (isMaster) {

            aPicView = new ImageView(Game2DClean.CREWMATE_IMAGE);
            racerPosX = (int) ((this.game2dClean.scene.getWidth() / 2) - (crewMateImage.getWidth() / 2));
            aPicView.setX(racerPosX); // (int)(root.getWidth()/2);

            this.racerPosY = (int) ((this.game2dClean.scene.getHeight() / 2) - (crewMateImage.getHeight() / 2));
            aPicView.setY(racerPosY); // (int)(root.getHeight()/2);

        } else
            aPicView = new ImageView(Game2DClean.CREWMATE_RUNNERS);

        racerPosX += 200;
        racerPosY += 200;
        this.getChildren().add(aPicView);
    }

    public CrewmateRacer(Game2DClean game2dClean, double posX, double posY) {
        this.game2dClean = game2dClean;
        aPicView = new ImageView(Game2DClean.CREWMATE_RUNNERS);

        racerPosX += posX;
        racerPosY += posY;
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

        this.game2dClean.movableBackground.update();

        double speed = 10;

        int backgroundPosX = (int) this.game2dClean.movableBackground.getTranslateX();
        int backgroundPosY = (int) this.game2dClean.movableBackground.getTranslateY();

        // if (this.checkCollision(racerPosX, racerPosY) ||
        // this.checkCollision(racerPosX + 60, racerPosY) ||
        // this.checkCollision(racerPosX + 60, racerPosY + 130) ||
        // this.checkCollision(racerPosX, racerPosY + 130)) {

        if (this.game2dClean.moveDown && this.checkCollision(racerPosX + 60, racerPosY + 130)) {
            racerPosY -= speed;
            this.game2dClean.moveDown = false;
            backgroundPosY = lastBackgroundPosY;

        } else if (this.game2dClean.moveUP && this.checkCollision(racerPosX, racerPosY)) {
            racerPosY += speed;
            this.game2dClean.moveUP = false;
            backgroundPosY = lastBackgroundPosY;

        } else if (this.game2dClean.moveLeft && this.checkCollision(racerPosX, racerPosY)) {
            racerPosX += speed;
            this.game2dClean.moveLeft = false;
            backgroundPosX = lastBackgroundPosX;

        } else if (this.game2dClean.moveRight && this.checkCollision(racerPosX + 60, racerPosY)) {
            racerPosX -= speed;
            this.game2dClean.moveRight = false;
            backgroundPosX = lastBackgroundPosX;

            // }

        } else {
            lastRacerPosX = racerPosX;
            lastRacerPosY = racerPosY;
            lastBackgroundPosY = backgroundPosY;
            lastBackgroundPosX = backgroundPosX;

        }

        if (this.game2dClean.moveDown) {
            racerPosY += speed;
            backgroundPosY -= speed;

        }
        if (this.game2dClean.moveUP) {
            racerPosY -= speed;
            backgroundPosY += speed;

        }
        if (this.game2dClean.moveLeft) {
            racerPosX -= speed;
            backgroundPosX += speed;

        }
        if (this.game2dClean.moveRight) {
            racerPosX += speed;
            backgroundPosX -= speed;

        }

        this.game2dClean.movableBackground.setTranslateX(backgroundPosX);
        this.game2dClean.movableBackground.setTranslateY(backgroundPosY);

        return new Point2D(racerPosX, racerPosY);
    }

    private boolean checkCollision(int x, int y) {
        Color color = this.game2dClean.backgroundCollision.getPixelReader().getColor(x,
                y);
        return color.getRed() >= 0.8 && color.getGreen() < 0.5;
    }

}