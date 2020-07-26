package mp.utp.xyz.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductoTableData
{
	private final SimpleIntegerProperty idProducto;
	private SimpleStringProperty nombreProducto;
	private SimpleIntegerProperty idProveedor;
	private SimpleIntegerProperty puntoReorden;

	public ProductoTableData(int idProducto, String nombreProducto, int idProveedor, int puntoReorden)
	{
		this.idProducto = new SimpleIntegerProperty(idProducto);
		this.nombreProducto = new SimpleStringProperty(nombreProducto);
		this.idProveedor = new SimpleIntegerProperty(idProveedor);
		this.puntoReorden = new SimpleIntegerProperty(puntoReorden);
	}

	/**
	 * Setters
	 */

	public void setNombreProducto(String nombreProducto)
	{
		this.nombreProducto.set(nombreProducto);
	}

	public void setIdProveedor(int idProveedor)
	{
		this.idProveedor.set(idProveedor);
	}

	public void setPuntoReorden(int puntoReorden)
	{
		this.puntoReorden.set(puntoReorden);
	}

	/**
	 * Getters
	 */

	public int getIdProducto()
	{
		return idProducto.get();
	}

	public String getNombreProducto()
	{
		return nombreProducto.get();
	}

	public int getIdProveedor()
	{
		return idProveedor.get();
	}

	public int getPuntoReorden()
	{
		return puntoReorden.get();
	}
}
