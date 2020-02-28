package com.openwebinars.spring.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.openwebinars.spring.modelo.Empleado;
import com.openwebinars.spring.servicios.EmpleadoService;
import com.openwebinars.spring.servicios.EmpleadoServiceMemory;
import com.openwebinars.spring.upload.storage.StorageService;

@Controller
public class EmpleadoController {

	
	/*
	 * Este atributo "servicio" antes era declarado como EmpleadoServiceMemory que hora ya no existe debido a que ahora tambien tenemos otro repositorio de almacenamiento
	 * que es la base de datos H2. Debido a que ahora con esta refactorizacion hemos creado una interfaz "EmpleadoService" el cual es implementado por nuestro JPARespository
	 * Spring llamara al core container para buscar a alguien que lo este implementando
	 * */
	@Autowired
	public EmpleadoService servicio; // Ya que la clase EmpleadoServiceMemory tiene el @Primary lo llamara por defecto, si no estuviera disponible, llamará a EmpleadoServiceMemory

	@Autowired
	private StorageService storageService;
	
	/*================= AGREGAMOS EL GET Y POST PARA EL FORMULARIO DE CREAR EMPLEADO =======*/
	
	// el GET es el que prepara el formulario para que el usuario comience, en este caso, a ingresar nuevo empleado
	@GetMapping({ "/", "/empleado/list" }) // Reutilizamos este metodo para poder tambien realizar la busqueda
	public String listado(Model model, @RequestParam(name = "q", required = false) String  query) {
		List<Empleado> resultado=(query ==null)? servicio.findAll() : servicio.buscador(query);
		model.addAttribute("listaEmpleados", resultado);
		return "list";
	}

	@GetMapping("/empleado/new")
	public String nuevoEmpleadoForm(Model model) {
		model.addAttribute("empleadoForm", new Empleado());
		return "form";
	}

	
	/*================= AGREGAMOS EL GET Y POST PARA EL FORMULARIO DE EDITAR EMPLEADO =========*/
	
	// En esta peticion que va gestionar la subida del fichero tenemos que añadir un @RequestParam de tipo MultipartFile
	// Necesitamos que cuando creamos un nuevo empleado, el fichero se almacene en el servidor
	// Explicacion de Servicio de almacenamiento de archivos: https://openwebinars.net/academia/aprende/spring-boot/4846/ 
	
	// El submit que procesara lo que se envio desde el front
	// Los BeanValidation siempre van declarados en los PostMapping
	@PostMapping("/empleado/new/submit")
	public String nuevoEmpleadoSubmit(@Valid @ModelAttribute("empleadoForm") Empleado nuevoEmpleado,
			BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
		//@RequestParam("file") el nombre file es porque lo llamamos asi en el formulario

		if (bindingResult.hasErrors()) {			
			return "form";	
		} else {
			if (!file.isEmpty()) {
				// Lógica de almacenamiento del fichero cuando no esta vacio: Una vez que lo recibamos debemos saber dónde lo vamos a almacenar
				
				// Obligaremos a que el avator este presente, sino no envio nada será nulo
				// storageService.store nos devuelve la ruta del fichero el cual lo almacenamos  junto a los datos del empleado
				String avatar=storageService.store(file, nuevoEmpleado.getId());
				
				//Construimos la URI de la ruta de la imagen que esta en nuestro sistema de Almacenamiento de imagenes.
				// haciendo uso de MvcUriComponentBuilder construiremos la URI completa ejem: localhost:9000/files/my_avatar.jpg
				nuevoEmpleado.setImagen(MvcUriComponentsBuilder
						
						// Uso la ruta de mapping definido en el metodo "serverFile" y junto con el nombre del archivo avatar construye la URI, por partes
							.fromMethodName(EmpleadoController.class, "serverFile", avatar) 
							.build().toUriString()
						);
						
			}
			
			// Aqui nuevoEmpleado ya tiene además de los datos del empleado, la imagen pero como URI
			servicio.add(nuevoEmpleado);
			//Cuando lo pase a la vista añadiremos lo necesario para que se pueda mostrar el avatar.
			//estableceremos en la vista algun avatar aleatorio para lo que no tengan ninguna ruta o sea nulo
			return "redirect:/empleado/list";
		}
	}

	// Preparamos el formulario con los datos del formulario que esta solicitando a traves del "id"
	@GetMapping("/empleado/edit/{id}")
	public String editarEmpleadoForm(@PathVariable long id, Model model) {
		
		// En lugar de inyectar uno nuevo ( new Empleado() ), lo recuperamos mediante el servicio
		Empleado empleado = servicio.findById(id);
		if (empleado != null) {
			model.addAttribute("empleadoForm", empleado);
			return "form";
		} else
		//  Si no lo encontramos, lo cual sería un poco extraño ya que le estamos pasando un Id desde el front (tal vez con mailintencion), pero bueno puede darse
		// lo redirigimos al formulario de creación de un nuevo empleado
			return "redirect:/empleado/new";
	}

	// Los BeanValidation siempre van declarados en los PostMapping
	@PostMapping("/empleado/edit/submit")
	public String editarEmpleadoSubmit(@Valid @ModelAttribute("empleadoForm") Empleado nuevoEmpleado,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// en este caso le diremos al frontend los errores y sus mensajes que encontraramos en cada campo del formulario  (si es que lo hubiera)
			return "form";	
		} else {
			servicio.edit(nuevoEmpleado);
			return "redirect:/empleado/list";
		}
	}
	
	
	
	@GetMapping("/files/{filename:.+}")// esto significa que en la url acepta el caracter "." para que no se corte
	@ResponseBody
	public ResponseEntity<Resource> serverFile(@PathVariable String filename){
		// Explicacion de Servicio de almacenamiento de archivos: https://openwebinars.net/academia/aprende/spring-boot/4846/ A partir del minuto 02:50 
		// Retornaremos un entity de tipo recurso, el cual le damos como parametro en nombre del archivo
		// Notar que aqui la abstracción de este servicio loadAsResource nos oculta qué hace adentro y de donde esta recuperando el archivo avatar
		// Por lo que me deja a libertad la implementación de esta interfaz StorageService para cualquier tipo Sistema de Almacenamiento de archivos (ejem: AWS, Azure, Firebase, etc)
		Resource file= storageService.loadAsResource(filename);
		
		return ResponseEntity.ok().body(file);
	}


}
