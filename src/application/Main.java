package application;
	
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FileInputStream inputStream = new FileInputStream("ai.GIF");
			Image gifImage = new Image(inputStream);
			ImageView imageView = new ImageView(gifImage);
			imageView.setFitHeight(150);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			HBox header = (HBox) root.getChildren().get(0);
			header.getChildren().add(imageView);
			Scene scene = new Scene(root,910,910);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("CONNECT FOUR!!!");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) { launch(args); }
}