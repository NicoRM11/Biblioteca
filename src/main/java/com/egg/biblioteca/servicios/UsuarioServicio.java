package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Transactional
    public void registrar(String nombre , String email , String password , String password2) throws MiException {
         
        validar(nombre, email, password, password2);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        
        //al momento de setear la contraseña, tambien la codificamos con el encoder
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        
        usuario.setRol(Rol.USER);
        
        usuarioRepositorio.save(usuario);
        
    
    }
    
    private void validar(String nombre , String email , String password , String password2) throws MiException {
        
        if (nombre == null) {
            throw new MiException("El nombre no puede ser nulo");
        }

        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        }
        if (password == null || password.isEmpty() || password.length() <= 5 ) {
            throw new MiException("La contraseña no puede ser nula o estar vacia, y debe contener mas de 5 caracteres");
        }
        
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        
        
        
    
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
             
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        
        if (usuario != null) {
            
            //lista de permisos
            List<GrantedAuthority> permisos = new ArrayList();
            
            GrantedAuthority p  = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            
            permisos.add(p);
            
            //recuperar sesion de usuario ya logueado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
                    
            session.setAttribute("usuariosession", usuario);
            
            
            return new User(usuario.getEmail() , usuario.getPassword() , permisos);
             
        } else {
            return null;
        }
        
    }
    
    
    
}
