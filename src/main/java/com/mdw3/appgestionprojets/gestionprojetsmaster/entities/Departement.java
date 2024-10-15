package com.mdw3.appgestionprojets.gestionprojetsmaster.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Departement {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter private String nom;
    //Association avec Projet
    @OneToMany(mappedBy = "departement")
    @JsonIgnore
    private List<Projet> projets;
}