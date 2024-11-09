package club.management.club.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private Date date;
    
    @ManyToOne
    @JoinColumn(name = "demande_id") 
    private Demande demande;

    
    public Historique() {
    	
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitre() {
		return titre;
	}


	public void setTitre(String titre) {
		this.titre = titre;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Demande getDemande() {
		return demande;
	}


	public void setDemande(Demande demande) {
		this.demande = demande;
	}
    
    
    
    
    
}