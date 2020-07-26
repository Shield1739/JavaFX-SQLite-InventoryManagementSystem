package mp.utp.xyz.controllers.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mp.utp.xyz.controllers.BaseController;
import mp.utp.xyz.data.ItemTableData;

public class EntregarProductoController extends BaseController implements Initializable
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
	private TableColumn<ItemTableData, String> fabricadoColumn;

	@FXML
	private TableColumn<ItemTableData, String> venceColumn;

	@FXML
	private Label fieldLabel;

	@FXML
	private TextField cantidadField;

	@FXML
	private Button atrasButton;

	private ObservableList<ItemTableData> data;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		this.data = FXCollections.observableArrayList();

		initTable();
		this.tableView.setItems(this.data);
	}

	private void initTable()
	{
		loadData();

		this.idItemColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
		this.idProductoColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
		this.cntOkColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadOK"));
		this.fabricadoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaFabricacion"));
		this.venceColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
	}

	private void loadData()
	{
		this.data = dbUtilities.getItemTableReduced();
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
		int cantidadOK = tableData.getCantidadOK();
		int cantidadEntregar = 0;

		//Valid Formats
		try
		{
			cantidadEntregar = Integer.parseInt(cantidadField.getText());

		}
		catch (NumberFormatException e)
		{
			setLabel("INPUT INVALIDO!", Color.RED);
		}

		//Valid Conditions
		if (cantidadEntregar > cantidadOK || cantidadEntregar <= 0)
		{
			setLabel("INPUT INVALIDO!", Color.ORANGE);
			return;
		}

		//Set nuevaCantidad
		int nuevaCantidad = tableData.getCantidadOK() - cantidadEntregar;

		//Query
		String sql = "UPDATE Items SET CantidadOK = %d WHERE ID = %d";
		sql = String.format(sql, nuevaCantidad, tableData.getIdItem());
		dbUtilities.updateTable(sql);

		String sql2 = "UPDATE Semana SET CantidadEntregada = CantidadEntregada + %d WHERE ID = %d";
		sql2 = String.format(sql2, cantidadEntregar, tableData.getIdItem());
		dbUtilities.updateTable(sql2);

		setLabel("GUARDADO", Color.GREEN);

		//Update Table
		tableData.setCantidadOK(nuevaCantidad);
		this.tableView.refresh();
	}

	@FXML
	private void handleAtrasButton()
	{
		Stage stage = (Stage) atrasButton.getScene().getWindow();
		stage.close();
	}
}

