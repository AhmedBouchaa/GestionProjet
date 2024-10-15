package com.mdw3.appgestionprojets.gestionprojetsmaster.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

@Entity
@Data
public class Projet {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter private String nom;
    @Getter @Setter private LocalDate dateDebut;
    @Getter @Setter private LocalDate dateFin;
    //Association avec Departement
    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonIgnore
    @Getter @Setter private Departement departement;
    //Association*,*
    @ManyToMany(mappedBy = "projets")
    @Getter @Setter private List<Employe> employes;
}
