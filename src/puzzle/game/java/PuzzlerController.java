
package puzzle.game.java;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import puzzle.game.Main;

public class PuzzlerController implements Initializable{
   private static Stage puzzlerStage;

   /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    @FXML
    void launchGame(ActionEvent event) throws Exception{
        try {
    PuzzleGame launch = new PuzzleGame();
    launch.start(puzzlerStage);
        } catch (IOException ex) {
                    Logger.getLogger(PuzzlerController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
}

