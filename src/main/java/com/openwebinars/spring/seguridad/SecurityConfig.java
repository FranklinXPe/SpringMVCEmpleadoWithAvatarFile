package com.openwebinars.spring.seguridad;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 *  1RO: Crearmos una clases la cual extenderemos de WebSecurityConfigurerAdapter
 * La cual lo anotamos con Configuration y EnableWebSecurity=>"Habilita la seguiridad Web de nuestra aplicacion"
 * En resumen para que Spring Security funcione y podamos configurarnos necesitamos que algun bean (dentro del contenedor)
 * implemente la interfaz WebSecurityConfigurer, en este caso usamos WebSecurityConfigurerAdapter, que lo hace.
 *
 
 * Solo sobre escribiendo este metodo, lo que hemos logrado es:
 * - Requisito de atenticacion en todas las URLs
 * - Generacion de un formulario login
 * - Permitir el acceso a un usuario con Username "admin" y Password  "admin"
 * - Permitir hacer el logout
 * - Prevención de ataques CSRF, entre otras (Session Fixation, X-XSS-Protection, Clickjacking)
 * - Integración con metodos del API Servlet HttpServletRequest como lo son:
 * 			* getRemoteUser(), getUserPrincipal(), isUserInRole(), login(), logout()
 * - Ha registrado un filtro de Servlet especial llamado "springSecurityFilterChain", el cual es una cadena de filtros 
 * 	 responsable de toda el proceso de seguridad (AUTENTICACION y AUTORIZACION) como:
 *   proteger las URLs, validar usuario y contraseña, redirigir al formulario login, etc.
 * 	
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	/*===== 2DO: CONFIGURAMOS EL METODO RESPONSABLE DE LA AUTENTICACION ¿Quien soy? ======*/
	// Elegimos el modo de autenticación
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication() // aqui le decimos que elegimos la configuracion de almacenamiento de usuario "En memoria"
		.passwordEncoder(NoOpPasswordEncoder.getInstance())
		.withUser("admin")
		.password("admin")
		.roles("ADMIN");
	}
	
	/*====== 3RO: CONFIGURAMOS EL RESPONSABLE DE LA AUTORIZACION  ¿Siendo quien soy, para qué tengo permiso? =====*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			// Todo lo que este dentro de webjars y css los permitimos. Luego agregamos JPA y H2 y nos aseguramos que no permita a todos el ingreso a la consola H2
				.antMatchers("/webjars/**","/css/**","/h2-console/**").permitAll() 
				.anyRequest().authenticated()//Y cualquier otra peticion debe ser autenticada
				.and() // y
			.formLogin() // definiremos nuestro propio formulario login (no el que por defecto nos trae WebSercurity)
				.loginPage("/login") // de forma que la pagina se encontrará en "/login"
				.permitAll() // y lo permitiremos a todos
				.and()
			.logout();
		
		
		http.csrf().disable();// Desabilitamos el CSRF para que la consola de H2 pueda trabajar
		http.headers().frameOptions().disable(); // Spring Security habilita el bloqueo de los frames. Aqui lo desabilitamos para poder ver la consola de H2
		
		
	}

}
