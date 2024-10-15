package com.mdw3.appgestionprojets.gestionprojetsmaster.repositories;

import com.mdw3.appgestionprojets.gestionprojetsmaster.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mdw3.appgestionprojets.gestionprojetsmaster.entities.Employe;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe,Long> {
    Optional<Employe> findByNom(String tel);
    Optional<Employe> findByPrenomAndNom(String prenom, String nom);
    @Query("from Employe e where upper(e.nom) like upper(concat('%', :keyword, '%')) or upper(e.prenom) like upper(concat('%',:keyword,'%')) ")
    List<Employe> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(@Param("keyword") String keyword);
}
