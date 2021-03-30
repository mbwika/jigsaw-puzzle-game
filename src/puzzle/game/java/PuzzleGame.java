package puzzle.game.java;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */

import java.text.DecimalFormat;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Rotate;
import javafx.stage.StageStyle;

/**
 * A sample in which an image is broken into pieces to create a jigsaw puzzle.
 *
 * @see javafx.scene.shape.Path
 * @see javafx.scene.image.Image
 * @see javafx.scene.image.ImageView
 * @see javafx.scene.control.Button
 * @see javafx.scene.layout.Pane
 * @see javafx.scene.input.MouseEvent
 * @see javafx.scene.effect.DropShadow
 * @resource PuzzlePieces-picture.jpg
 */
public class PuzzleGame extends javafx.application.Application {
    private Timeline timeline;
    private static BorderPane outerBorder;
    private Watch watch;

    
    @Override
    public void start(Stage puzzlerStage) throws Exception {
        Group root = new Group();
        root.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058);");
        outerBorder = new BorderPane(root);
        outerBorder.setStyle("-fx-background-image: url(\"puzzle/game/images/background.jpg\")");
        puzzlerStage.setScene(new Scene(outerBorder, 1120, 540, Color.GRAY));
        puzzlerStage.initStyle(StageStyle.DECORATED);
 
        puzzlerStage.show();

        watch = new Watch();
        myLayout();
        root.getChildren().add(watch);

        // load puzzle image
        Image image;
        image = new Image(("puzzle/game/images/army.PNG"), 800, 500, false, false);
        
        //number of columns = image width divided by size(width) of a piece(100px)
        //number of rows = image height divided by size(height) of a piece(100px)
        int numOfColumns = (int) (image.getWidth() / Piece.SIZE);
        int numOfRows = (int) (image.getHeight() / Piece.SIZE);
        
        //AnchorPane = Desk
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setVisible(true);
        
        // create desk where puzzle image rests
        final Desk desk = new Desk(numOfColumns, numOfRows);
        desk.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058); -fx-effect: dropshadow( gaussian , blurType(21.0,21.0,10.0) , 0,0,0), innershadow( three-pass-box , rgba(0,21.0,21.0,10.0) , 6, 0.0 , 0 , 2 )");
        
        // create puzzle pieces
        final List<Piece> pieces  = new ArrayList<>();
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = new Piece(image, x, y, row>0, col>0,
                        row<numOfRows -1, col < numOfColumns -1,
                        //anchorPane.getWidth(), anchorPane.getHeight());
                        desk.getWidth(), desk.getHeight());
                pieces.add(piece);
            }
        }
        //anchorPane.getChildren().addAll(pieces);
        desk.getChildren().addAll(pieces);
        
        // create button box
        Button shuffleButton = new Button("Launch Game");
        shuffleButton.setStyle("-fx-background-radius: 20; -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 ); -fx-font-size: 2em;");
        shuffleButton.setOnAction((ActionEvent actionEvent) -> {
 
            //Action onClicking shuffleButton
            if (timeline != null) timeline.stop();
            timeline = new Timeline();
            pieces.stream().map((piece) -> {
                piece.setActive();
                return piece;
            }).forEachOrdered((piece) -> {
                double shuffleX = Math.random() *
                        (desk.getWidth() - Piece.SIZE + 48f ) -
                        //(anchorPane.getWidth() - Piece.SIZE + 48f ) -
                        24f - piece.getCorrectX();
                double shuffleY = Math.random() *
                        (desk.getHeight() - Piece.SIZE + 30f ) -
                        //(anchorPane.getHeight() - Piece.SIZE + 30f ) -
                        15f - piece.getCorrectY();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(piece.translateXProperty(), shuffleX),
                                new KeyValue(piece.translateYProperty(), shuffleY)));
            });
            timeline.playFromStart();
            
            //</Action>
        }); //</Button>
        
        //Create Button solveButton
        Button solveButton = new Button("  Reset ");
        solveButton.setStyle("-fx-background-radius: 20; -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 ); -fx-font-size: 2em;");
        solveButton.setOnAction((ActionEvent actionEvent) -> {
            
            
            //Action onClicking solveButton
            if (timeline != null) timeline.stop();
            timeline = new Timeline();
            pieces.stream().map((piece) -> {
                piece.setInactive();
                return piece;
            }).forEachOrdered((piece) -> {
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(piece.translateXProperty(), 0),
                                new KeyValue(piece.translateYProperty(), 0)));
            });
            timeline.playFromStart();
        //</Action>
        }); 
        //</Button>
        
        //creating Exit Button
        Button exitBtn = new Button("Exit Puzzler");
        exitBtn.setStyle("-fx-background-radius: 20; -fx-background-color: linear-gradient(#5e5a5a, #807676); -fx-background-color: linear-gradient(#837e7e, #eee9e9); -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 ); -fx-font-size: 2em;");
        exitBtn.setOnAction((ActionEvent event) -> {
   
        ((Stage)((Button)event.getSource()).getScene().getWindow()).close();
    });
    
        //creating Minimize Button
        Button minBtn = new Button(" Minimize ");
        minBtn.setStyle("-fx-background-radius: 20; -fx-background-color: linear-gradient(#5e5a5a, #807676); -fx-background-color: linear-gradient(#837e7e, #eee9e9); -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 ); -fx-font-size: 2em;");
        minBtn.setOnAction((ActionEvent event) -> {
    
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    });
        
        //Instatiating Watch Class and calling method myLayout()
        watch = new Watch();
        myLayout();
        
        //Arranging the Stuff
        HBox watchs = new HBox(8);
        watchs.getChildren().add(watch); 
        HBox shuffleButtons = new HBox(8);
        shuffleButtons.getChildren().addAll(shuffleButton, solveButton);
        HBox solveButtons = new HBox(8);
        solveButtons.getChildren().addAll(exitBtn, minBtn);    
        VBox right = new VBox(10);
        right.getChildren().addAll(watchs,shuffleButtons, solveButtons);
        HBox buttons = new HBox(8);
        buttons.getChildren().addAll(desk,right); 
        //buttons.getChildren().addAll(anchorPane,right); 
        
        //Displaying arranged Stuff
        root.getChildren().addAll(buttons); 
           
        }
    //Creating Layout method for the Watch
        private void myLayout() {
        watch.setLayoutX(15);
        watch.setLayoutY(20);
        
    }
  

    /**
     * Node that represents the playing area/ desktop where the puzzle pieces sit
     */
    public static class Desk extends Pane {
        Desk(int numOfColumns, int numOfRows) {
            setStyle("-fx-background-color: #D2691E; " +
                    "-fx-border-color: #464646; " +
                    "-fx-effect: innershadow( two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 );");
            double DESK_WIDTH = Piece.SIZE * numOfColumns;
            double DESK_HEIGHT = Piece.SIZE * numOfRows;
            setPrefSize(DESK_WIDTH,DESK_HEIGHT);
            setMaxSize(DESK_WIDTH, DESK_HEIGHT);
            autosize();
            
            // create path for lines
            Path grid = new Path();
            grid.setStroke(Color.rgb(70, 70, 70));
            getChildren().add(grid);
            
            // create vertical lines
             for (int col = 0; col < numOfColumns - 1; col++) {
                 grid.getElements().addAll(
                     new MoveTo(Piece.SIZE + Piece.SIZE * col, 5),
                     new LineTo(Piece.SIZE + Piece.SIZE * col, Piece.SIZE * numOfRows - 5)
                 );
            }
             
            // create horizontal lines
            for (int row = 0; row < numOfRows - 1; row++) {
                 grid.getElements().addAll(
                     new MoveTo(5, Piece.SIZE + Piece.SIZE * row),
                     new LineTo(Piece.SIZE * numOfColumns - 5, Piece.SIZE + Piece.SIZE * row)
                 );
            }
        }

        private Desk() {
            throw new UnsupportedOperationException("Not supported yet."); 
            //To change body of generated methods, choose Tools | Templates.
        }
        @Override protected void layoutChildren() {}
    }

    /**
     * Node that represents a puzzle piece
     */
    public static class Piece extends Parent {
        public static final int SIZE = 100;
        private final double correctX;
        private final double correctY;
        private final boolean hasTopTab;
        private final boolean hasLeftTab;
        private final boolean hasBottomTab;
        private final boolean hasRightTab;
        private double startDragX;
        private double startDragY;
        private final Shape pieceStroke;
        private final Shape pieceClip;
        private final ImageView imageView = new ImageView();
        private Point2D dragAnchor;

        public Piece(Image image, final double correctX, final double correctY,
                     boolean topTab, boolean leftTab, boolean bottomTab, boolean rightTab,
                     final double deskWidth, final double deskHeight) {
            this.correctX = correctX;
            this.correctY = correctY;
            this.hasTopTab = topTab;
            this.hasLeftTab = leftTab;
            this.hasBottomTab = bottomTab;
            this.hasRightTab = rightTab;
            
            // configure clip
            pieceClip = createPiece();
            pieceClip.setFill(Color.WHITE);
            pieceClip.setStroke(null);
            
            // add a stroke
            pieceStroke = createPiece();
            pieceStroke.setFill(null);
            pieceStroke.setStroke(Color.CHOCOLATE);
            
            // create image view
            imageView.setImage(image);
            imageView.setClip(pieceClip);
            setFocusTraversable(true);
            getChildren().addAll(imageView, pieceStroke);
            
            // turn on caching so the jigsaw piece is fasr to draw when dragging
            setCache(true);
            
            // start in inactive state
            setInactive();
            
            // add listeners to support dragging
            setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent me) {
                    toFront();
                    startDragX = getTranslateX();
                    startDragY = getTranslateY();
                    dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent me) {
                    if (getTranslateX() < (10) && getTranslateX() > (- 10) &&
                        getTranslateY() < (10) && getTranslateY() > (- 10)) {
                        setTranslateX(0);
                        setTranslateY(0);
                        setInactive();
                    }
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent me) {
                    double newTranslateX = startDragX
                                            + me.getSceneX() - dragAnchor.getX();
                    double newTranslateY = startDragY
                                            + me.getSceneY() - dragAnchor.getY();
                    double minTranslateX = - 45f - correctX;
                    double maxTranslateX = (deskWidth - Piece.SIZE + 50f ) - correctX;
                    double minTranslateY = - 30f - correctY;
                    double maxTranslateY = (deskHeight - Piece.SIZE + 70f ) - correctY;
                    if ((newTranslateX> minTranslateX ) &&
                            (newTranslateX< maxTranslateX) &&
                            (newTranslateY> minTranslateY) &&
                            (newTranslateY< maxTranslateY)) {
                        setTranslateX(newTranslateX);
                        setTranslateY(newTranslateY);
                    }
                }
            });
        }

        private Shape createPiece() {
            Shape shape = createPieceRectangle();
            if (hasRightTab) {
                shape = Shape.union(shape,
                        createPieceTab(69.5f, 0f, 10f, 17.5f, 50f, -12.5f, 11.5f,
                                25f, 56.25f, -14f, 6.25f, 56.25f, 14f, 6.25f));
            }
            if (hasBottomTab) {
                shape = Shape.union(shape,
                        createPieceTab(0f, 69.5f, 17.5f, 10f, -12.5f, 50f, 25f,
                                11f, -14f, 56.25f, 6.25f, 14f, 56.25f, 6.25f));
            }
            if (hasLeftTab) {
                shape = Shape.subtract(shape,
                        createPieceTab(-31f, 0f, 10f, 17.5f, -50f, -12.5f, 11f,
                                25f, -43.75f, -14f, 6.25f, -43.75f, 14f, 6.25f));
            }
            if (hasTopTab) {
                shape = Shape.subtract(shape,
                        createPieceTab(0f, -31f, 17.5f, 10f, -12.5f, -50f, 25f,
                                12.5f, -14f, -43.75f, 6.25f, 14f, -43.75f, 6.25f));
            }
            shape.setTranslateX(correctX);
            shape.setTranslateY(correctY);
            shape.setLayoutX(50f);
            shape.setLayoutY(50f);
            return shape;
        }

        private Rectangle createPieceRectangle() {
            Rectangle rec = new Rectangle();
            rec.setX(-50);
            rec.setY(-50);
            rec.setWidth(SIZE);
            rec.setHeight(SIZE);
            return rec;
        }

        private Shape createPieceTab(double eclipseCenterX, double eclipseCenterY, double eclipseRadiusX, double eclipseRadiusY,
                                     double rectangleX, double rectangleY, double rectangleWidth, double rectangleHeight,
                                     double circle1CenterX, double circle1CenterY, double circle1Radius,
                                     double circle2CenterX, double circle2CenterY, double circle2Radius) {
            Ellipse e = new Ellipse(eclipseCenterX, eclipseCenterY, eclipseRadiusX, eclipseRadiusY);
            Rectangle r = new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight);
            Shape tab = Shape.union(e, r);
            Circle c1 = new Circle(circle1CenterX, circle1CenterY, circle1Radius);
            tab = Shape.subtract(tab, c1);
            Circle c2 = new Circle(circle2CenterX, circle2CenterY, circle2Radius);
            tab = Shape.subtract(tab, c2);
            return tab;
        }

        public void setActive() {
            setDisable(false);
            setEffect(new DropShadow());
            toFront();
        }

        public void setInactive() {
            setEffect(null);
            setDisable(true);
            toBack();
        }

        public double getCorrectX() { return correctX; }

        public double getCorrectY() { return correctY; }
    }

 
    private class Watch extends Parent {
        //visual nodes
 
        private final Dial mainDial;
        private final Dial minutesDial;
        private final Dial tenthsDial;
        private final Group background = new Group();
        private final DigitalClock digitalClock = new DigitalClock();
        private final Button shuffleButton;
        private final Button solveButton;
        /**
         * The number of milliseconds which have elapsed while the stopwatch has
         * been running. That is, it is the total time kept on the stopwatch.
         */
        private int elapsedMillis = 0;
        /**
         * Keeps track of the amount of the clock time (CPU clock) when the
         * stopwatch run plunger was pressed, or when the last tick even occurred.
         * This is used to calculate the elapsed time delta.
         */
        private int lastClockTime = 0;
        private DecimalFormat twoPlaces = new DecimalFormat("00");
        private Timeline time = new Timeline();
 
        public Watch() {
            shuffleButton = new Button();
            shuffleButton.setStyle("-fx-background-color: #8cc700; -fx-background-color: #71a000");
            solveButton = new Button();
            solveButton.setStyle("-fx-background-color: #AA0000; -fx-background-color: #660000");
 
            mainDial = new Dial(117, true, 12, 60, Color.RED, true);
            minutesDial = new Dial(30, false, 12, 60, "minutes", Color.BLACK, false);
            tenthsDial = new Dial(30, false, 12, 60, "10ths", Color.BLACK, false);
 
            configureBackground();
            myLayout();
            configureListeners();
            configureTimeline();
            getChildren().addAll(background, minutesDial, tenthsDial, digitalClock, mainDial, solveButton, shuffleButton);
        }
 
        private void configureTimeline() {
            time.setCycleCount(Timeline.INDEFINITE);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(47), new EventHandler<ActionEvent>() {
 
                public void handle(ActionEvent event) {
                    calculate();
                }
            });
            time.getKeyFrames().add(keyFrame);
        }
 
        private void configureBackground() {
            ImageView imageView = new ImageView();
            Image image = new Image("puzzle/game/images/stopwatch.png");
            imageView.setImage(image);
 
            Circle circle1 = new Circle();
            circle1.setCenterX(140);
            circle1.setCenterY(140);
            circle1.setRadius(120);
            circle1.setFill(Color.TRANSPARENT);
            circle1.setStroke(Color.web("#0A0A0A"));
            circle1.setStrokeWidth(0.3);
 
            Circle circle2 = new Circle();
            circle2.setCenterX(140);
            circle2.setCenterY(140);
            circle2.setRadius(118);
            circle2.setFill(Color.TRANSPARENT);
            circle2.setStroke(Color.web("#0A0A0A"));
            circle2.setStrokeWidth(0.3);
 
            Circle circle3 = new Circle();
            circle3.setCenterX(140);
            circle3.setCenterY(140);
            circle3.setRadius(140);
            circle3.setFill(Color.TRANSPARENT);
            circle3.setStroke(Color.web("#818a89"));
            circle3.setStrokeWidth(1);
 
            Ellipse ellipse = new Ellipse(140, 95, 180, 95);
            Circle ellipseClip = new Circle(140, 140, 140);
            ellipse.setFill(Color.web("#535450"));
            ellipse.setStrokeWidth(0);
            GaussianBlur ellipseEffect = new GaussianBlur();
            ellipseEffect.setRadius(10);
            ellipse.setEffect(ellipseEffect);
            ellipse.setOpacity(0.1);
            ellipse.setClip(ellipseClip);
            background.getChildren().addAll(imageView, circle1, circle2, circle3, ellipse);
        }
 
        private void myLayout() {
            mainDial.setLayoutX(140);
            mainDial.setLayoutY(140);
 
            minutesDial.setLayoutX(100);
            minutesDial.setLayoutY(100);
 
            tenthsDial.setLayoutX(180);
            tenthsDial.setLayoutY(100);
 
            digitalClock.setLayoutX(79);
            digitalClock.setLayoutY(195);
 
            shuffleButton.setLayoutX(223);
            shuffleButton.setLayoutY(1);
            Rotate rotateRight = new Rotate(360 / 12);
            shuffleButton.getTransforms().add(rotateRight);
 
            solveButton.setLayoutX(59.5);
            solveButton.setLayoutY(0);
            Rotate rotateLeft = new Rotate(-360 / 12);
            solveButton.getTransforms().add(rotateLeft);
        }
 
        private void configureListeners() {
            shuffleButton.setOnMousePressed(new EventHandler<MouseEvent>() {
 
                public void handle(MouseEvent me) {
                    //startButton.moveDown();
                    me.consume();
                }
            });
 
            solveButton.setOnMousePressed(new EventHandler<MouseEvent>() {
 
                public void handle(MouseEvent me) {
                    //stopButton.moveDown();
                    me.consume();
                }
            });
 
            shuffleButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
 
                public void handle(MouseEvent me) {
                    //startButton.moveUp();
                    solveButton();
                    me.consume();
                }
            });
 
            solveButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
 
                public void handle(MouseEvent me) {
                    //stopButton.moveUp();
                    stopReset();
                    me.consume();
                }
            });
 
            shuffleButton.setOnMouseDragged(new EventHandler<MouseEvent>() {
 
                public void handle(MouseEvent me) {
                    me.consume();
                }
            });
 
            solveButton.setOnMouseDragged(new EventHandler<MouseEvent>() {
 
                public void handle(MouseEvent me) {
                    me.consume();
                }
            });
        }
 
        //MODEL
        private void calculate() {
            if (lastClockTime == 0) {
                lastClockTime = (int) System.currentTimeMillis();
            }
 
            int now = (int) System.currentTimeMillis();
            int delta = now - lastClockTime;
 
            elapsedMillis += delta;
 
            int tenths = (elapsedMillis / 10) % 100;
            int seconds = (elapsedMillis / 1000) % 60;
            int mins = (elapsedMillis / 60000) % 60;
 
            refreshTimeDisplay(mins, seconds, tenths);
 
            lastClockTime = now;
        }
 
        public void solveButton() {
            if (time.getStatus() != Animation.Status.STOPPED) {
                // if started, stop it
                time.stop();
                lastClockTime = 0;
            } else {
                // if stopped, restart
                time.play();
            }
        }
 
        public void stopReset() {
            if (time.getStatus() != Animation.Status.STOPPED) {
                // if started, stop it
                time.stop();
                lastClockTime = 0;
            } else {
                // if stopped, reset it
                lastClockTime = 0;
                elapsedMillis = 0;
                refreshTimeDisplay(0, 0, 0);
            }
        }
 
        private void refreshTimeDisplay(int mins, int seconds, int tenths) {
            double handAngle = ((360 / 60) * seconds);
            mainDial.setAngle(handAngle);
 
            double tenthsHandAngle = ((360 / 100.0) * tenths);
            tenthsDial.setAngle(tenthsHandAngle);
 
            double minutesHandAngle = ((360 / 60.0) * mins);
            minutesDial.setAngle(minutesHandAngle);
 
            String timeString = twoPlaces.format(mins) + ":" + twoPlaces.format(seconds) + "." + twoPlaces.format(tenths);
            digitalClock.refreshDigits(timeString);
        }
    }

    
    public static void main(String[] args){
        launch(args);
    }


}