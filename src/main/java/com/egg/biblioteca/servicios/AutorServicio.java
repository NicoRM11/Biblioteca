package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException {
        
        validar(nombre);
        
        Autor autor = new Autor();

        autor.setNombre(nombre);

        autorRepositorio.save(autor);

    }

    public List<Autor> listarAutor() {

        List<Autor> autores = new ArrayList();

        autores = autorRepositorio.findAll();

        return autores;
    }
    
    @Transactional
    public void modificarAutor(String id , String nombre) throws MiException{
        
        validar(nombre);
       
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            
            autor.setNombre(nombre);
            
            autorRepositorio.save(autor);
        }
    
    }
    
    public Autor getOne(String id){
        return autorRepositorio.getOne(id);
    }
    
    
    private void validar(String nombre) throws MiException{
       
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
    
    }
    
    

}
