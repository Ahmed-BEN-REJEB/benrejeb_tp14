package ssii.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ssii.entity.Participation;
import ssii.entity.Personne;
import ssii.entity.Projet;
import ssii.dao.ParticipationRepository;
import ssii.dao.PersonneRepository;
import ssii.dao.ProjetRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipationServiceTest {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private ProjetRepository projetRepository;

    private Personne personne;
    private Projet projet1;
    private Projet projet2;
    private Projet projet3;
    private Projet projet4;

    @BeforeEach
    void setUp() {
        participationRepository.deleteAll();
        projetRepository.deleteAll();
        personneRepository.deleteAll();

        // Création et sauvegarde d’une personne
        personne = new Personne("Dupont", "Jean", "Développeur");
        personne = personneRepository.save(personne);

        // Création et sauvegarde de projets
        projet1 = new Projet("Projet Alpha", LocalDate.now(), null);
        projet2 = new Projet("Projet Beta", LocalDate.now(), null);
        projet3 = new Projet("Projet Gamma", LocalDate.now(), null);
        projet4 = new Projet("Projet Delta", LocalDate.now(), null);

        projet1 = projetRepository.save(projet1);
        projet2 = projetRepository.save(projet2);
        projet3 = projetRepository.save(projet3);
        projet4 = projetRepository.save(projet4);
    }

    @Test
    void testEnregistrerParticipation_Succes() {
        Participation participation = participationService.enregistrerParticipation(
            personne.getMatricule(), projet1.getCode(), "Développeur", 50
        );

        assertNotNull(participation);
        assertEquals("Développeur", participation.getRole());
        assertEquals(50, participation.getPourcentage());

        List<Participation> participations = participationRepository.findByPersonne(personne);
        assertEquals(1, participations.size());
    }

    @Test
    void testEnregistrerParticipation_Echec_Doublon() {
        // Créer une personne valide
        Personne personne = new Personne("Dupont", "Jean", "Développeur");
        personneRepository.save(personne);  // Sauvegarde dans la BD

        Projet projet = new Projet("Projet Alpha");
        projetRepository.save(projet);

        // Ajouter la participation une première fois
        participationService.enregistrerParticipation(personne.getMatricule(), projet.getCode(), "Développeur", 50.0f);

        // Tenter d'ajouter la même participation (doit échouer)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            participationService.enregistrerParticipation(personne.getMatricule(), projet.getCode(), "Développeur", 50.0f);
        });

        assertEquals("La personne participe déjà à ce projet", exception.getMessage());
    }

    @Test
    void testEnregistrerParticipation_Echec_ChargeTropHaute() {
        participationService.enregistrerParticipation(personne.getMatricule(), projet1.getCode(), "Développeur", 80);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            participationService.enregistrerParticipation(personne.getMatricule(), projet2.getCode(), "Analyste", 30)
        );

        assertEquals("La personne ne peut pas être occupée à plus de 100%", exception.getMessage());
    }

    @Test
    void testEnregistrerParticipation_Echec_TropDeProjets() {
        participationService.enregistrerParticipation(personne.getMatricule(), projet1.getCode(), "Dev", 30);
        participationService.enregistrerParticipation(personne.getMatricule(), projet2.getCode(), "Analyste", 40);
        participationService.enregistrerParticipation(personne.getMatricule(), projet3.getCode(), "Chef", 20);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            participationService.enregistrerParticipation(personne.getMatricule(), projet4.getCode(), "Développeur", 10)
        );

        assertEquals("La personne ne peut pas participer à plus de trois projets en même temps", exception.getMessage());
    }
}
