package com.openwebinars.spring.servicios;

import java.util.List;

import com.openwebinars.spring.modelo.Empleado;



/**
 * Esta interfaz tendra implementado los metodos CRUD del empleado
 * 
 * */
public interface EmpleadoService {
	
	public Empleado add(Empleado e);
	public List<Empleado> findAll();
	public Empleado findById(long id);
	public Empleado edit(Empleado e);
	public List<Empleado> buscador(String query);

}
