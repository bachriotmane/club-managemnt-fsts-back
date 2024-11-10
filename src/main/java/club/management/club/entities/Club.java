package club.management.club.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private Date createdAt;
    private String logo; 
    private String instagramme;

    @ManyToMany
    @JoinTable(
        name = "club_etudiant",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    private List<Etudiant> members;

    @OneToMany(mappedBy = "club")
    private List<Publication> publications;

    @OneToMany(mappedBy = "club")
    private List<Demande> demandes;

    
    @ManyToMany
    @JoinTable(
        name = "club_admins",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    private List<Etudiant> admins;
    
    //vide
    public Club() {
    	
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getInstagramme() {
		return instagramme;
	}

	public void setInstagramme(String instagramme) {
		this.instagramme = instagramme;
	}

	public List<Etudiant> getMembers() {
		return members;
	}

	public void setMembers(List<Etudiant> members) {
		this.members = members;
	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}

	public List<Etudiant> getAdmins() {
		return admins;
	}

	public void setAdmins(List<Etudiant> admins) {
		this.admins = admins;
	}


	
    
    
  
}