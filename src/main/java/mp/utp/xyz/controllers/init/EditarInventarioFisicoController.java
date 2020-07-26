package mp.utp.xyz.controllers.init;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import mp.utp.xyz.controllers.BaseController;
import mp.utp.xyz.data.ItemTableData;
import mp.utp.xyz.database.DbUtilities;

public class EditarInventarioFisicoController extends BaseController implements Initializable
{
	@FXML
	private TableView<ItemTableData> tableView;

	@FXML
	private TableColumn<ItemTableData, Integer> idItemColumn;

	@FXML
	private TableColumn<ItemTableData, Integer> idProductoColumn;

	@FXML
	private TableColumn<ItemTableData, Integer> cntOkColumn;

	@FXML
	private TableColumn<ItemTableData, Integer> cntBrkColumn;

	@FXML
	private TableColumn<ItemTableData, String> fabricadoColumn;

	@FXML
	private TableColumn<ItemTableData, String> venceColumn;

	@FXML
	private TextField idItemField;

	@FXML
	private TextField idProductoField;

	@FXML
	private TextField cntOkField;

	@FXML
	private TextField cntBrkField;

	@FXML
	private DatePicker fabricadoDatePicker;

	@FXML
	private DatePicker venceDatePicker;

	@FXML
	private CheckBox venceCheckBox;

	@FXML
	private Label fieldLabel;

	private ObservableList<ItemTableData> data;

	private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		this.data = FXCollections.observableArrayList();

		initTable();
		this.tableView.setOnMouseClicked(event -> setFields());
		this.tableView.setItems(this.data);
	}

	public void initTable()
	{
		loadData();

		this.idItemColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
		this.idProductoColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
		this.cntOkColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadOK"));
		this.cntBrkColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadBRK"));
		this.fabricadoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaFabricacion"));
		this.venceColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
	}

	private void loadData()
	{
		this.data = dbUtilities.getItemTable();
	}

	private void setFields()
	{
		ItemTableData value = this.tableView.getSelectionModel().getSelectedItem();

		if (value == null)
		{
			return;
		}

		this.idItemField.setText("" + value.getIdItem());
		this.idProductoField.setText("" + value.getIdProducto());
		this.cntOkField.setText("" + value.getCantidadOK());
		this.cntBrkField.setText("" + value.getCantidadBRK());
		this.fabricadoDatePicker.setValue(value.getLocalFechaFabricacion());

		if (!value.getFechaVencimiento().equals("null"))
		{
			this.venceDatePicker.setValue(value.getLocalFechaVencimiento());
		}

		this.venceCheckBox.setSelected(!value.getFechaVencimiento().equals("null"));
		handleVenceCheckBox();
	}

	private void setLabel(String text, Color color)
	{
		fieldLabel.setTextFill(color);
		fieldLabel.setText(text);
	}

	@FXML
	private void handleGuardarButton()
	{
		ItemTableData tableData = this.tableView.getSelectionModel().getSelectedItem();

		if (tableData == null)
		{
			return;
		}

		//init variables
		int cantidadOK;
		int cantidadBRK;
		LocalDate fechaFabricacion = this.fabricadoDatePicker.getValue();
		LocalDate fechaVencimiento = this.venceDatePicker.getValue();
		int id = tableData.getIdItem();

		//Valid Formats
		try
		{
			cantidadOK = Integer.parseInt(this.cntOkField.getText());
			cantidadBRK = Integer.parseInt(this.cntBrkField.getText());

		}
		catch (NumberFormatException e)
		{
			setFields();
			setLabel("INPUT INVALIDO!", Color.RED);
			return;
		}

		//Valid Conditions
		if (cantidadOK < 0 || cantidadOK > 1000 ||
			cantidadBRK < 0 || cantidadBRK > 1000)
		{
			setFields();
			setLabel("INPUT INVALIDO!", Color.ORANGE);
			return;
		}

		String fechaV = "null";

		//Set FechaVencimiento
		if (!venceCheckBox.isSelected())
		{
			fechaVencimiento = null;
		}
		else if (fechaVencimiento != null)
		{
			fechaV = fechaVencimiento.toString();
		}

		//Query
		String sql = "UPDATE Items SET CantidadOK = %d, CantidadBRK = %d, FechaFabricacion = '%s', FechaVencimiento = '%s' WHERE ID = %d";
		sql = String.format(sql, cantidadOK, cantidadBRK, fechaFabricacion, fechaV, id);
		dbUtilities.updateTable(sql);

		setLabel("GUARDADO!", Color.GREEN);

		//Update Table
		tableData.setCantidadOK(cantidadOK);
		tableData.setCantidadBRK(cantidadBRK);
		tableData.setFechaFabricacion(fechaFabricacion.format(formatoFecha));

		if (fechaVencimiento != null)
		{
			fechaV = fechaVencimiento.format(formatoFecha);
		}

		tableData.setFechaVencimiento(fechaV);

		this.tableView.refresh();
	}

	@FXML
	private void handleDefaultButton()
	{
		this.tableView.getItems().clear();

		dbUtilities.setDefault(DbUtilities.T_ITEMS, DbUtilities.TDEFAULT_ITEMS);
		dbUtilities.setAutoIncrement();
		loadData();

		this.tableView.setItems(this.data);
	}

	@FXML
	private void handleVenceCheckBox()
	{
		this.venceDatePicker.setDisable(!this.venceCheckBox.isSelected());
	}
}
