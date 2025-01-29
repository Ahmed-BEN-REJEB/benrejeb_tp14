package ssii.entity;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

//Ajout des annotations nécessaires
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Projet {

    public Projet(String nom) {
        this.nom= nom;
    }

    public Projet(String nom, LocalDate debut, LocalDate fin) {
        this.nom= nom;
        this.debut= debut;
        this.fin= fin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @NonNull
    @Basic(optional = false)
    @Setter(AccessLevel.NONE) //la clé est auto-générée par la bd, on ne veut pas de Setter
    private Integer code;

    @NonNull
    @Basic(optional=false)
    private String nom;

    @NonNull
    @Basic(optional=false)
    private LocalDate debut= LocalDate.now();

    @Basic(optional = false)
    private LocalDate fin= null;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "projet")
    @ToString.Exclude
    //@NonNull : cette annotation est inutile parce qu'une personne peut ne pas avoir de participations
    private List<Participation> participations = new ArrayList<>();
}
