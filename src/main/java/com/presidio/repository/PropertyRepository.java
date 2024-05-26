package com.presidio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presidio.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findBySellerId(Long sellerId);
    List<Property> findByPlaceContainingAndAreaBetweenAndNumberOfBedroomsAndNumberOfBathrooms(String place, Double minArea, Double maxArea, int numberOfBedrooms, int numberOfBathrooms);
}
