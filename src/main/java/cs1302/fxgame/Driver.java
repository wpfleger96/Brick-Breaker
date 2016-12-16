
package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 Driver.java extends application and is the entry point into the program. It builds the GUI using an instance of BreakoutGame.java
 @author: William Pfleger
 */

public class Driver extends Application {

    //Creates the GUI with an instance of BreakoutGame
    @Override
    public void start(Stage primaryStage) throws Exception { 
        Game game = new BreakoutGame(primaryStage);
        primaryStage.setTitle(game.getTitle());
        primaryStage.setScene(game.getScene());
        primaryStage.show();
        game.run();
    } // start
    
    //Launches the program
    public static void main(String[] args) {
        launch(args);
    } // main

} // Driver

