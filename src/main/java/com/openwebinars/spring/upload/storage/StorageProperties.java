package com.openwebinars.spring.upload.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Esta clase funciona como un envoltorio de Properties de manera que lo podemos gestionar desde una clase
 * con getters y setters y aplicar la validación de Beans.
 * 
 * Para poder usarlo se necesita la anotacion @ConfigurationProperties con un prefijo. 
 * Y tambien que una clase anotada con @Configuration (ver clase MyConfig.java en paquete spring.config) tuviera la 
 * anotacion @EnableConfigurationProperties(StorageProperties.class) con una referencia a la clase de aqui.
 * 
 *  Explicacion de Servicio de almacenamiento de archivos: https://openwebinars.net/academia/aprende/spring-boot/4846/
 * 
 * */

@ConfigurationProperties(prefix="storage")
public class StorageProperties {

    
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
