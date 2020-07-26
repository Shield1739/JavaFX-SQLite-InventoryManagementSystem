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
import mp.utp.xyz.data.ProductoTableData;
import mp.utp.xyz.database.DbUtilities;

public class EditarProductosController extends BaseController implements Initializable
{
	@FXML
	private TableView<ProductoTableData> tableView;

	@FXML
	private TableColumn<ProductoTableData, Integer> idProductoColumn;

	@FXML
	private TableColumn<ProductoTableData, String> nombreProductoColumn;

	@FXML
	private TableColumn<ProductoTableData, Integer> idProveedorColumn;

	@FXML
	private TableColumn<ProductoTableData, Integer> puntoReordenColumn;

	@FXML
	private TextField idProductoField;

	@FXML
	private TextField nombreProductoField;

	@FXML
	private TextField idProveedorField;

	@FXML
	private TextField puntoReordenField;

	@FXML
	private Label fieldLabel;

	private ObservableList<ProductoTableData> data;

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

		this.idProductoColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
		this.nombreProductoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
		this.idProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
		this.puntoReordenColumn.setCellValueFactory(new PropertyValueFactory<>("puntoReorden"));
	}

	private void loadData()
	{
		this.data = dbUtilities.getProductoTable();
	}

	private void setFields()
	{
		ProductoTableData value = this.tableView.getSelectionModel().getSelectedItem();

		if (value == null)
		{
			return;
		}

		this.idProductoField.setText("" + value.getIdProducto());
		this.nombreProductoField.setText("" + value.getNombreProducto());
		this.idProveedorField.setText("" + value.getIdProveedor());
		this.puntoReordenField.setText("" + value.getPuntoReorden());
	}

	private void setLabel(String text, Color color)
	{
		fieldLabel.setTextFill(color);
		fieldLabel.setText(text);
	}

	@FXML
	private void handleGuardarButton()
	{
		ProductoTableData tableData = this.tableView.getSelectionModel().getSelectedItem();

		if (tableData == null)
		{
			return;
		}

		//init variables
		String nombre = this.nombreProductoField.getText();
		int proveedor;
		int reorden;
		int id = tableData.getIdProducto();

		//Valid Formats
		try
		{
			proveedor = Integer.parseInt(this.idProveedorField.getText());
			reorden = Integer.parseInt(this.puntoReordenField.getText());
		}
		catch (NumberFormatException e)
		{
			setFields();
			setLabel("INPUT INVALIDO!", Color.RED);
			return;
		}

		//Valid Conditions
		if (nombre == null || nombre.equals("") ||
			proveedor < 1 || proveedor > 5 ||
			reorden < 1 || reorden > 100)
		{
			setFields();
			setLabel("INPUT INVALIDO!", Color.ORANGE);
			return;
		}

		//Query
		String sql = "UPDATE Productos SET Nombre = '%s', Proveedor = %d, PuntoDeReorden = %d WHERE ID = %d";
		sql = String.format(sql, nombre, proveedor, reorden, id);
		dbUtilities.updateTable(sql);

		setLabel("GUARDADO", Color.GREEN);

		//Update Table
		tableData.setNombreProducto(nombre);
		tableData.setIdProveedor(proveedor);
		tableData.setPuntoReorden(reorden);

		this.tableView.refresh();
	}

	@FXML
	private void handleDefaultButton()
	{
		this.tableView.getItems().clear();

		dbUtilities.setDefault(DbUtilities.T_PRODUCTOS, DbUtilities.TDEFAULT_PRODUCTOS);
		loadData();

		this.tableView.setItems(this.data);
	}
}