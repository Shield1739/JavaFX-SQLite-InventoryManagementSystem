package mp.utp.xyz.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemTableData
{
	private DateTimeFormatter formatoFechaDB = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private final SimpleIntegerProperty idItem;
	private final SimpleIntegerProperty idProducto;
	private SimpleIntegerProperty cantidadOK;
	private SimpleIntegerProperty cantidadBRK;
	private SimpleStringProperty fechaFabricacion;
	private SimpleStringProperty fechaVencimiento;

	public ItemTableData(int idItem, int idProducto, int cantidadOK, int cantidadBRK, String fechaFabricacion, String fechaVencimiento)
	{
		this.idItem = new SimpleIntegerProperty(idItem);
		this.idProducto = new SimpleIntegerProperty(idProducto);
		this.cantidadOK = new SimpleIntegerProperty(cantidadOK);
		this.cantidadBRK = new SimpleIntegerProperty(cantidadBRK);
		LocalDate ff = LocalDate.parse(fechaFabricacion, formatoFechaDB);
		this.fechaFabricacion = new SimpleStringProperty(ff.format(formatoFecha));
		if (fechaVencimiento.equals("null"))
		{
			this.fechaVencimiento = new SimpleStringProperty("null");
		}
		else
		{
			LocalDate fv = LocalDate.parse(fechaVencimiento, formatoFechaDB);
			this.fechaVencimiento = new SimpleStringProperty(fv.format(formatoFecha));
		}
	}

	public ItemTableData(int idItem, int idProducto, int cantidadOK, String fechaFabricacion, String fechaVencimiento)
	{
		this.idItem = new SimpleIntegerProperty(idItem);
		this.idProducto = new SimpleIntegerProperty(idProducto);
		this.cantidadOK = new SimpleIntegerProperty(cantidadOK);
		LocalDate ff = LocalDate.parse(fechaFabricacion, formatoFechaDB);
		this.fechaFabricacion = new SimpleStringProperty(ff.format(formatoFecha));
		if (fechaVencimiento.equals("null"))
		{
			this.fechaVencimiento = new SimpleStringProperty("null");
		}
		else
		{
			LocalDate fv = LocalDate.parse(fechaVencimiento, formatoFechaDB);
			this.fechaVencimiento = new SimpleStringProperty(fv.format(formatoFecha));
		}
	}

	/**
	 * Setters
	 */

	public void setCantidadOK(int cantidadOK)
	{
		this.cantidadOK.set(cantidadOK);
	}

	public void setCantidadBRK(int cantidadBRK)
	{
		this.cantidadBRK.set(cantidadBRK);
	}

	public void setFechaFabricacion(String fechaFabricacion)
	{
		this.fechaFabricacion.set(fechaFabricacion);
	}

	public void setFechaVencimiento(String fechaVencimiento)
	{
		this.fechaVencimiento.set(fechaVencimiento);
	}

	/**
	 * Getters
	 */

	public int getIdItem()
	{
		return idItem.get();
	}

	public int getIdProducto()
	{
		return idProducto.get();
	}

	public int getCantidadOK()
	{
		return cantidadOK.get();
	}

	public int getCantidadBRK()
	{
		return cantidadBRK.get();
	}

	public String getFechaFabricacion()
	{
		return fechaFabricacion.get();
	}

	public String getFechaVencimiento()
	{
		return fechaVencimiento.get();
	}

	public LocalDate getLocalFechaFabricacion()
	{
		return LocalDate.parse(fechaFabricacion.get(), formatoFecha);
	}

	public LocalDate getLocalFechaVencimiento()
	{
		if (fechaVencimiento.get() == null)
		{
			return null;
		}
		return LocalDate.parse(fechaVencimiento.get(), formatoFecha);
	}
}
