package core;

import func.FileIO;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/RollCallView2.fxml"));
        primaryStage.setTitle(" 随机点名器 version:1.0.1");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/rollcall.png")));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
//        File file = new File("./src/excelsamples/16计科1.xls");
//        FileIO.exportClassRollBook(primaryStage,file);
    }


    public static void main(String[] args) {

        launch(args);
    }
}
