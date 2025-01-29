package ssii.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ssii.entity.Participation;
import ssii.entity.Personne;
import ssii.entity.Projet;
import ssii.dao.ParticipationRepository;
import ssii.dao.PersonneRepository;
import ssii.dao.ProjetRepository;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PersonneRepository personneRepository;
    private final ProjetRepository projetRepository;

    @Transactional
    public Participation enregistrerParticipation(Integer matricule, Integer codeProjet, String role, float pourcentage) {
        Personne personne = personneRepository.findById(matricule)
                .orElseThrow(() -> new IllegalArgumentException("Personne non trouvée"));
        Projet projet = projetRepository.findById(codeProjet)
                .orElseThrow(() -> new IllegalArgumentException("Projet non trouvé"));

        // Règle 1 : Vérifier que la personne ne participe pas déjà au projet
        if (participationRepository.existsByPersonneAndProjet(personne, projet)) {
            throw new IllegalArgumentException("La personne participe déjà à ce projet");
        }

        // Règle 2 : Vérifier que la personne ne dépasse pas 100% de charge sur les projets en cours
        List<Participation> participationsActuelles = participationRepository.findByPersonne(personne);
        double chargeTotale = participationsActuelles.stream()
                .mapToDouble(Participation::getPourcentage)
                .sum();
        
        if (chargeTotale + pourcentage > 100) {
            throw new IllegalArgumentException("La personne ne peut pas être occupée à plus de 100%");
        }

        // Règle 3 : Vérifier que la personne ne participe pas à plus de 3 projets actifs en même temps
        long projetsEnCours = participationsActuelles.stream()
                .filter(p -> p.getProjet().getFin() == null) // Projets non terminés
                .count();
        
        if (projetsEnCours >= 3) {
            throw new IllegalArgumentException("La personne ne peut pas participer à plus de trois projets en même temps");
        }

        // Enregistrer la participation
        Participation participation = new Participation(role, pourcentage, projet, personne);
        participationRepository.save(participation);
        
        return participation;
    }
}
