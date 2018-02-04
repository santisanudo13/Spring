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

		List<Pelicula> pelis=getAllPelis();
		String isRoot=adminPanel();

		return new ModelAndView("admin/peliculas").addObject("isRoot", isRoot).addObject("pelis", pelis);
	}

	/**
	 * Mapeo de direccion anhadePelicula, incluyendo objeto de autoridad del usuario.
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/anhadePelicula")
	public ModelAndView anhadePelicula() {
		String isRoot=adminPanel();

		return new ModelAndView("admin/anhadePelicula").addObject("isRoot", isRoot);
	}

	/**
	 * Mapeo de direccion eliminaPelicula, incluyendo objeto de autoridad del usuario.
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/eliminaPelicula")
	public ModelAndView eliminaPelicula() {
		String isRoot=adminPanel();

		return new ModelAndView("admin/eliminaPelicula").addObject("isRoot", isRoot);
	}
	/**
	 * Mapeo de direccion buscarPelicula, incluyendo objeto de autoridad del usuario.
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/buscarPelicula")
	public ModelAndView buscarPelicula() {
		String isRoot=adminPanel();
		return new ModelAndView("admin/buscarPelicula").addObject("isRoot", isRoot);
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
	@RequestMapping("/processFormPeli")	
	public ModelAndView anhadePelicula(@RequestParam String nombre,@RequestParam String urlVideo,@RequestParam String descripcion,
			@RequestParam String anhio,@RequestParam String director,@RequestParam String actores,@RequestParam String urlPortada,
			@RequestParam String valoracion) throws Exception{

		String isRoot=adminPanel();

		//Comprueba campos
		if(nombre.equals("") || urlVideo.equals("") || descripcion.equals("") || anhio.equals("") || director.equals("") || actores.equals("") || urlPortada.equals("") || valoracion.equals("") ){
			return new ModelAndView("admin/anhadePelicula").addObject("isRoot",isRoot).addObject("mensjFallo", "Todos los campos Nombre y UrlVideo son obligatorios");
		}
		if(!peliculaRepository.findByNombre(nombre).isEmpty())
			return new ModelAndView("admin/anhadePelicula").addObject("isRoot",isRoot).addObject("mensjFallo", "La pelicula ya existe");


		// Obtiene id peli en API Rest
		String id="";
		/*
		BuscaComponenteRest x = idPeliRest(nombre);


		//Compara si el id es igual que alguno de los del API Rest


		List<Title> titles=x.getData().getResults().getTitles();

		for(Title t:titles){
			if(t.getTitle().toLowerCase().equals(nombre.toLowerCase()))
				id=t.getId();
		}
		 */



		//Si se obtuvo peli, se buscan los datos de la misma.
		PeliRest pelicula=null;
		if(!id.equals(""))
			pelicula= peliculaRest(id);	



		//Crea la pelicula usando los datos de la API Rest si son necesarios
		Pelicula p=createPelicula(nombre,urlVideo,descripcion,anhio,director,actores,urlPortada,valoracion,pelicula);
		peliculaRepository.save(p);


		List<Pelicula> pelis=getAllPelis();

		return new ModelAndView("admin/peliculas").addObject("isRoot",isRoot).addObject("mensjExito", "La pelicula "+p.getNombre()+" ha sido añadida satisfactoriamente").addObject("pelis", pelis);
	}


	/**
	 * Metodo que permite borrar la pelicula basandonos en el id de la misma.
	 * @param id
	 * @return
	 */

	@Secured("ROLE_ADMIN")
	@RequestMapping("/processFormPeli2")	
	public ModelAndView process2(@RequestParam String id){
		//Autorizacion
		String isRoot=adminPanel();
		//Si no encuentra el usuario no lo elimina
		if(peliculaRepository.findById(Long.parseLong(id)).isEmpty()){
			return new ModelAndView("admin/eliminaPelicula").addObject("isRoot",isRoot).addObject("mensjFallo", "La pelicula que quieres borrar no existe");
		}
		//borra la pelicula
		peliculaRepository.delete(Long.parseLong(id));

		List<Pelicula> pelis=getAllPelis();

		return new ModelAndView("admin/peliculas").addObject("isRoot",isRoot).addObject("mensjExito", "La pelicula  ha sido eliminada satisfactoriamente").addObject("pelis", pelis);
	}

	/**
	 * Metodo que procesa el boton de editar una peli, lo que hara que se 
	 * borre la peli de la base de datos y nos mande a una ventana donde volver a añadirla con los mismo o diferente valores en sus campos.
	 * @param nombre
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/processFormPeliEditar")	
	public ModelAndView processFormPeliEditar(@RequestParam String nombre) {

		String isRoot=adminPanel();



		List<Pelicula> pelis2=peliculaRepository.findByNombre(nombre);

		Pelicula p=null;
		if(!pelis2.isEmpty()){
			p=pelis2.get(0);
		}
		peliculaRepository.delete(p);

		return new ModelAndView("admin/editPeli").addObject("isRoot",isRoot).addObject("p", p);
	}



	/**
	 * Metodo para buscar peliculas en la ventana root.
	 * @param nombre
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/processFormPeli3")	
	public ModelAndView process3(@RequestParam String nombre) {
		String isRoot=adminPanel();

		List<Pelicula> pelisAll=getAllPelis();
		List<Pelicula> pelis=new ArrayList<Pelicula>();

		for(Pelicula p:pelisAll){
			if(p.getNombre().toLowerCase().contains(nombre.toLowerCase())){
				pelis.add(p);
			}
		}


		if(pelis.isEmpty())
			return new ModelAndView("admin/buscarPelicula").addObject("mensjFallo", "La pelicula que busca no existe");


		return new ModelAndView("admin/peliBuscada").addObject("isRoot",isRoot).addObject("pelis", pelis);
	} 
	/**
	 * Metodo que toma los valores de la pelicula que estamos editando y los agrega a la base de datos.
	 * @param nombre
	 * @param urlVideo
	 * @param descripcion
	 * @param anhio
	 * @param director
	 * @param actores
	 * @param urlPortada
	 * @param valoracion
	 * @param id
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping("/processFormPeliEdit")	
	public ModelAndView process4(@RequestParam String nombre,@RequestParam String urlVideo,@RequestParam String descripcion,@RequestParam String anhio,@RequestParam String director,@RequestParam String actores,@RequestParam String urlPortada,@RequestParam String valoracion,@RequestParam String id) {
		//autorizacion
		String isRoot=adminPanel();

		Pelicula x=new Pelicula(nombre,urlVideo,descripcion,anhio,director,actores,urlPortada,valoracion);
		peliculaRepository.save(x);

		List<Pelicula> pelis=getAllPelis();

		return new ModelAndView("admin/peliculas").addObject("isRoot",isRoot).addObject("mensjExito", "La pelicula  ha sido editada satisfactoriamente").addObject("pelis", pelis);
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
			return new ModelAndView("admin/anhadeUser").addObject("isRoot",isRoot).addObject("mensjFallo", "Todos los campos son obligatorios");
		}

		//Comprobamos que no esta ya en la base de datos.


		if(userRepository.findByUser(username)!=null)
			return new ModelAndView("admin/anhadeUser").addObject("isRoot",isRoot).addObject("mensjFallo", "El usuario ya existe");

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
		u = userRepository.findByUser(user);
		
		
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


		User u = userRepository.findByUser(userOld);
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
		String isRoot=adminPanel();
		User u = null;
		u = userRepository.findByUser(user);

		if(u == null) {
			List<User> users=getAllUsers();
			return new ModelAndView("admin/users").addObject("isRoot",isRoot).addObject("mensjFallo", "Ha habido un error con el usuario que quieres editar").addObject("users", users);
		}

		return new ModelAndView("admin/editUser").addObject("isRoot",isRoot).addObject("u", u);
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
	 * Obtiene id que sea igual al nombre proporcionado, dentro de la API Rest
	 * @param nombre
	 * @return
	 */
	private BuscaComponenteRest idPeliRest(String nombre){
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
				"http://imdb.wemakesites.net/api").build();

		BuscaComponenteRestService service = adapter.create(BuscaComponenteRestService.class);

		BuscaComponenteRest x = service.getBuscaComponenteRest(nombre);
		return x;
	}
	/**
	 * Obtiene los datos de la pelicula de la API Rest
	 * @param id
	 * @return
	 */
	private PeliRest peliculaRest(String id){
		RestAdapter adapter2 = new RestAdapter.Builder().setEndpoint(
				"http://imdb.wemakesites.net/api/").build();
		PeliRestService service2 = adapter2.create(PeliRestService.class);
		PeliRest pelicula = service2.getPeliRest(id);  
		return pelicula;
	}

	/**
	 * Crea la pelicula modificando los datos si los campos estan vacios usando los de la pelicula creada por la API Rest.
	 * @param nombre
	 * @param urlVideo
	 * @param descripcion
	 * @param anhio
	 * @param director
	 * @param actores
	 * @param urlPortada
	 * @param valoracion
	 * @param pelicula
	 * @return
	 */
	private Pelicula createPelicula(String nombre, String urlVideo, String descripcion, String anhio, String director,
			String actores, String urlPortada, String valoracion, PeliRest pelicula) {


		if(descripcion.equals("")){
			String descr=pelicula.getData().getDescription();
			if(descr.length()>255){
				descripcion=descr.substring(0, 251)+"...";
			}

		}
		if(anhio.equals(""))
			anhio=(String) pelicula.getData().getYear();
		if(director.equals(""))
			director=pelicula.getData().getDirectors().toString().substring(1,pelicula.getData().getDirectors().toString().length()-1);
		if(actores.equals(""))
			actores=pelicula.getData().getCast().toString().substring(1,pelicula.getData().getCast().toString().length()-1);
		if(urlPortada.equals(""))
			urlPortada=(String) pelicula.getData().getImage();
		if(valoracion.equals(""))
			valoracion=(String) pelicula.getData().getReview().getRating();
		return new Pelicula(nombre, urlVideo,descripcion,anhio,director,actores,urlPortada,valoracion);
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
