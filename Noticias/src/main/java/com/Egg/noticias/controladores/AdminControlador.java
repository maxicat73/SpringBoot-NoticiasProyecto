/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Egg.noticias.controladores;

import com.Egg.noticias.Exception.MiException;
import com.Egg.noticias.entidades.Noticia;
import com.Egg.noticias.entidades.Usuario;
import com.Egg.noticias.servicios.NoticiaServicio;
import com.Egg.noticias.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author mexi_
 **/
 @Controller
@RequestMapping("/dashboard")
 public class AdminControlador {

      
    @Autowired
    private NoticiaServicio ns;
    
    @Autowired
    private UsuarioServicio us;
    
    @GetMapping("/admin")
    public String panelAdmin(ModelMap modelo){
        
        List<Noticia> noticias = ns.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        
    return "PanelAdmin.html";
    }
    
    @GetMapping("/usuarios")
    public String usuarios(ModelMap modelo,ModelMap modelo2){
        
        List<Usuario> usuarios = us.listarUsuario();
        modelo.addAttribute("usuarios", usuarios);
        
         List<Usuario> periodistas = us.listarPeriodistas();
        modelo2.addAttribute("periodistas", periodistas);
    
    return "Usuarios.html";
    }
   
    @GetMapping("/crear/{id}")
    public String crearPeriodista(@PathVariable String id, ModelMap modelo) throws MiException{
        
        try {
            us.crearPeriodista(id);
            modelo.put("exito","Periodista creado con exito");

        }catch(MiException ex){
            
            modelo.put("error",ex.getMessage());
            return "redirect:/dashboard/usuarios";
            
        }return "redirect:/dashboard/usuarios";
    }
    
    @GetMapping("/periodistas")
    public String periodistas(ModelMap modelo){
        
        List<Usuario> periodistas = us.listarPeriodistas();
        modelo.addAttribute("periodistas", periodistas);
        
    return "Usuarios.html";
    }
    
    @PostMapping("/modificar")
    public String modificarPeriodista(@RequestParam String id,@RequestParam Boolean activo,@RequestParam Integer sueldoMensual, ModelMap modelo) throws MiException{
        
        try {
            
            us.modificarPeriodista(id, sueldoMensual, activo);
            modelo.put("exito","Periodista modificado con exito");

        }catch(MiException ex){
            
            modelo.put("error",ex.getMessage());
            return "redirect:/dashboard/usuarios";
            
        }return "redirect:/dashboard/usuarios";
    }
    
     @GetMapping("/eliminar/{id}")
   public String eliminarUs(@PathVariable String id){
       
       us.eliminarUsuario(id);
       
       return "redirect:/dashboard/usuarios";
}
 
}
    

