package puzzle.game;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import puzzle.game.java.PuzzleGame;

/**
 *
 * @author Collins Mbwika <collinsmbwika.me>
 */

public class Main extends Application {
    
    private static Stage puzzlerStage;
    private static BorderPane puzzlerLayout;
    
   
    
    @Override
    public void start(Stage stage) {
        
        //Group Groot = new Group();
        
        
        //Parent loaderRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("views/home.fxml"));
                    puzzlerLayout = loader.load();
                    Parent root = puzzlerLayout;

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
        stage.setTitle("THE PUZZLER");
        stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("images/icon_game.png")));

        stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    
        puzzlerStage = new Stage(StageStyle.TRANSPARENT);
        puzzlerStage.setTitle("THE PUZZLER");
        puzzlerStage.getIcons().add(new Image(this.getClass().getResourceAsStream("images/icon_game.png")));
        setStage(puzzlerStage);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> {
                try {
                    Thread.sleep(1000);

                    
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("views/puzzler.fxml"));
                    try {
                        puzzlerLayout = loader.load();
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Parent root = puzzlerLayout;

                    Scene scene = new Scene(root);
                    puzzlerStage.setScene(scene);
                    puzzlerStage.show();
                    stage.hide();
                    
                    //when screen is dragged, translate it accordingly
                    puzzlerLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    private double initX;
                    private double initY;
                    @Override
                    public void handle(MouseEvent me) {
                    puzzlerStage.setX(me.getScreenX() - initX);
                    puzzlerStage.setY(me.getScreenY() - initY);
                    }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                

            });
        }   catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).start();
            
    }
    public static Stage getStage() {
        return puzzlerStage;
    }

    public static void setStage(Stage stage) {
        puzzlerStage = stage;
    }
    
    public static void setRoot() throws IOException{
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("views/puzzler.fxml"));
        puzzlerLayout = loader.load();
    }
    public static void setSplash() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("views/home.fxml"));
        puzzlerLayout = loader.load();
    }
      
    public void showPuzzler() throws IOException{
       try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("views/puzzler.fxml"));
                    puzzlerLayout = loader.load();
                    Parent root = puzzlerLayout;

                    Scene scene = new Scene(root);
                    puzzlerStage.setScene(scene);
                    puzzlerStage.show();
                    
                    //when screen is dragged, translate it accordingly
                puzzlerLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    private double initX;
                    private double initY;
                    @Override
                    public void handle(MouseEvent me) {
                        puzzlerStage.setX(me.getScreenX() - initX);
                        puzzlerStage.setY(me.getScreenY() - initY);
                    }
                });
                    
                      
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }          
            
    }             
        
  
    /**
     * @param args the command line arguments
     */
  
}
               


