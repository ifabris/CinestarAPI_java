/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ivan
 */
@XmlRootElement(name="filmarchive")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilmArchive {
    
    @XmlElementWrapper
    @XmlElement (name = "film")
    private List<Film> films; 

    public FilmArchive() {
    }

    public FilmArchive(List<Film> films) {
        this.films = films;
    }

    public List<Film> getFilms() {
        return films;
    }
    
    
    
    
}
