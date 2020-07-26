package mp.utp.xyz.controllers.init;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import mp.utp.xyz.controllers.BaseController;
import mp.utp.xyz.data.ProveedorTableData;
import mp.utp.xyz.database.DbUtilities;

public class EditarProveedoresController extends BaseController implements Initializable
{
	@FXML
	private TableView<ProveedorTableData> tableView;

	@FXML
	private TableColumn<ProveedorTableData, Integer> idColumn;

	@FXML
	private TableColumn<ProveedorTableData, String> nombreColumn;

	@FXML
	private TextField idField;

	@FXML
	private TextField nombreField;

	@FXML
	private Label fieldLabel;

	private ObservableList<ProveedorTableData> data;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		this.data = FXCollections.observableArrayList();

		initTable();
		this.tableView.setOnMouseClicked(event -> setFields());
		this.tableView.setItems(this.data);
	}

	private void initTable()
	{
		loadData();

		this.idColumn.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
		this.nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
	}

	private void loadData()
	{
		this.data = dbUtilities.getProveedorTable();
	}

	private void setFields()
	{
		ProveedorTableData value = this.tableView.getSelectionModel().getSelectedItem();

		if (value == null)
		{
			return;
		}

		this.idField.setText("" + value.getIdProveedor());
		this.nombreField.setText("" + value.getNombreProveedor());
	}

	private void setLabel(String text, Color color)
	{
		fieldLabel.setTextFill(color);
		fieldLabel.setText(text);
	}

	@FXML
	private void handleGuardarButton()
	{
		ProveedorTableData tableData = this.tableView.getSelectionModel().getSelectedItem();

		if (tableData == null)
		{
			return;
		}

		//init variables
		String nombre = this.nombreField.getText();
		int id = tableData.getIdProveedor();

		//Valid Conditions
		if (nombre == null || nombre.equals(""))
		{
			setFields();
			setLabel("INPUT INVALIDO!", Color.ORANGE);
			return;
		}

		//Query
		String sql = "UPDATE Proveedores SET Nombre = '%s' WHERE ID = %d";
		sql = String.format(sql, nombre, id);
		dbUtilities.updateTable(sql);

		setLabel("GUARDADO", Color.GREEN);

		//Update Table
		tableData.setNombreProveedor(nombre);

		this.tableView.refresh();
	}

	@FXML
	private void handleDefaultButton()
	{
		this.tableView.getItems().clear();

		dbUtilities.setDefault(DbUtilities.T_PROVEEDORES, DbUtilities.TDEFAULT_PROVEEDORES);
		loadData();

		this.tableView.setItems(this.data);
	}
}
