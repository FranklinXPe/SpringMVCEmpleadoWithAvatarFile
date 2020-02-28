package com.openwebinars.spring.upload.storage;

/**
 *  Explicacion de Servicio de almacenamiento de archivos: https://openwebinars.net/academia/aprende/spring-boot/4846/
 * */
public class StorageFileNotFoundException extends StorageException {
	
	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
