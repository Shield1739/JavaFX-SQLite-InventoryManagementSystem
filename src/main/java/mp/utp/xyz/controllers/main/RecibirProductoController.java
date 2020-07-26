package mp.utp.xyz.controllers.main;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mp.utp.xyz.controllers.BaseController;
import mp.utp.xyz.database.DbConnection;

public class RecibirProductoController extends BaseController implements Initializable
{

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private Label fieldLabel;

	@FXML
	private TextField cantidadField;

	@FXML
	private TextField cantidadBrokenField;

	@FXML
	private DatePicker fechaFabricadoDatePicker;

	@FXML
	private DatePicker fechaVenceDatePicker;

	@FXML
	private CheckBox venceCheckBox;

	@FXML
	private Button atrasButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		initComboBox();
		DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		this.fechaFabricadoDatePicker.setValue(LocalDate.parse("01-01-2020", formatoFecha));
		this.fechaVenceDatePicker.setValue(LocalDate.parse("20-02-2020", formatoFecha));
		this.fechaVenceDatePicker.setDisable(true);
	}

	private void initComboBox()
	{
		this.comboBox.setItems(dbUtilities.getComboBoxList());
	}

	@FXML
	private void handleCheckBox()
	{
		this.fechaVenceDatePicker.setDisable(!this.venceCheckBox.isSelected());
	}

	private void setLabel(String text, Color color)
	{
		fieldLabel.setTextFill(color);
		fieldLabel.setText(text);
	}

	@FXML
	private void handleAgregarButton()
	{
		int id = this.comboBox.getSelectionModel().getSelectedIndex();
		if (id == -1)
		{
			return;
		}

		//init variables
		int cantidadTotal;
		int cantidadBrk;
		int cantidadOk;
		LocalDate fechaFabricacion = fechaFabricadoDatePicker.getValue();
		LocalDate fechaVencimiento = fechaVenceDatePicker.getValue();
		id++;

		//Valid Formats
		try
		{
			cantidadTotal = Integer.parseInt(this.cantidadField.getText());
			cantidadBrk = Integer.parseInt(this.cantidadBrokenField.getText());
		}
		catch (NumberFormatException e)
		{
			setLabel("INPUT INVALIDO!", Color.RED);
			return;
		}

		//Valid Conditions
		if (cantidadTotal < cantidadBrk || cantidadBrk < 0 || fechaFabricacion == null)
		{
			setLabel("INPUT INVALIDO!", Color.ORANGE);
			return;
		}

		String fechaV = "null";

		//Set FechaVencimiento
		if (fechaVencimiento != null)
		{
			fechaV = fechaVencimiento.toString();
		}

		//Set Cantidad;
		cantidadOk = cantidadTotal - cantidadBrk;

		/*
		String sql = "SELECT COUNT(ID) AS total FROM Items";

			ResultSet rs = this.c.createStatement().executeQuery(sql);

			int total2 = rs.getInt("total");
		 */

		//Query
		String sql = "INSERT INTO Items VALUES (null, %d, %d, %d, '%s', '%s')";
		sql = String.format(sql, id, cantidadOk, cantidadBrk, fechaFabricacion, fechaV);
		dbUtilities.updateTable(sql);

		String sql2 = "UPDATE Semana SET CantidadRecibidaOk = CantidadRecibidaOk + %d, CantidadRecibidaBRK = CantidadRecibidaBRK + %d WHERE ID = %d";
		sql2 = String.format(sql2, cantidadOk, cantidadOk, id);
		dbUtilities.updateTable(sql2);

		setLabel("AGREGADO", Color.GREEN);
	}

	@FXML
	private void handleAtrasButton()
	{
		Stage stage = (Stage) atrasButton.getScene().getWindow();
		stage.close();
	}
}
