package app.dao;



import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Archivo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nombre;
	private String descripcion;
	private String tipo;
	@Column(length=2100000000)
	private byte[] file;



	// Default contructor (needed by SpringData)
	public Archivo() {
	}
	//Constructor



	public Archivo(String nombre, String descripcion, String tipo, byte[] file) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.file = file;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public byte[] getFile() {
		return file;
	}



	public void setFile(byte[] file) {
		this.file = file;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	


}