package com.openwebinars.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.openwebinars.spring.modelo.Empleado;
import com.openwebinars.spring.repositorios.EmpleadoRepository;
import com.openwebinars.spring.upload.storage.StorageService;

import io.netty.util.internal.ThreadLocalRandom;

/**
 * ESTE PROYECTO NECESITA QUE ESTE CORREINDO LA BASE DE DATOS REDIS PARA PODER GESTINAR LAS SESIONES EN  LA APLICACION UNA VEZ LOGUEADOS.
 * EN ESTE CASO REDIS LO CONFIGURAMOS EN Docker por lo cual se sugiere arrancar la configuracion realizada:
 * Por ejemplo: docker container start myredis
 * 
 * 
 * Tambien una vez logueado con  admin:admin podremos acceder a la consola de gestion de la base de datos H2 para guardar la entidad Empleado 
 * mediante la siguiente direccion: http://localhost:9000/h2-console/
 * 
 * Recordar que: (ver archivo: application.properties)
 * 
 * 	JDBC URL: jdbc:h2:./openwebinars
 *  USER NAME: sa
 *  PASSWORD: (vacio)
 * */

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	/**
	 * Se usa cuando el proyecto integro la parte de subida de archivos para los avatars
	 * 
	 * Este bean se inicia al lanzar la aplicación. Nos permite inicializar el almacenamiento
	 * secundario del proyecto (osea el lugar donde se estana almacenando los avatars que es en la raiz/upload-dir)
	 * 
	 * Explicacion de Servicio de almacenamiento de archivos: https://openwebinars.net/academia/aprende/spring-boot/4846/
	 * 
	 * @param storageService Almacenamiento secundario del proyecto
	 * @return
	 */
	@Bean
    public CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
	
	
	/**
	 * 
	 * Este CommandLineRunner se ejecutará al iniciar la aplicación (al igual como lo hace el anterior)
	 * En este caso ingresará dos empleados a la tabla de la base de datos H2
	 * */
	@Bean
	public CommandLineRunner initData(EmpleadoRepository repositorio) {
		return (args) ->{
			/**  == Esto hacia cuando la base de datos era en Memoria
			Empleado empleado=new Empleado("Luis Miguel López","luismi@mail.com","453463456");
			Empleado empleado2=new Empleado("José García","jose.garcia@mail.com","676766766");
			
			repositorio.save(empleado);
			repositorio.save(empleado2);
			
			repositorio.findAll().forEach(System.out::println);
			*/
			
		
			
			/** === Esto usamos cuando estabamos probando el CrudRepository en H2
			repositorio.saveAll(Arrays.asList(	new Empleado(1,"Antonio García","antonio@mail.com","987456377"),
						new Empleado(2, "Maria Lopez","maria@mail.com","987676577"),
						new Empleado(3,"Angel Antunez","angel@mail.com","956768745")
						
							)
					);
					*/
			
			// Ahora generamos un random de Empleados con JPARepository

			List<String> nombres = Arrays.asList("Lucas", "Hugo", "Martín", "Daniel", "Pablo", "Alejandro", "Mateo",
					"Adrián", "Álvaro", "Manuel", "Leo", "David", "Mario", "Diego", "Javier", "Luis", "Marcos", "Juan",
					"José", "Gonzalo", "Lucía", "Sofía", "María", "Martina", "Paula", "Julia", "Daniela", "Valeria",
					"Alba", "Emma", "Carla", "Sara", "Noa", "Carmen", "Claudia", "Valentina", "Alma", "Ana", "Luisa",
					"Marta");

			List<String> apellidos = Arrays.asList("García", "González", "López", "Rodríguez", "Martínez", "Sánchez",
					"Pérez", "Gómez", "Martín", "Saez", "Velasco", "Moya", "Soler", "Parra", "Bravo", "Rojas", "Romero",
					"Sosa", "Torres", "Álvarez", "Flores", "Molina", "Ortiz", "Silva", "Torres");


			
			Collections.shuffle(nombres); // barajamos los nombres

			repositorio.saveAll(IntStream.rangeClosed(1, nombres.size()).mapToObj((i) -> {
				String nombre = nombres.get(i-1);
				String apellido1 = apellidos.get(ThreadLocalRandom.current().nextInt(apellidos.size()));
				String apellido2 = apellidos.get(ThreadLocalRandom.current().nextInt(apellidos.size()));
				return new Empleado(String.format("%s %s %s", nombre, apellido1, apellido2), 
						String.format("%s.%s@openwebinars.net", nombre.toLowerCase(), apellido1.toLowerCase()), "954000000");
			}).collect(Collectors.toList()));
			
			
		};
	}
}
