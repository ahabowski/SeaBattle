package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.core.NewGame;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new NewGame(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
