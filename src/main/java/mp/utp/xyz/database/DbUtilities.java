package mp.utp.xyz.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import mp.utp.xyz.data.ItemTableData;
import mp.utp.xyz.data.ProductoTableData;
import mp.utp.xyz.data.ProveedorTableData;

public class DbUtilities
{
	private Connection c;

	//Table Names
	public static final String TDEFAULT_PRODUCTOS = "ProductosDEFAULT";
	public static final String TDEFAULT_PROVEEDORES = "ProveedoresDEFAULT";
	public static final String TDEFAULT_ITEMS = "ItemsDEFAULT";
	public static final String TDEFAULT_SEMANA = "SemanaDEFAULT";

	public static final String T_PRODUCTOS = "Productos";
	public static final String T_PROVEEDORES = "Proveedores";
	public static final String T_ITEMS = "Items";
	public static final String T_SEMANA = "Semana";

	//Private Utility
	private void open()
	{
		this.c = DbConnection.getConnection();

		if (this.c == null)
		{
			System.exit(2);
		}
	}

	private void close()
	{
		try
		{
			this.c.close();
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}
	}

	/**
	 * Setters
	 */

	public void setDefault(String table, String tableDefault)
	{
		open();
		try
		{
			String sql = "DELETE FROM %s";
			sql = String.format(sql, table);
			this.c.createStatement().executeUpdate(sql);

			String sql2 = "INSERT INTO %s SELECT * FROM %s";
			sql2 = String.format(sql2, table, tableDefault);
			this.c.createStatement().executeUpdate(sql2);

			if (table.equals(T_SEMANA))
			{
				String sql3 = "UPDATE Semana SET Nombre = (SELECT p.Nombre from Productos p WHERE p.ID = Semana.ID)";
				this.c.createStatement().executeUpdate(sql3);
			}
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
	}

	public void updateTable(String sql)
	{
		open();

		try
		{
			this.c.createStatement().executeUpdate(sql);
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
	}

	public void setAutoIncrement()
	{
		open();

		try
		{
			String sql = "UPDATE sqlite_sequence SET SEQ = 10 WHERE NAME = 'Items'";
			this.c.createStatement().executeUpdate(sql);
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
	}

	/**
	 * Getters
	 */

	public TableView buildTable(String sql)
	{
		open();

		ObservableList<ObservableList> data = FXCollections.observableArrayList();
		TableView tableView = new TableView<>();

		try
		{
			ResultSet rs = c.createStatement().executeQuery(sql);

			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++)
			{
				final int j = i;
				TableColumn col = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
					(Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>)
						param -> new SimpleStringProperty(param.getValue().get(j).toString()));

				tableView.getColumns().addAll(col);
			}

			while (rs.next())
			{
				//Iterate Row
				ObservableList<String> row = FXCollections.observableArrayList();

				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
				{
					//Iterate Column
					row.add(rs.getString(i));
				}
				//System.out.println("Row [1] added "+row );
				data.add(row);
			}

			//add to tableview
			tableView.setItems(data);
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
		return tableView;
	}

	public ObservableList<ProductoTableData> getProductoTable()
	{
		open();

		ObservableList<ProductoTableData> data = null;

		try
		{
			String sql = "SELECT * FROM Productos";
			ResultSet rs = this.c.createStatement().executeQuery(sql);

			data = FXCollections.observableArrayList();
			while (rs.next())
			{
				data.add(new ProductoTableData(
					rs.getInt("ID"), rs.getString("Nombre"),
					rs.getInt("Proveedor"), rs.getInt("PuntoDeReorden"))
				);
			}
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
		return data;
	}

	public ObservableList<ProveedorTableData> getProveedorTable()
	{
		open();

		ObservableList<ProveedorTableData> data = null;

		try
		{
			String sql = "SELECT * FROM Proveedores";
			ResultSet rs = this.c.createStatement().executeQuery(sql);

			data = FXCollections.observableArrayList();
			while (rs.next())
			{
				data.add(new ProveedorTableData(
					rs.getInt("ID"), rs.getString("Nombre"))
				);
			}
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
		return data;
	}

	public ObservableList<ItemTableData> getItemTable()
	{
		open();

		ObservableList<ItemTableData> data = null;

		try
		{
			String sql = "SELECT * FROM Items";
			ResultSet rs = this.c.createStatement().executeQuery(sql);

			data = FXCollections.observableArrayList();
			while (rs.next())
			{
				data.add(new ItemTableData(
					rs.getInt("ID"), rs.getInt("ProductoID"),
					rs.getInt("CantidadOK"), rs.getInt("CantidadBRK"),
					rs.getString("FechaFabricacion"), rs.getString("FechaVencimiento"))
				);
			}
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
		return data;
	}

	public ObservableList<ItemTableData> getItemTableReduced()
	{
		open();

		ObservableList<ItemTableData> data = null;

		try
		{
			String sql = "SELECT * FROM Items WHERE CantidadOK > 0";
			ResultSet rs = this.c.createStatement().executeQuery(sql);

			data = FXCollections.observableArrayList();
			while (rs.next())
			{
				data.add(new ItemTableData(
					rs.getInt("ID"), rs.getInt("ProductoID"),
					rs.getInt("CantidadOK"), rs.getString("FechaFabricacion"), rs.getString("FechaVencimiento"))
				);
			}
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
		return data;
	}

	public ObservableList<String> getComboBoxList()
	{
		open();

		ObservableList<String> productoList = FXCollections.observableArrayList();

		try
		{
			ResultSet rs = this.c.createStatement().executeQuery("SELECT ID, Nombre FROM Productos");

			while (rs.next())
			{
				productoList.add("#" + rs.getInt(1) + ", " + rs.getString(2));
			}
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		close();
		return productoList;
	}
}
