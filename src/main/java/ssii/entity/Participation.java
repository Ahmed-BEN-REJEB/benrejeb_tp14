package ssii.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

//Ajout des annotations nécessaires
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Participation {
    
    public Participation(String role, float pourcentage, Projet projet, Personne personne) {
        this.role= role;
        this.pourcentage= pourcentage;
        this.projet= projet;
        this.personne= personne;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @NonNull
    @Basic(optional = false)
    @Setter(AccessLevel.NONE) //la clé est auto-générée par la bd, on ne veut pas de Setter
    private Integer id;

    @NonNull
    @Basic(optional=false)
    private String role;

    //@NonNull : cette annotation est inutile pour cette colonne parce qu'elle est de type primitive
    @Min(value = 0, message= "La valeur du pourcentage ne peut pas être négative")
    @Basic(optional=false)
    private float pourcentage;

    @JoinColumn(nullable = false, name= "projet_id")
    @NonNull
	@ManyToOne(optional = false)
	private Projet projet;

	@JoinColumn(nullable = false)
    @NonNull
	@ManyToOne(optional = false)
	private Personne personne;
}
