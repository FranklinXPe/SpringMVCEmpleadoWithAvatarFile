package com.openwebinars.spring.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.openwebinars.spring.modelo.Empleado;
import com.openwebinars.spring.repositorios.EmpleadoRepository;

@Primary // PARA EVITAR LA COLISIÓN CON LA OTRA CLASE "EmpleadoServiceMemory" que tambien implementa la interfaz "EmpleadoService"
@Service("empleadoServiceDB")
public class EmpleadoServiceDB implements EmpleadoService {
	
	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Override
	public Empleado add(Empleado e) {
		
		return empleadoRepository.save(e);
	}

	@Override
	public List<Empleado> findAll() {
		
		return empleadoRepository.findAll();
	}

	@Override
	public Empleado findById(long id) {
	
		return empleadoRepository.findById(id).orElse(null);
	}

	@Override
	public Empleado edit(Empleado e) {
		
		return empleadoRepository.save(e);
	}
	
	@Override
	public List<Empleado> buscador(String cadena){
		// return empleadoRepository.findByNombreContainsIgnoreCaseOrEmailContainsIgnoreCaseOrTelefonoContainsIgnoreCase(cadena, cadena, cadena);
		//return empleadoRepository.encuentraPorNombreEmailOTelefono(cadena);
		
		return empleadoRepository.encuentraPorNombreEmailOTelefonoNativa(cadena);
	}
	

}
