/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Egg.noticias.entidades;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mexi_
 */
@Entity
@DiscriminatorValue("PERIODISTA")
@Getter
@Setter
@NoArgsConstructor 
public class Periodista extends Usuario{
    
    @OneToMany(mappedBy="creador")
    private List<Noticia> misNoticias;
    
   
    private Integer sueldoMensual;

}

