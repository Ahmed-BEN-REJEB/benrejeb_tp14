package ssii.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import ssii.entity.Participation;
import ssii.entity.Personne;
import ssii.entity.Projet;


//This will be AUTO IMPLEMENTED by Spring
public interface ParticipationRepository extends JpaRepository<Participation, Integer>  {
    
    
    boolean existsByPersonneAndProjet(Personne personne, Projet projet);

    List<Participation> findByPersonne(Personne personne);

}
