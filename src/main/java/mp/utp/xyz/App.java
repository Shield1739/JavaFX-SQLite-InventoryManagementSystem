package mp.utp.xyz;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(App.class.getResource("views/init/InitSwitcher.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Oficina XYZ");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
}
