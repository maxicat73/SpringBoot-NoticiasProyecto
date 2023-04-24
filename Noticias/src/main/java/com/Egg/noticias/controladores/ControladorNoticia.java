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
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ControladorNoticia {

    @Autowired
    private NoticiaServicio noticiaServicio;

    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/cargar")
    public String cargarNoticia() {

        return "cargar.html";
    }
    
    
    @PostMapping("/llenar")
    public String llenar(@RequestParam String titulo, @RequestParam String cuerpo, ModelMap modelo) throws MiException {

        try {
            noticiaServicio.crearNoticia(titulo, cuerpo);
            modelo.put("exito", "La noticia fue cargada con Exito");
             
        } catch (MiException e) {

            modelo.put("error", e.getMessage());
            return "cargar.html";
        }

        return "redirect:/panelAdmin";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/listar")
    public String listaNoticias(ModelMap modelo,HttpSession session) {

        Usuario logueado=(Usuario) session.getAttribute("usuariosession");
        
        if(logueado.getRol().toString().equals("ADMIN") || logueado.getRol().toString().equals("PERIODISTA") ){
            return "redirect:/panelAdmin";
        }
        
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("lista", noticias);
        
        
        return "inicio.html";

    }

    @GetMapping("/mostrarNoticia/{id}")
    public String mostrarNoticia(ModelMap modelo, @PathVariable(value = "id") Integer id) {

        modelo.addAttribute("id", noticiaServicio.buscarPorID(id));

        return "Noticia.html";

    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/panelAdmin")
    public String panelAdmin(ModelMap modelo) {

        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noti", noticias);

        return "panelAdmin.html";
    }

    
    @PostMapping("/panelAdmin")
    public String modificarNoticia(@RequestParam Integer id, @RequestParam String titulo, @RequestParam String cuerpo, ModelMap modelo) throws MiException {

        noticiaServicio.validar(titulo, cuerpo);

        Noticia noticiaEncontrada = noticiaServicio.buscarPorID(id);

        noticiaEncontrada.setTitulo(titulo);
        noticiaEncontrada.setCuerpo(cuerpo);

        noticiaServicio.modificarNoticia(noticiaEncontrada);

        return "redirect:/panelAdmin";

    }

    @GetMapping("/panelAdmin/editar/{id}")
    public String modificarForm(@PathVariable Integer id, ModelMap modelo) {

        modelo.addAttribute("noticia", noticiaServicio.buscarPorID(id));

        return "editarNoti.html";
    }

    @GetMapping("/panelAdmin/eliminar/{id}")
    public String eliminarNoticia(@PathVariable Integer id) {

        noticiaServicio.eliminarNoticia(id);

        return "redirect:/panelAdmin";
    }

  
    
    
}
