package mp.utp.xyz.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProveedorTableData
{
	private final SimpleIntegerProperty idProveedor;
	private SimpleStringProperty nombreProveedor;

	public ProveedorTableData(int idProveedor, String nombreProveedor)
	{
		this.idProveedor = new SimpleIntegerProperty(idProveedor);
		this.nombreProveedor = new SimpleStringProperty(nombreProveedor);
	}

	/**
	 * Setters
	 */

	public void setNombreProveedor(String nombreProveedor)
	{
		this.nombreProveedor.set(nombreProveedor);
	}

	/**
	 * Getters
	 */

	public int getIdProveedor()
	{
		return idProveedor.get();
	}

	public String getNombreProveedor()
	{
		return nombreProveedor.get();
	}
}
