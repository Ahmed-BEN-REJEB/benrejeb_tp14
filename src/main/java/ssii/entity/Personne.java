package ssii.entity;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @NonNull
    @Basic(optional = false)
    @Setter(AccessLevel.NONE) //la clé est auto-générée par la bd, on ne veut pas de Setter
    private Integer matricule;

    @NonNull
    @Basic(optional=false)
    private String nom;

    @NonNull
    @Basic(optional=false)
    private String prenom;

    @NonNull
    @Basic(optional = false)
    private String poste;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "personne")
    @ToString.Exclude
    //@NonNull : cette annotation est inutile parce qu'une personne peut ne pas avoir de participations
    private List<Participation> participations = new ArrayList<>();    

    @ManyToOne
    @JoinColumn(name = "superieur_id")
    //@NonNull : Cette annotation n'a pas été utilisée parce qu'une personne peut ne pas avoir de supérieur  
    @ToString.Exclude
    private Personne superieur;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "superieur")
    @ToString.Exclude
    @NonNull
    private List<Personne> subordonnes= new ArrayList<>();

    
    public Personne(String nom, String prenom, String poste) {
        this.nom= nom;
        this.prenom= prenom;
        this.poste= poste;
    }
}
