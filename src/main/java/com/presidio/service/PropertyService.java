package com.presidio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.presidio.model.Property;
import com.presidio.repository.PropertyRepository;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    public Property postProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getPropertiesBySeller(Long sellerId) {
        return propertyRepository.findBySellerId(sellerId);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
    public Optional<Property> getPropertyById(Long id) {
    	 return propertyRepository.findById(id);
    }

    public List<Property> searchProperties(String place, Double minArea, Double maxArea, int numberOfBedrooms, int numberOfBathrooms) {
        return propertyRepository.findByPlaceContainingAndAreaBetweenAndNumberOfBedroomsAndNumberOfBathrooms(place, minArea, maxArea, numberOfBedrooms, numberOfBathrooms);
    }
}
