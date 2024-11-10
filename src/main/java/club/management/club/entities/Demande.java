package club.management.club.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Club club;
    
    @OneToMany(mappedBy = "demande") 
    private List<Historique> historiques;
    
    @OneToOne
    @JoinColumn(name = "evenement_id") 
    private Evenement evenement;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;
    
    @Enumerated(EnumType.STRING)
    private TypeDemande type;
    
    public Demande(){
    	
    }
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public List<Historique> getHistoriques() {
		return historiques;
	}

	public void setHistoriques(List<Historique> historiques) {
		this.historiques = historiques;
	}

	public Evenement getEvenement() {
		return evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}

	public StatutDemande getStatut() {
		return statut;
	}

	public void setStatut(StatutDemande statut) {
		this.statut = statut;
	}

	public TypeDemande getType() {
		return type;
	}

	public void setType(TypeDemande type) {
		this.type = type;
	}
    
    

    
}

enum StatutDemande {
    EN_COURS, ACCEPTE, REFUSE
}
enum TypeDemande {
    CREATION_CLUB, INTEGRATION_CLUB, EVENEMENT
}
