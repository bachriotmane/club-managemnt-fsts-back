package club.management.club.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Etudiant extends User {
    private String cne;
    private String filiere;

    @ManyToMany(mappedBy = "members")
    private List<Club> clubs;

    @ManyToMany(mappedBy = "admins")
    private List<Club> clubsAdmin; 
    
    @OneToMany(mappedBy = "etudiant") 
    private List<Demande> demandes;
    
    public Etudiant() {
    	super();
    	
    }

	public String getCne() {
		return cne;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	public String getFiliere() {
		return filiere;
	}

	public void setFiliere(String filiere) {
		this.filiere = filiere;
	}

	public List<Club> getClubs() {
		return clubs;
	}

	public void setClubs(List<Club> clubs) {
		this.clubs = clubs;
	}

	public List<Club> getClubsAdmin() {
		return clubsAdmin;
	}

	public void setClubsAdmin(List<Club> clubsAdmin) {
		this.clubsAdmin = clubsAdmin;
	}

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}
    
    
    
    
    
}
