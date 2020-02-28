package com.openwebinars.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.openwebinars.spring.upload.storage.StorageProperties;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class MyConfig {
	/*===== ESTOS BEANS ME SERVIRAN PARA MANEJAR LOS ERRORES Y OBTENERLO DESDE errors.properties*/
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource
	      = new ReloadableResourceBundleMessageSource();
	 // nos permite cargar un fichero (recurso) donde le diremos donde encontrarlo (la ruta raiz) y la codificacion de este fichero de properties
	    messageSource.setBasename("classpath:errors");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
	@Bean
	public LocalValidatorFactoryBean getValidator() {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	 // hacemos uso del bean anterior para que obtenga el origen de los mensajes de validacion  		
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}

}
