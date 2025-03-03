package com.tourweb.TouristWeb.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class AllInclude {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String includes;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @JsonIgnore
    private PackageDetails packageDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getIncludes() {
		return includes;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}

	public PackageDetails getPackageDetails() {
		return packageDetails;
	}

	public void setPackageDetails(PackageDetails packageDetails) {
		this.packageDetails = packageDetails;
	}

}
