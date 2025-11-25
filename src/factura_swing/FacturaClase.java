package factura_swing;

import java.time.LocalDate;

public class FacturaClase {
	private LocalDate fecha_fac;
	private String asunto_fac;
	private int cantidad_fac;
	private String tipo_fac;
	
	
	public FacturaClase(String tipo, String asunto, int cantidad, int anio, int mes, int dia) {
		this.asunto_fac=asunto;
		this.tipo_fac=tipo;
		this.cantidad_fac=cantidad;
		this.fecha_fac=LocalDate.of(anio, mes, dia);
	}


	public LocalDate getFecha_fac() {
		return fecha_fac;
	}


	public String getAsunto_fac() {
		return asunto_fac;
	}


	public void setAsunto_fac(String asunto_fac) {
		this.asunto_fac = asunto_fac;
	}


	public int getCantidad_fac() {
		return cantidad_fac;
	}


	public void setCantidad_fac(int cantidad_fac) {
		this.cantidad_fac = cantidad_fac;
	}


	public String getTipo_fac() {
		return tipo_fac;
	}


	public void setTipo_fac(String tipo_fac) {
		this.tipo_fac = tipo_fac;
	}


	@Override
	public String toString() {
		return this.asunto_fac+":"+this.tipo_fac+":"+this.cantidad_fac+":"+this.fecha_fac;
	}
	
	
	
}
