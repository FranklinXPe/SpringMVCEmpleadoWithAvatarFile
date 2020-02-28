package com.openwebinars.spring.upload.storage;

/**
 * 
 * 
 *  Explicacion de Servicio de almacenamiento de archivos: https://openwebinars.net/academia/aprende/spring-boot/4846/
 * */

public class StorageException extends RuntimeException {
	
	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
