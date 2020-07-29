package mp.utp.xyz.controllers.main;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import mp.utp.xyz.controllers.BaseController;
import mp.utp.xyz.controllers.init.InitSwitcherController;
import mp.utp.xyz.database.DbUtilities;

public class MainController extends BaseController implements Initializable
{
	@FXML
	private GridPane gridPane;

	@FXML
	private Label inicioLabel;

	@FXML
	private Label hoyLabel;

	@FXML
	private Label diaLabel;

	private TableView tableView;

	private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private LocalDate fechaInicio;
	private LocalDate fechaHoy;

	private int diaCount;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		this.tableView = new TableView<>();
		this.gridPane.getChildren().add(this.tableView);

		emptySemana();

		this.fechaInicio = LocalDate.parse("01-01-2020", formatoFecha);
		setupDialog();
		this.fechaHoy = this.fechaInicio;

		this.diaCount = 1;

		this.inicioLabel.setText(this.fechaInicio.format(formatoFecha));
		this.hoyLabel.setText(this.fechaHoy.format(formatoFecha));
		this.diaLabel.setText("#" + this.diaCount);
	}

	private void setupDialog()
	{
		//Dialog
		Dialog<LocalDate> dialog = new Dialog<>();
		dialog.setTitle("Fecha");
		dialog.setHeaderText("Introduzca la fecha de Hoy");

		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

		DatePicker datePicker = new DatePicker();
		datePicker.setValue(this.fechaInicio);

		dialog.getDialogPane().setContent(datePicker);
		Platform.runLater(datePicker::requestFocus);

		dialog.setResultConverter(dialogButton ->
		{
			if (dialogButton == ButtonType.OK)
			{
				return datePicker.getValue();
			}
			return null;
		});

		Optional<LocalDate> result = dialog.showAndWait();

		result.ifPresent(localDate -> {
			this.fechaInicio = localDate;
		});
	}

	private void clearGrid()
	{
		if (this.gridPane.getChildren().size() > 0)
		{
			this.gridPane.getChildren().clear();
		}
	}

	@FXML
	private void handleEntregar()
	{
		windows("views/main/EntregarProducto.fxml", "Entregar Producto");
	}

	@FXML
	private void handleRecibir()
	{
		windows("views/main/RecibirProducto.fxml", "Recibir Producto");
	}

	@FXML
	private void handleNextDay()
	{
		this.fechaHoy = this.fechaHoy.plusDays(1);
		this.diaCount++;

		this.hoyLabel.setText(this.fechaHoy.format(formatoFecha));
		this.diaLabel.setText("#" + this.diaCount);
	}

	/**
	 * SQL QUERIES
	 */

	@FXML
	private void handleExistenciaPorProducto()
	{
		clearGrid();
		String sql =
			"SELECT p.ID, p.Nombre, sum(cantidadOK + cantidadBRK) AS 'Cantidad Total', sum(cantidadOK) AS 'Cantidad OK', sum(cantidadBRK) AS 'Cantidad Dañada' " +
				"FROM Productos p INNER JOIN Items i ON p.ID = i.ProductoID GROUP BY p.ID";
		buildTable(sql);
	}

	@FXML
	private void handleVencenPronto()
	{
		clearGrid();
		String sql =
			"SELECT p.ID, p.Nombre, i.ID AS 'Item ID', FechaVencimiento AS 'Fecha Vencimiento', " +
				"Cast ((JulianDay(FechaVencimiento) - JulianDay('" + this.fechaHoy.toString() + "')) AS Integer) AS 'Dias Hasta Expirar' " +
				"FROM Productos p INNER JOIN Items i ON p.ID = i.ProductoID " +
				"WHERE FechaVencimiento != 'null' AND [Dias Hasta Expirar] < 30 AND [Dias Hasta Expirar] > -1";
		buildTable(sql);
	}

	@FXML
	private void handleProductoPorProveedor()
	{
		clearGrid();
		String sql =
			"SELECT pd.ID, pd.Nombre, p.ID, p.Nombre " +
				"FROM Proveedores pd INNER JOIN Productos p ON pd.ID = p.Proveedor";
		buildTable(sql);
	}

	@FXML
	private void handleProductoEnReorden()
	{
		clearGrid();
		String sql =
			"SELECT p.ID, p.Nombre, p.PuntoDeReorden, cantidadOK AS 'Cantidad OK' " +
				"FROM Productos p INNER JOIN Items i ON p.ID = i.ProductoID " +
				"WHERE [Cantidad OK] <= p.PuntoDeReorden GROUP BY p.ID";
		buildTable(sql);
	}

	@FXML
	private void handleProductoSinExistencia()
	{
		clearGrid();
		String sql =
			"SELECT p.ID, p.Nombre " +
				"FROM Productos p INNER JOIN Items i ON p.ID = i.ProductoID " +
				"WHERE cantidadOK = 0 GROUP BY p.ID";
		buildTable(sql);
	}

	@FXML
	private void handleProductoVencido()
	{
		clearGrid();
		String sql =
			"SELECT p.ID, p.Nombre, i.ID AS 'Item ID', FechaVencimiento AS 'Fecha Vencimiento' " +
				"FROM Productos p INNER JOIN Items i ON p.ID = i.ProductoID " +
				"WHERE FechaVencimiento != 'null' AND Cast ((JulianDay(FechaVencimiento) - JulianDay('" + this.fechaHoy.toString() + "')) AS Integer) < 0";
		buildTable(sql);
	}

	@FXML
	private void handleProductoBroken()
	{
		clearGrid();
		String sql =
			"SELECT p.ID, p.Nombre, cantidadBRK AS 'Cantidad Dañada' " +
				"FROM Productos p INNER JOIN Items i ON p.ID = i.ProductoID " +
				"WHERE cantidadBRK != 0";
		buildTable(sql);
	}

	@FXML
	private void handleSemana()
	{
		// WHERE [CantidadEntregada + CantidadRecibidaOK + CantidadRecibidaBrk] != 0
		clearGrid();
		String sql =
			"SELECT ID, Nombre, CantidadEntregada AS 'Cantidad Entregada', CantidadRecibidaOK AS 'Cantidad Recibida OK', " +
				"CantidadRecibidaBrk AS 'Cantidad Recibida Dañada' " +
				"FROM Semana WHERE CantidadEntregada + CantidadRecibidaOK + CantidadRecibidaBrk != 0";
		buildTable(sql);
	}

	/**
	 * Utility
	 */

	private void buildTable(String sql)
	{
		this.tableView = dbUtilities.buildTable(sql);
		this.gridPane.getChildren().add(this.tableView);
	}

	private void emptySemana()
	{
		dbUtilities.setDefault(DbUtilities.T_SEMANA, DbUtilities.TDEFAULT_SEMANA);
	}

	private void windows(String path, String title)
	{
		InitSwitcherController.setWindows(path, title);
	}
}
