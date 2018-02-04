package app.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

		String isRoot=adminPanel();

		if(peliculaRepository.findAll().isEmpty())
			return new ModelAndView("user/home").addObject("isRoot", isRoot);

		List<List> list=randomPelisHome();


		return new ModelAndView("user/home").addObject("isRoot", isRoot).addObject("list",list);
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
	 * Metodo para obetener nivel de autoridad del usuario logeado.
	 * @return
	 */
	private String adminPanel(){
		//Botones de ADMIN
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String isRoot="";
		Iterator<? extends GrantedAuthority> a=auth.getAuthorities().iterator();

		a.next();//ROLE_USER

		if(a.hasNext())
			isRoot = a.next().toString();
		return isRoot;
	}

	/**
	 * Retorna1 lista de todas las peliculas de forma aleatoria
	 * @return
	 */
	private List<List> randomPelisHome(){
		List<Pelicula> list = peliculaRepository.findAll();
		Collections.shuffle(list, new Random(System.nanoTime()));

		List<List> listDeList = new ArrayList<List>();
		
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
			}
		}
		
		return listDeList;

	}
	/**
	 * Metodo que busca peliculas basadas en el nombre pasado como parametro. Con que la pelicula 
	 * contenga el nombre pasado como parametro se incluira como valida.
	 * @param nombre
	 * @return
	 */
	private List<Pelicula> buscaPeli(String nombre){
		List<Pelicula> pelisAll= peliculaRepository.findAll();

		List<Pelicula> pelis=new ArrayList<Pelicula>();

		for(Pelicula p:pelisAll){
			if(p.getNombre().toLowerCase().contains(nombre.toLowerCase())){
				pelis.add(p);
			}
		}
		return pelis;
	}


}
