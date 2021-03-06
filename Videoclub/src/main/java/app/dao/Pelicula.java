package app.dao;



import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Pelicula implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String nombre;
	private String urlVideo; 
	private String descripcion; 
	private int anhio; 
	private String director; 
	private String actores; 
	private String urlPortada; 
	private int valoracion; 



	// Default contructor (needed by SpringData)
	protected Pelicula() {
	}
	//Constructor




	public Pelicula(String nombre, String urlVideo, String descripcion, int anhio, String director, String actores,
			String urlPortada, int valoracion) {
		super();
		this.nombre = nombre;
		this.urlVideo = urlVideo;
		this.descripcion = descripcion;
		this.anhio = anhio;
		this.director = director;
		this.actores = actores;
		this.urlPortada = urlPortada;
		this.valoracion = valoracion;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlVideo() {
		return urlVideo;
	}
	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getAnhio() {
		return anhio;
	}
	public void setAnhio(int anhio) {
		this.anhio = anhio;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActores() {
		return actores;
	}
	public void setActores(String actores) {
		this.actores = actores;
	}
	public String getUrlPortada() {
		return urlPortada;
	}
	public void setUrlPortada(String urlPortada) {
		this.urlPortada = urlPortada;
	}
	public int getValoracion() {
		return valoracion;
	}
	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}










}
