package app.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PeliculaRepository extends CrudRepository<Pelicula, Long> {
	 List<Pelicula> findByNombre(String nombre);
	 List<Pelicula> findByDirector(String director);
	 List<Pelicula> findById(Long id);
	 List<Pelicula> findAll();


	 
	
	} 