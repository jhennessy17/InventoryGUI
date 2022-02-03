package InventoryGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Jeremy Hennessy
 * FUTURE ENHANCEMENTS Make the program maintain its memory contents upon closing the program
 * FUTURE ENHANCEMENTS Make the search functionality dynamic as letters are typed
 */
public class Main extends Application {

    /**
     * Starts the program
     * @param primaryStage main menu
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
