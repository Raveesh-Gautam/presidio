package com.presidio.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.presidio.model.Property;
import com.presidio.model.User;
import com.presidio.service.PropertyService;
import com.presidio.service.UserService;

@RestController
@RequestMapping("/properties")
@CrossOrigin("http://localhost:3000")

public class PropertyController {
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserService userService;

    @PostMapping("/post")
    public ResponseEntity<Property> postProperty(@RequestBody Property property) {
        // Get the authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the username (email) of the authenticated user
            String username = authentication.getName();
            
            // Assuming userService is used to retrieve the user details from the database
            User seller = userService.getUserByEmail(username);
            
            // Check if the user exists and is authorized to post the property (e.g., seller role)
            if (seller != null && seller.getRole().equals("seller")) {
                // Assign the authenticated user (seller) to the property
                property.setSellerId(seller.getId());
                
                // Save the property
                Property newProperty = propertyService.postProperty(property);
                
                // Return the newly created property with HTTP status 201 Created
                return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
            } else {
                // User is not authorized to post a property
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            // User is not authenticated
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List> getPropertiesBySeller(@PathVariable Long sellerId) {
        List properties = propertyService.getPropertiesBySeller(sellerId);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
        @RequestParam String place,
        @RequestParam Double minArea,
        @RequestParam Double maxArea,
        @RequestParam int numberOfBedrooms,
        @RequestParam int numberOfBathrooms) {
        List<Property> properties = propertyService.searchProperties(place, minArea, maxArea, numberOfBedrooms, numberOfBathrooms);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @PostMapping("/interested/{id}")
    public ResponseEntity<User> interestedInProperty(@PathVariable Long id) {
        Optional<Property> propertyOpt = propertyService.getPropertyById(id);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            Long userId = property.getId(); // Assuming userId refers to the foreign key of User
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent()) {
                User seller = userOpt.get();
                return new ResponseEntity<>(seller, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Property not found
        }
    }


}
