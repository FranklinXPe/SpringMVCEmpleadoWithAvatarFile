package com.openwebinars.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  Ya que para algunos controlladores que solo hacen un trabajo de redirección más no de procesamiento (como en este caso, que solo me lleve a la plantilla de login).
 *  No es necesario crearnos una clase @Controller y colocar las demás anotaciones. En este caso mejor lo manejamos a traves del WebMvcConfigurer
 *  
 ***/
@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// En la ruta /login me llevara a la plantilla "login"
		registry.addViewController("/login").setViewName("login");;
	}

}
