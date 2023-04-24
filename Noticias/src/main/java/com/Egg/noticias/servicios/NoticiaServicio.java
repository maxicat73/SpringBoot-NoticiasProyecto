/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Egg.noticias.servicios;

import com.Egg.noticias.Exception.MiException;
import com.Egg.noticias.entidades.Noticia;
import com.Egg.noticias.repositorios.NoticiaRepositorio;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @Transactional
    public void crearNoticia(String titulo, String cuerpo) throws MiException {

        validar(titulo, cuerpo);
        Noticia noticia = new Noticia();

        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);

        noticiaRepositorio.save(noticia);

    }

    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = noticiaRepositorio.buscarOrdenado();

        return noticias;
    }

    @Transactional
    public void modificarNoticia(Noticia noti) {

        noticiaRepositorio.save(noti);

    }
    
    
    public void eliminarNoticia(Integer id) {

        noticiaRepositorio.deleteById(id);

    }

    public Noticia buscarPorID(Integer id) {

        Optional<Noticia> oNoticia = noticiaRepositorio.findById(id);
        Noticia noticia = null;
        if (oNoticia.isPresent()) {

            noticia = oNoticia.get();

        }
        return noticia;
    }
    
    

    public void validar(String titulo, String cuerpo) throws MiException {

        if (titulo == null || titulo.isEmpty()) {
            throw new MiException("El titulo no puede estar vacio");

        }
        if (cuerpo == null || cuerpo.isEmpty()) {

            throw new MiException("El cuerpo no puede estar vacio");
        }

    }

}
