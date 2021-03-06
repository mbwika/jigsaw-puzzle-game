/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.game.java;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class Buttons extends Parent {
        private final Color colorWeak;
        private final Color colorStrong;
        private final Rectangle rectangleSmall = new Rectangle(14, 7);
        private final Rectangle rectangleBig = new Rectangle(28, 5);
        private final Rectangle rectangleWatch = new Rectangle(24, 14);
        private final Rectangle rectangleVisual = new Rectangle(28, 7 + 5 + 14);
 
        Buttons(Color colorWeak, Color colorStrong) {
            this.colorStrong = colorStrong;
            this.colorWeak = colorWeak;
            configureDesign();
            setCursor(Cursor.HAND);
            getChildren().addAll(rectangleVisual, rectangleSmall, rectangleBig, rectangleWatch);
        }

 
        private void configureDesign() {
            rectangleVisual.setLayoutY(0f);
            rectangleVisual.setLayoutX(-14);
            rectangleVisual.setFill(Color.TRANSPARENT);
 
            rectangleSmall.setLayoutX(-7);
            rectangleSmall.setLayoutY(5);
            rectangleSmall.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[]{
                        new Stop(0, colorWeak),
                        new Stop(0.5, colorStrong),
                        new Stop(1, colorWeak)}));
 
            rectangleBig.setLayoutX(-14);
            rectangleBig.setLayoutY(0);
            rectangleBig.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[]{
                        new Stop(0, colorStrong),
                        new Stop(0.5, colorWeak),
                        new Stop(1, colorStrong)}));
 
            rectangleWatch.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[]{
                        new Stop(0, Color.web("#4e605f")),
                        new Stop(0.2, Color.web("#c3d6d5")),
                        new Stop(0.5, Color.web("#f9ffff")),
                        new Stop(0.8, Color.web("#c3d6d5")),
                        new Stop(1, Color.web("#4e605f"))}));
            rectangleWatch.setLayoutX(-12);
            rectangleWatch.setLayoutY(12);
        }
 
        public void move(double smallRectHeight) {
            rectangleSmall.setHeight(smallRectHeight);
            rectangleSmall.setTranslateY(7 - smallRectHeight);
            rectangleBig.setTranslateY(7 - smallRectHeight);
        }
 
        public void moveDown() {
            move(0);
        }
 
        public void moveUp() {
            move(7);
        }
    }
