package club.management.club.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Evenement {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String nom;
	    private String description;
	    private Date date;
	    private String location;
	    private Double budget;
	    
	    @OneToOne(mappedBy = "evenement") 
	    private Demande demande;
	    
	    public Evenement(){
	    	
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
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

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public Double getBudget() {
			return budget;
		}

		public void setBudget(Double budget) {
			this.budget = budget;
		}

		public Demande getDemande() {
			return demande;
		}

		public void setDemande(Demande demande) {
			this.demande = demande;
		}

	    
	    
	    

}
