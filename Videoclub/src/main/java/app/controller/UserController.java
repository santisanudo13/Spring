package app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import app.dao.Pelicula;
import app.dao.PeliculaRepository;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private PeliculaRepository peliculaRepository;



	/**
	 * Mapeo a home, añade a la vista si todo es correcto, nivel de autorizacion de user, y 2 listas de 4 aleatorias del 
	 * total de las peliculas de la base de datos
	 * @return
	 */
	@Secured({ "ROLE_USER" })
	@RequestMapping("/home")
	public ModelAndView home() {

		List<List<Pelicula>> list=randomPelisHome();
		return new ModelAndView("user/home").addObject("list",list);
	}

	/**
	 * Mapeo a la direccion verPelicula, donde se incluye el nombre de la peli y la url de la misma.
	 * @param url
	 * @param nombre
	 * @return
	 */
	@Secured({ "ROLE_USER"})
	@RequestMapping("/verPelicula")
	public ModelAndView verPelicula(@RequestParam String url, @RequestParam String nombre) {


		return new ModelAndView("user/verPelicula").addObject("nombre",nombre).addObject("url", url); 
	}



	/**
	 * 
	 * @param nombre
	 * @return
	 */
	@Secured({ "ROLE_USER" })
	@RequestMapping("/buscaPelicula")	
	public ModelAndView processBuscaPeli(@RequestParam String nombre) {

		List<Pelicula> pelis=buscaPeli(nombre);

		return new ModelAndView("user/buscaPelicula").addObject("pelis", pelis).addObject("nombre",nombre);
	} 



	/**
	 * Retorna1 lista de todas las peliculas de forma aleatoria
	 * @return
	 */
	private List<List<Pelicula>> randomPelisHome(){
		Iterable<Pelicula> pelis = peliculaRepository.findAll();

		List<Pelicula> list = new ArrayList<Pelicula>();
		
		for(Pelicula p : pelis)
			list.add(p);
		
		Collections.shuffle(list, new Random(System.nanoTime()));

		List<List<Pelicula>> listDeList = new ArrayList<List<Pelicula>>();

		int i = 0;
		int counter = 0;
		List<Pelicula> l = null;
		while(i < list.size()) {
			if(counter == 0) {
				l = new ArrayList<Pelicula>();
			}

			l.add(list.get(i));


			i++;
			counter++;
			if(counter%4 == 0) {
				counter = 0;
				listDeList.add(l);
				l = null;
			}
		}
		if(l != null)
			listDeList.add(l);

		return listDeList;

	}
	/**
	 * Metodo que busca peliculas basadas en el nombre pasado como parametro. Con que la pelicula 
	 * contenga el nombre pasado como parametro se incluira como valida.
	 * @param nombre
	 * @return
	 */
	private List<Pelicula> buscaPeli(String nombre){
		Iterable<Pelicula> pelisAll = peliculaRepository.findAll();

		List<Pelicula> pelis=new ArrayList<Pelicula>();

		for(Pelicula p:pelisAll){
			if(p.getNombre().toLowerCase().contains(nombre.toLowerCase())){
				pelis.add(p);
			}
		}
		return pelis;
	}


}
