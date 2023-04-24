
package com.Egg.noticias.controladores;

import com.Egg.noticias.Exception.MiException;
import com.Egg.noticias.entidades.Usuario;
import com.Egg.noticias.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/portal")
public class PortalControlador {
    
   @Autowired
   public UsuarioServicio us;
  
   
   
   @GetMapping("/registroUsuario")
   public String registroUsuario(){
       
       return "registroUsuario.html";
       
   }
   
   @PostMapping("/registrar")
   public String crearUsuario(@RequestParam MultipartFile archivo,@RequestParam String nombreUsuario,@RequestParam String password,@RequestParam String password2,ModelMap modelo) throws MiException{
       
        
       try {
             us.crearUsuario(archivo,nombreUsuario, password, password2);
             modelo.put("exito","el usuario fue creado con exito");
              return "redirect:/portal/registroUsuario";
       } catch (MiException e) {
           
           modelo.put("error",e.getMessage());
          
           return "redirect:/portal/registroUsuario";
       }
     
   }
   
     
      @GetMapping("/login")
      public String login(@RequestParam(required=false)String error, ModelMap modelo){
       
       if(error != null){
       
       modelo.put("error","El usuario o la contraseña son incorrectos");
       }
       
       return "Login.html";
       
   }
      
  
    
}
    
    
           
    