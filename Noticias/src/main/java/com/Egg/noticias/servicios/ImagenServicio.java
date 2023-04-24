/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Egg.noticias.servicios;

import com.Egg.noticias.Exception.MiException;
import com.Egg.noticias.entidades.Imagen;
import com.Egg.noticias.repositorios.ImagenRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {
    
    @Autowired
    private ImagenRepositorio imagenRepo;
    
    
    
    @Transactional
    public Imagen guardar(MultipartFile img) throws MiException{
        
        if(img!=null){
            try {
                
                Imagen imagen=new Imagen();
                imagen.setMime(img.getContentType());
                imagen.setNombre(img.getName());
                imagen.setContenido(img.getBytes());
                
              return imagenRepo.save(imagen);
                
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
                  
        }
        
         return null;
        
    }
    
    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException{
         
       try {
                
                Imagen imagen=new Imagen();
                if(idImagen != null){
                    Optional<Imagen> respuesta=imagenRepo.findById(idImagen);
                    if(respuesta.isPresent()){
                        imagen=respuesta.get();
                    }
                    
                }
                
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                
              return imagenRepo.save(imagen);
                
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        return null;
    
    }
    
}
