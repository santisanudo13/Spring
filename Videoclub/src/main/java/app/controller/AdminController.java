package app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import app.dao.Pelicula;
import app.dao.PeliculaRepository;
import app.dao.User;
import app.dao.UserRepository;
import rest.RestClientTest.PeliRest;
import rest.RestClientTest.PeliRestService;
import rest.RestClientTest.buscaComp.BuscaComponenteRest;
import rest.RestClientTest.buscaComp.BuscaComponenteRestService;
import retrofit.RestAdapter;









@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private PeliculaRepository peliculaRepository;
	@Autowired
	private UserRepository userRepository;




	/**
	 * Mapeo a direccion peliculas añadiendo objetos de autoridad del usuario y lista total de peliculas.
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/peliculas")
	public ModelAndView peliculas() {

		Iterable<Pelicula> pelis = peliculaRepository.findAll();


		return new ModelAndView("admin/peliculas").addObject("pelis", pelis);
	}

	/**
	 * Mapeo de direccion formulario para anhadir pelicula
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoFormularioAnhadePelicula")
	public ModelAndView metodoFormularioAnhadePelicula() {

		return new ModelAndView("admin/formularioAnhadePelicula");
	}


	/**
	 * Mapeo de direccion buscarPelicula, incluyendo objeto de autoridad del usuario.
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoBuscaPelicula")
	public ModelAndView metodoBuscaPelicula(@RequestParam String nombre) {

		List<Pelicula> pelis=new ArrayList<Pelicula>();

		Iterable<Pelicula> all = peliculaRepository.findAll();
		for (Pelicula p : all) {
			if(p.getNombre().toLowerCase().contains(nombre.toLowerCase())){
				pelis.add(p);
			}
		}
		return new ModelAndView("admin/peliculas").addObject("pelis", pelis);
	}

	/**
	 * Metodo que proceso el añadir una pelicula, si los campos estan vacios(sin incluir nombre y url) hace uso de una API Rest para obtener
	 * los datos necesarios.
	 * 
	 * @param nombre
	 * @param urlVideo
	 * @param descripcion
	 * @param anhio
	 * @param director
	 * @param actores
	 * @param urlPortada
	 * @param valoracion
	 * @return
	 * @throws Exception
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoAnhadePelicula")	
	public ModelAndView anhadePelicula(@RequestParam String nombre,@RequestParam String urlVideo,@RequestParam String descripcion,
			@RequestParam String anhio,@RequestParam String director,@RequestParam String actores,@RequestParam String urlPortada,
			@RequestParam String valoracion) throws Exception{



		//Comprueba campos
		if(nombre.equals("") || urlVideo.equals("") || descripcion.equals("") || anhio.equals("") || director.equals("") || actores.equals("") || urlPortada.equals("") || valoracion.equals("") ){
			return new ModelAndView("admin/formularioAnhadePelicula").addObject("mensjFallo", "Todos los campos son obligatorios");
		}
		List<Pelicula> pels = null;
		pels = peliculaRepository.findByNombre(nombre);
		if(!pels.isEmpty())
			for(Pelicula pel : pels)
				if(pel.getNombre().equals(nombre) && pel.getUrlVideo().equals(urlVideo) && pel.getDescripcion().equals(descripcion) && pel.getAnhio().equals(anhio) && pel.getDirector().equals(director) && pel.getActores().equals(actores) && pel.getUrlPortada().equals(urlPortada) && pel.getValoracion().equals(valoracion))
					return new ModelAndView("admin/formularioAnhadePelicula").addObject("mensjFallo", "La pelicula ya existe");


		//Crea la pelicula usando los datos de la API Rest si son necesarios
		Pelicula p=new Pelicula(nombre,urlVideo,descripcion,anhio,director,actores,urlPortada,valoracion);
		peliculaRepository.save(p);


		List<Pelicula> pelis= peliculaRepository.findAll();

		return new ModelAndView("admin/peliculas").addObject("mensjExito", "La pelicula "+p.getNombre()+" ha sido añadida satisfactoriamente").addObject("pelis", pelis);
	}


	/**
	 * Metodo que permite borrar la pelicula basandonos en el id de la misma.
	 * @param id
	 * @return
	 */

	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoEliminarPelicula")	
	public ModelAndView metodoEliminarPelicula(@RequestParam Long id){


		//Si no encuentra el usuario no lo elimina
		if(peliculaRepository.findOne(id) == null){
			return new ModelAndView("admin/eliminaPelicula").addObject("mensjFallo", "La pelicula que quieres borrar no existe");
		}
		//borra la pelicula
		peliculaRepository.delete(id);

		List<Pelicula> pelis= peliculaRepository.findAll();

		return new ModelAndView("admin/peliculas").addObject("mensjExito", "La pelicula  ha sido eliminada satisfactoriamente").addObject("pelis", pelis);
	}


	/**
	 * Muestra los datos de un usuario para editarlos
	 * @param user
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoDatosPelicula")	
	public ModelAndView metodoDatosPelicula(@RequestParam Long id) {
		Pelicula p = null;
		p = peliculaRepository.findOne(id);

		if(p == null) {
			List<Pelicula> peliculas = peliculaRepository.findAll();
			return new ModelAndView("admin/peliculas").addObject("mensjFallo", "Ha habido un error con el usuario que quieres editar").addObject("pelis", peliculas);
		}

		return new ModelAndView("admin/editPeli").addObject("p", p);
	} 
	/**
	 * Metodo que procesa el boton de editar una peli, lo que hara que se 
	 * borre la peli de la base de datos y nos mande a una ventana donde volver a añadirla con los mismo o diferente valores en sus campos.
	 * @param nombre
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoEditarPelicula")	
	public ModelAndView metodoEditarPelicula(@RequestParam String nombre,@RequestParam String urlVideo,@RequestParam String descripcion,@RequestParam String anhio,@RequestParam String director,@RequestParam String actores,@RequestParam String urlPortada,@RequestParam String valoracion,@RequestParam Long id) {


		if(nombre.equals("") || urlVideo.equals("") || descripcion.equals("") || anhio.equals("") ||director.equals("") ||  actores.equals("") || urlPortada.equals("") || valoracion.equals("") || id == null )
			return new ModelAndView("admin/editPeli").addObject("p", peliculaRepository.findOne(id)).addObject("mensjFallo", "No debe haber campos vacios");





		Pelicula p= null;

		p = peliculaRepository.findOne(id);
		if(p == null) {
			List<Pelicula> peliculas = peliculaRepository.findAll();
			return new ModelAndView("admin/peliculas").addObject("mensjFallo", "Ha habido un error con el usuario que quieres editar").addObject("pelis", peliculas);
		}


		p.setNombre(nombre);
		p.setActores(actores);
		p.setUrlVideo(urlVideo);
		p.setDescripcion(descripcion);
		p.setAnhio(anhio);
		p.setDirector(director);
		p.setUrlPortada(urlPortada);
		p.setValoracion(valoracion);

		peliculaRepository.save(p);

		List<Pelicula> pelis=getAllPelis();

		return new ModelAndView("admin/peliculas").addObject("mensjExito", "La pelicula  ha sido editada satisfactoriamente").addObject("pelis", pelis);
	}

















	/**
	 * Retorna una vista con todos los usuarios, y el objeto que contiene su autorizacion
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/users")
	public ModelAndView users() {

		String isRoot=adminPanel();
		List<User> users=getAllUsers();

		return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("users", users);
	}

	/**
	 * Busca user by nombre
	 * @param user
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoBuscaUser")	
	public ModelAndView metodoBuscaUser(@RequestParam String user) {
		String isRoot=adminPanel();

		List<User> users=new ArrayList<User>();

		Iterable<User> all = userRepository.findAll();
		for (User p : all) {
			if(p.getUser().toLowerCase().contains(user.toLowerCase())){
				users.add(p);
			}
		}

		return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("users", users);
	} 


	/**
	 * Añade un user nuevo con los parametros indicados y con una autorizacion de nivel USER.
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoAnhadeUsuario")	
	public ModelAndView metodoAnhadeUsuario(@RequestParam String username,@RequestParam String password,@RequestParam String email) {
		String isRoot=adminPanel();

		//Comprobar todos campos completos
		if(username.equals("") || password.equals("")||email.equals("")){
			return new ModelAndView("admin/formularioAnhadeUser").addObject("mensjFallo", "Todos los campos son obligatorios");
		}

		//Comprobamos que no esta ya en la base de datos.


		if(userRepository.findOne(username) != null)
			return new ModelAndView("admin/formularioAnhadeUser").addObject("mensjFallo", "El usuario ya existe");

		//Creamos user y lo añadimos a la base de datos
		GrantedAuthority[] userRoles = {
				new SimpleGrantedAuthority("ROLE_USER") };

		User u=new User(username, password, Arrays.asList(userRoles),email);


		userRepository.save(u);

		List<User> users=getAllUsers();

		return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("mensjExito", "El usuario "+username+" ha sido añadido satisfactoriamente").addObject("users", users);
	} 

	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoFormularioAnhadeUsuario")	
	public ModelAndView metodoFormularioAnhadeUsuario() {

		return new ModelAndView("admin/formularioAnhadeUser");

	} 


	/**
	 * Elimina user by nombre
	 * @param nombre
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoEliminarUsuario")	
	public ModelAndView metodoEliminarUsuario(@RequestParam String user) {
		//Autorizacion
		String isRoot=adminPanel();	

		User u=null;
		u = userRepository.findOne(user);


		//borra usuario
		userRepository.delete(u);

		List<User> users = getAllUsers();
		return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("users", users).addObject("mensjExito", "Se ha eliminado el usuario "+user+" con éxito");
	}

	/**
	 * Edita usuario
	 * @param user
	 * @param password
	 * @param email
	 * @param userOld
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoEditarUsuario")	
	public ModelAndView metodEditarUsuario(@RequestParam String user,@RequestParam String password,@RequestParam String email,@RequestParam String userOld) {
		String isRoot=adminPanel();
		List<User> users=getAllUsers();
		//Comprobar todos campos completos
		if(user.equals("") || password.equals("")||email.equals("")||userOld.equals("")){
			return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("mensjFallo", "Todos los campos son obligatorios, error al editar el usuario").addObject("users", users);
		}

		//Comprobamos que no esta ya en la base de datos.


		User u = userRepository.findOne(userOld);
		userRepository.delete(u);

		User uNew = new User(user,password,u.getRoles(),email);

		userRepository.save(uNew);



		return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("mensjExito", "El usuario "+user+" ha sido editado satisfactoriamente").addObject("users", users);
	} 

	/**
	 * Muestra los datos de un usuario para editarlos
	 * @param user
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/metodoDatosUsuario")	
	public ModelAndView metodoDatosUser(@RequestParam String user) {
		User u = null;
		u = userRepository.findOne(user);

		if(u == null) {
			String isRoot=adminPanel();
			List<User> users=getAllUsers();
			return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("mensjFallo", "Ha habido un error con el usuario que quieres editar").addObject("users", users);
		}

		return new ModelAndView("admin/editUser").addObject("u", u);
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
	 * Metodo para obetener todas las peliculas
	 * @return
	 */
	private List<Pelicula> getAllPelis(){
		List<Pelicula> pelis2=new ArrayList<Pelicula>();

		Iterable<Pelicula> all = peliculaRepository.findAll();
		for (Pelicula peli : all) {
			pelis2.add(peli);

		}
		return pelis2;

	}






	/**
	 * retorna todos los usuarios en una lista.
	 * @return
	 */
	private List<User> getAllUsers(){
		List<User> users=new ArrayList<User>();

		Iterable<User> all = userRepository.findAll();
		for (User u : all) {

			users.add(u);

		}
		return users;
	}

}
