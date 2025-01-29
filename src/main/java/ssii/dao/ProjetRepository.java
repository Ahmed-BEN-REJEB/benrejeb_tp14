package ssii.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ssii.entity.Projet;

//This will be AUTO IMPLEMENTED by Spring
public interface ProjetRepository extends JpaRepository<Projet, Integer> {


}
