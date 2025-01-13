package com.tourweb.TouristWeb.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourweb.TouristWeb.Model.AllInclude;
import com.tourweb.TouristWeb.Model.Iternary;
import com.tourweb.TouristWeb.Model.Location;
import com.tourweb.TouristWeb.Model.PackageDetails;
import com.tourweb.TouristWeb.Repository.PackageDetailsRepo;
import com.tourweb.TouristWeb.Service.Interface.PackageDetailsServiceInterface;

@Service
public class PackageDetailsService  implements PackageDetailsServiceInterface{
	
	
	@Autowired
	private PackageDetailsRepo packageDetailsRepo;

	// Get all packages
   
	@Override
	 public List<PackageDetails> getAllPackages() {
        return packageDetailsRepo.findAll();
    }

	//get Package Details By packageId
	 @Override
	    public Optional<PackageDetails> findById(Long id) {
	        return packageDetailsRepo.findById(id);
	    }

	 @Override
	 public boolean deletePackageDetails(Long id) {
	     if(packageDetailsRepo.existsById(id)) {
	         packageDetailsRepo.deleteById(id);
	         return true;  // Return true if deletion is successful
	     }
	     return false;  // Return false if the package doesn't exist
	 }


	@Override
	public boolean editPackageDetails(Long id, PackageDetails packageDetails) {
		if(packageDetailsRepo.existsById(id)) {
			packageDetails.setId(id);
			packageDetailsRepo.save(packageDetails);
			return true;
		}
		return false;
	}
	
	@Override
	public List<PackageDetails> findAll() {
	    return packageDetailsRepo.findAll();
	}
	
	@Override
	public String UpdatePackage(PackageDetails packageDetails, AllInclude allInclude, List<Location> locations, List<Iternary> iternaries) {
	    packageDetails.setAllInclude(allInclude);
	    packageDetails.setLocations(locations);
	    packageDetails.setIternary(iternaries);

	    packageDetailsRepo.save(packageDetails);
	    
	    if (packageDetails.getId() != null) {
	        return "Package details updated successfully!";
	    } else {
	        return "Package details saved successfully!";
	    }
	}

	public PackageDetails saveThePackageDetails(PackageDetails packageDetails) {
		// Set packageDetails reference in Locations
		if (packageDetails.getLocations() != null) {
			packageDetails.getLocations().forEach(location -> location.setPackageDetails(packageDetails));
		}

		// Set packageDetails reference in AllInclude
		if (packageDetails.getAllInclude() != null) {
			packageDetails.getAllInclude().setPackageDetails(packageDetails);
		}

		// Set packageDetails reference in Iternary
		if (packageDetails.getIternary() != null) {
			packageDetails.getIternary().forEach(iternary -> {
				iternary.setPackageDetails(packageDetails);

				// Set Iternary reference in SightseeingEntrie
				if (iternary.getSightseeingEntrie() != null) {
					iternary.getSightseeingEntrie().forEach(sightseeingEntry -> sightseeingEntry.setIternary(iternary));
				}
			});
		}

		return packageDetailsRepo.save(packageDetails);
	}

	
	
	@Override
    @Transactional
    public PackageDetails updatePackageDetails(Long id, PackageDetails packageDetails) {
        System.out.println("Fetching package details with ID: " + id);
        Optional<PackageDetails> optionalDetails = packageDetailsRepo.findById(id);

        if (optionalDetails.isEmpty()) {
            System.out.println("Package not found with ID: " + id);
            return null;
        }

        PackageDetails existingPackageDetails = optionalDetails.get();
        System.out.println("Existing Package Details: " + existingPackageDetails);

        // Update fields
        existingPackageDetails.setPackageName(packageDetails.getPackageName());
        existingPackageDetails.setDuration(packageDetails.getDuration());
        existingPackageDetails.setPrice(packageDetails.getPrice());
        existingPackageDetails.setPackageType(packageDetails.getPackageType());
        existingPackageDetails.setPackageImage(packageDetails.getPackageImage());
        existingPackageDetails.setLocations(packageDetails.getLocations());
        existingPackageDetails.setAllInclude(packageDetails.getAllInclude());
        existingPackageDetails.setIternary(packageDetails.getIternary());

        // Set relationships
        if (existingPackageDetails.getLocations() != null) {
            existingPackageDetails.getLocations().forEach(location -> location.setPackageDetails(existingPackageDetails));
        }

        if (existingPackageDetails.getAllInclude() != null) {
            existingPackageDetails.getAllInclude().setPackageDetails(existingPackageDetails);
        }

        if (existingPackageDetails.getIternary() != null) {
            existingPackageDetails.getIternary().forEach(iternary -> {
                iternary.setPackageDetails(existingPackageDetails);
                if (iternary.getSightseeingEntrie() != null) {
                    iternary.getSightseeingEntrie().forEach(sightseeingEntry -> sightseeingEntry.setIternary(iternary));
                }
            });
        }

        // Save updated details
        PackageDetails updatedPackage = packageDetailsRepo.save(existingPackageDetails);
        System.out.println("Updated Package Details Saved: " + updatedPackage);

        return updatedPackage;
    }

	
	public List<PackageDetails> findbypackageType(String packageType) {
    return packageDetailsRepo.findByPackageType(packageType);
}

}
