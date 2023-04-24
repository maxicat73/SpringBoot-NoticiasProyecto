package com.Egg.noticias.servicios;

import com.Egg.noticias.Enumeraciones.Rol;
import com.Egg.noticias.Exception.MiException;
import com.Egg.noticias.entidades.Imagen;
import com.Egg.noticias.entidades.Noticia;
import com.Egg.noticias.entidades.Periodista;
import com.Egg.noticias.entidades.Usuario;
import com.Egg.noticias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{

    @Autowired
    private UsuarioRepositorio ur;
    
    @Autowired
    private ImagenServicio imagenServicio;

    
     @Transactional
    public void crearUsuario(MultipartFile archivo, String nombreUsuario, String password, String password2) throws MiException {
         
        Usuario us = new Usuario();
        
        
        validar(nombreUsuario, password, password2);

        us.setNombreUsuario(nombreUsuario);
        us.setPassword(new BCryptPasswordEncoder().encode(password));
        us.setRol(Rol.USER);
        us.setFechaDeAlta(new Date());
        us.setActivo(true);
        
        Imagen imagen=imagenServicio.guardar(archivo);
        us.setImagen(imagen);
//        compararNombre(us,nombreUsuario);
        ur.save(us);
        
    }

    @Transactional
    public void modificarUsuario(Usuario us){
        
        ur.save(us);
        
    }
    
    public void eliminarUsuario(String id){
        
        ur.deleteById(id);
    }
    
    
    public ArrayList<Usuario> listarUsuario(){
        
        ArrayList<Usuario> listaUsuarios=(ArrayList<Usuario>) ur.findAll();
        
        return listaUsuarios;
        
        
    }
    
    
    
    public Usuario buscarPorID(String id){
        
        Optional<Usuario> oUsuario = ur.findById(id);
        Usuario Usuario = null;
        if (oUsuario.isPresent()) {

            Usuario = oUsuario.get();

        }
        return Usuario;
    }
        
          @Transactional
    public void crearPeriodista(String id) throws MiException{
             
        Usuario u = buscarPorID(id);
        Periodista p = new Periodista();
        
        p.setId("");
        p.setNombreUsuario(u.getNombreUsuario());
        p.setPassword(u.getPassword());
        p.setFechaDeAlta(u.getFechaDeAlta());

        ur.delete(u);
        ur.save(p);
    }
    
    @Transactional
    public void modificarPeriodista(String id, Integer sueldoMensual, Boolean activo) throws MiException{
             
        Usuario u = buscarPorID(id); 
        Periodista p = new Periodista();
        
        p.setId(u.getId());
        p.setNombreUsuario(u.getNombreUsuario());
        p.setPassword(u.getPassword());
        p.setFechaDeAlta(u.getFechaDeAlta());
        p.setSueldoMensual(sueldoMensual);
        p.setActivo(activo);
        ur.save(p);
    }
    
    public List<Usuario> listarPeriodistas() {
        List<Usuario> periodistas = new ArrayList();
        periodistas = ur.buscarPeriodistas();
        System.out.println(periodistas.toString());
    return periodistas;
    
    }
    
    

    
//    @Transactional
//    public void modificarROL(String id){
//        
//   Optional<Usuario> user=ur.findById(id);
//   
//   if(user.isPresent()){
//       
//      Usuario usuario=user.get();
//      
//      if()
//      
//       
//   }
//    
//    user.setRol(Rol.USER);
//        
//        
//    }
//    
    
//    public void compararNombre(Usuario us,String nombreUsuario) throws MiException{
//        
//       
//        Usuario usuario2=ur.buscarPorNombre(nombreUsuario);
//         System.out.println(usuario2.getNombreUsuario().toString());
//         System.out.println(us.getNombreUsuario().toString());
//        if(usuario2.getNombreUsuario().toString().equals(nombreUsuario)){
//            
//          throw new MiException("el nombre de usuario Ya existe");
//            
//        }
//    }
    
    
//    

    private void validar(String nombreUsuario, String password, String password2) throws MiException {

        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiException("El nombre de usuario no puede estar vacio o Nulo");

        }
        
      
        
      
      if (password.isEmpty() || password==null || password.length()<=5) {
            throw new MiException("Las contraseñas no pueden estar vacias y tener menos de 5 caracteres ");
      }
      
      if(!password.equals(password2)){
          throw new MiException("las contraseñas deben coincidir");
      }
            
    }
    
    
    

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

        Usuario u = ur.buscarPorNombre(nombreUsuario);

        if (u != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + u.getRol().toString());

            permisos.add(p);
            
            ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session= attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", u);
            User user = new User(u.getNombreUsuario(), u.getPassword(), permisos);

            return user;
        } else {
            return null;
        }

    }

    
    
}
