package com.mdw3.appgestionprojets.gestionprojetsmaster.controller;

import com.mdw3.appgestionprojets.gestionprojetsmaster.entities.Departement;
import com.mdw3.appgestionprojets.gestionprojetsmaster.entities.Employe;
import com.mdw3.appgestionprojets.gestionprojetsmaster.entities.Projet;
import com.mdw3.appgestionprojets.gestionprojetsmaster.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mdw3.appgestionprojets.gestionprojetsmaster.repositories.EmployeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employes")
public class EmployeController {
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private ProjetRepository projetRepository;
    /////////////////////////////////1/////////////////////////////////
    @PostMapping
    public Employe createEmploye(@RequestBody Employe employe){
        if(employe == null){
            throw new RuntimeException("L'employe ne peut pas être null");
        }
        if(employe.getNom() == null || employe.getPrenom() == null || employe.getEmail() == null || employe.getTel()==null){
            throw new RuntimeException(employe.getNom()+"tous les donnes d'employe sont obligatoire");
        }
        Optional<Employe> existingEmployee = employeRepository.findByPrenomAndNom(employe.getPrenom(),employe.getNom());
        if(existingEmployee.isPresent()){
            throw new RuntimeException("Un employe a deja ce nom et ce prenom");
        }
        Employe savedEmploye = employeRepository.save(employe);
        return savedEmploye;
    }
    /////////////////////////////////2/////////////////////////////////

    @GetMapping
    public List<Employe> getAllEmployes(){
        List<Employe> employes = employeRepository.findAll();

        if (employes.isEmpty()) {
            throw new RuntimeException("Aucun employe trouvé");
        }

        return employes;
    }
    /////////////////////////////////3/////////////////////////////////
    @GetMapping("/{id}")
    public Employe getEmployeById(@PathVariable Long id) {
        // Trouver l'employe par ID
        Optional<Employe> employe = employeRepository.findById(id);
        if (employe.isEmpty()) {
            // Lancer une exception si l'employe n'existe pas
            throw new RuntimeException("Employe non trouvé");
        }
        // Retourner le département trouvé
        return employe.get();
    }//FIn
    /////////////////////////////////4/////////////////////////////////
    @DeleteMapping("/{id}")
    public String deleteEmploye(@PathVariable Long id) {
        // Vérifier si l'employe existe
        Optional<Employe> employe = employeRepository.findById(id);
        if (employe.isEmpty()) {
            // Lancer une exception si l'employe n'existe pas
            throw new RuntimeException("employe non trouvé");
        }
        // Supprimer le mais avant il faut tester qu'aucun projet
        // // n'est pas liee par cet employe
        if (employe.get().getProjets().isEmpty()) {
            employeRepository.deleteById(id);
            // Retourner un message de succès
            return "employe supprimé avec succès";
        }
        else {
            // Lancer une exception si l'employe n'existe pas
            throw new RuntimeException("Impossible de supprimer l'employee car des projets y sont affectés");
        }
    }//Fin
    /////////////////////////////////5/////////////////////////////////
    @PutMapping
    public Employe updateEmploye(@RequestBody Employe upEmploye) {
        // Trouver l'employe existant par ID
        Employe existingEmploye = employeRepository.findById(upEmploye.getId())
                .orElseThrow(() -> new RuntimeException("employe non trouvé"));
        // Vérifier si le nom proposé existe déjà pour un autre employee
        Optional<Employe> employe = employeRepository.findByNom(upEmploye.getNom());
        if (employe.isPresent() && (!employe.get().getId().equals(upEmploye.getId())))
            throw new RuntimeException("Un employee avec ce nom existe déjà");
        // Mettre à jour les informations d'employee
        existingEmploye.setNom(upEmploye.getNom());
        existingEmploye.setPrenom(upEmploye.getPrenom());
        existingEmploye.setEmail(upEmploye.getEmail());
        existingEmploye.setTel(upEmploye.getTel());
        // Sauvegarder le département mis à jour
        return employeRepository.save(existingEmploye);
    }//Fin
    /////////////////////////////////6/////////////////////////////////
    @GetMapping("/{id}/projets")
    public List<Projet> getProjetsEmployeId(@PathVariable Long id) {
        // Cherche l'employe par son ID
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("employe non trouvé"));
        // Récupère la liste des projets
        List<Projet> projets = employe.getProjets();
        // Vérifie si la liste des projets est vide
        if (projets.isEmpty()) {
            throw new RuntimeException("Aucun projet n'est associé à cet employe");
        }
        return projets;
    }//Fin
    /////////////////////////////////7/////////////////////////////////
    @GetMapping("/recherche")
    public List<Employe> searchEmployes(@RequestParam String keyword) {
        List<Employe> resultats = employeRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword);
        if (resultats.isEmpty()) {
            throw new RuntimeException("Aucun employe trouvé pour le mot-clé: " + keyword);
        }
        return resultats;
    }//Fin
    /////////////////////////////////8/////////////////////////////////
    @PostMapping("/add/{projetId}/to/{employeId}")
    public String addProjetToEmploye(@PathVariable Long projetId, @PathVariable Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("employe introuvable"));

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        List<Projet> listprojet = employe.getProjets();
        // Vérification si le projet est déjà associé au employe
        if (listprojet.contains(projet)) {
            return "Cet projet est déjà assigné à cet employe.";
        }
        employe.getProjets().add(projet);
        projet.getEmployes().add(employe);
        employeRepository.save(employe);
        projetRepository.save(projet);
        return "Projet affecté";
    }//Fin
    /////////////////////////////////9/////////////////////////////////
    @DeleteMapping("/remove/{projetId}/from/{employeId}")
    public String removeProjetFromEmploye(@PathVariable Long projetId, @PathVariable Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("employe introuvable"));
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));
        // Vérifier si le projet est bien associé au employe
        if (!employe.getProjets().contains(projet)) {
            throw new RuntimeException("Le projet n'est pas associé à cet employe.");
        }
        employe.getProjets().remove(projet);
        projet.getEmployes().remove(employe); // Délier le projet de tout département
        employeRepository.save(employe);
        projetRepository.save(projet);
        return "Projet retiré d'employe avec succès";
    }//Fin
}
