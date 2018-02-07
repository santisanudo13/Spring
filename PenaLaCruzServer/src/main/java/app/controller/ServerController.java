package app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import app.dao.*;












@Controller

public class ServerController{

	@Autowired
	private ArchivoRepository archivoRepository;





	/**
	 * Mapeo a direccion peliculas añadiendo objetos de autoridad del usuario y lista total de peliculas.
	 * @return
	 */
	@Secured ({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping("/server")
	public ModelAndView server() {
		Iterable<Archivo> files = archivoRepository.findAll();
		return new ModelAndView("server/home").addObject("files", files);
	}

	@Secured ({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping("/buscarArchivo")
	public ModelAndView buscarArchivo(@RequestParam String nombre) {
		Iterable<Archivo> files = archivoRepository.findAll();

		List<Archivo> list = new ArrayList<Archivo>();
		for(Archivo f : files) {
			if(f.getNombre().toLowerCase().contains(nombre.toLowerCase())){
				list.add(f);
			}
		}

		return new ModelAndView("server/buscarArchivo").addObject("files", list);
	}

	@Secured ({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping("/uploadFile")
	public ModelAndView uploadFile() {
		Iterable<Archivo> files = archivoRepository.findAll();
		return new ModelAndView("server/uploadFile");
	}

	@Secured ({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(value = "/metodoSubirArchivo", method = RequestMethod.POST)
	public ModelAndView metodoSubirArchivo(@RequestParam("file") MultipartFile myFile,  @RequestParam("descripcion") String descripcion) {


		Archivo f = new Archivo();
		try {
			f.setFile(myFile.getBytes());
		} catch (IOException e) {

		}
		f.setNombre(myFile.getOriginalFilename());
		f.setTipo(myFile.getContentType());
		f.setDescripcion(descripcion);

		archivoRepository.save(f);
		// ... do whatever you want with 'myFile'
		// Redirect to a successful upload page
		Iterable<Archivo> files = archivoRepository.findAll();
		return new ModelAndView("server/home").addObject("files", files);

	} 

	@Secured ({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping("/metodoEliminarArchivo")
	public ModelAndView metodoEliminarArchivo(@RequestParam("id") Long id) {


		Archivo f = null;

		f = archivoRepository.findOne(id);
		if(f != null)
			archivoRepository.delete(f);
		// ... do whatever you want with 'myFile'
		// Redirect to a successful upload page
		Iterable<Archivo> files = archivoRepository.findAll();
		return new ModelAndView("server/home").addObject("files", files);

	} 

	// Using ResponseEntity<ByteArrayResource>
	@Secured ({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping("/metodoDescargarArchivo")
	public ResponseEntity<ByteArrayResource> metodoDescargarArchivo(@RequestParam("id") Long id) throws IOException {

		Archivo f = null;
		f = archivoRepository.findOne(id);



		byte[] data = f.getFile();
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,		"attachment;filename=" + f.getNombre()).contentLength(data.length).body(resource);
	}
}
