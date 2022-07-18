package com.egg.biblioteca;

import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    
    //metodo para encriptar la contraseña de cuenta
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        
        //que servicio se utiliza para autenticar a usuario, en este caso Usuario Servicio
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
              //se autentica primero               //luego se pasa el codificador de contraseñas
    
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.
            authorizeRequests() //muestra las vistas que se le indique en las funcionalidades
                             .antMatchers("/admin/*").hasRole("ADMIN") //condicionamos para el controlador completo de admin, que solo puedan ingresar usuarios admin
                             .antMatchers("/css/*" , "/js/*" , "/img/*" , "/**") //en este caso para que las vistas indicadas sea visible para cualquier usuario
                             .permitAll()
            .and().formLogin() //configuracion para loguearse(usar login)
                             .loginPage("/login")
                             .loginProcessingUrl("/logincheck") //URL por la cual Spring Security va a autenticar a un usuario
                             .usernameParameter("email") //parametro de nombre de usuario
                             .passwordParameter("password") //parametro de constraseña de usuario
                             .defaultSuccessUrl("/inicio") //si se genera un login correcto, URL a la que se dirige si todo esta bien
                             .permitAll()
            .and().logout()  //configuracion para la salida de nuestro sistema
                             .logoutUrl("/logout") //cuando ingrese a URL , se cierre sesion
                             .logoutSuccessUrl("/login") //si sale bien, manda a URL indicada
                             .permitAll()
            .and().csrf()    //configuracion para deshabilitar una config que trae Spring Security de manera intrinseca
                             .disable(); 
        
        
    }
    
}
