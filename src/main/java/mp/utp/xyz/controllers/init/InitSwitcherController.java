package mp.utp.xyz.controllers.init;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mp.utp.xyz.App;

public class InitSwitcherController
{
	@FXML
	private Button startAppButton;

	@FXML
	private void handleEditarProductosButton()
	{
		windows("views/init/EditarProductos.fxml", "Editar Productos");
	}

	@FXML
	private void handleEditarProveedoresButton()
	{
		windows("views/init/EditarProveedores.fxml", "Editar Proveedores");
	}

	@FXML
	private void handleEditarInventarioButton()
	{
		windows("views/init/EditarInventarioFisico.fxml", "Inventario Fisico");
	}

	@FXML
	private void handleStartAppButton() throws IOException
	{
		Stage stage = (Stage) startAppButton.getScene().getWindow();
		Parent root = FXMLLoader.load(App.class.getResource("views/main/Main.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	private void windows(String path, String title)
	{
		setWindows(path, title);
	}

	public static void setWindows(String path, String title)
	{
		Parent root;
		try
		{
			root = FXMLLoader.load(App.class.getResource(path));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle(title);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
	}
}
