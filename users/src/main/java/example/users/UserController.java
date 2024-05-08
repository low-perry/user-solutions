package example.users;

import java.net.URI;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import example.exceptions.WrongDateParametersException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.validation.Valid;

/**
 * The controller class that handles HTTP requests related to users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

/**
 * Retrieves a user by their ID.
 *
 * @param requestedId the ID of the user to retrieve
 * @param principal the principal object representing the currently authenticated user
 * @return a ResponseEntity containing the user if found, or a not found response if not found
 */
   @GetMapping("/{requestedId}")
   private ResponseEntity<User> findById(@PathVariable Long requestedId, Principal principal){
    
        User userOptional = findUser(requestedId, principal);

        if(userOptional != null) {
            return ResponseEntity.ok(userOptional);
        } else {
            return ResponseEntity.notFound().build();
        }
        
   }


    /**
     * Retrieves a list of users based on the provided pagination parameters and the authenticated principal.
     *
     * @param pageable   the pagination parameters for retrieving the users
     * @param principal  the authenticated principal representing the user
     * @return           a ResponseEntity containing the list of users
     */
    @GetMapping
    private ResponseEntity<List<User>> findAll(Pageable pageable, Principal principal) {
    
        Page<User> page = userRepository.findByOwner(
            principal.getName(),
            PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize(), 
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
            )
        );

        return ResponseEntity.ok(page.getContent());
          
    }

    /**
     * Retrieves a list of users owned by the authenticated principal between the specified start and end dates.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param principal the authenticated principal
     * @return a ResponseEntity containing the list of users if found, or a not found response if the list is empty
     * @throws IllegalArgumentException if the date format is invalid
     * @throws WrongDateParametersException if the end date is before the start date
     */
    @GetMapping("/{startDate}/{endDate}")
    private ResponseEntity<List<User>> findByOwnerAndBetweenDates(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate, Principal principal) {
    
        LocalDate start;
        LocalDate end;
    
        try {
            start = LocalDate.parse(startDate.toString());
            end = LocalDate.parse(endDate.toString());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format: yyyy-MM-dd", e);
        }

        if (endDate.isBefore(startDate)) {
            throw new WrongDateParametersException("endDate must be after startDate");
        }
        List<User> list = userRepository.findByOwnerAndBetweenDates(
            principal.getName(),
            startDate,
            endDate
        );

        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(list);
          
    }

/**
 * Creates a new user.
 *
 * @param newUser The user object containing the details of the new user.
 * @param ucb The UriComponentsBuilder used to build the URI for the new user.
 * @param principal The Principal object representing the currently authenticated user.
 * @return A ResponseEntity with a status code of 201 (Created) and the URI of the new user in the Location header.
 */
   @PostMapping
   private ResponseEntity<Void> createUser(@Valid @RequestBody User newUser, UriComponentsBuilder ucb, Principal principal){

        User userWithOwner = new User(null, newUser.getName(), newUser.getLastName(), newUser.getEmail(), newUser.getBirthday(), newUser.getPhoneNumber(), newUser.getAddress(), principal.getName());

        User savedUser = userRepository.save(userWithOwner);

        URI locationOfNewUser = ucb
            .path("/users/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();

        return ResponseEntity.created(locationOfNewUser).build();
   }

/**
 * Updates a user with the provided information.
 *
 * @param requestedId   The ID of the user to be updated.
 * @param updatedUser   The updated user object containing the new information.
 * @param principal     The principal object representing the currently authenticated user.
 * @return              A ResponseEntity with no content if the user was successfully updated,
 *                      or a ResponseEntity with a not found status if the user was not found.
 */
   @PutMapping("/{requestedId}")
   private ResponseEntity<Void> updateUser(@PathVariable Long requestedId, @RequestBody User updatedUser, Principal principal){
    
        User userOptional = findUser(requestedId, principal);

        if(userOptional != null) {
            User user = userOptional;
            user.setName(updatedUser.getName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setBirthday(updatedUser.getBirthday());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setAddress(updatedUser.getAddress());

            userRepository.save(user);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
        
   }

/**
 * Deletes a user with the specified ID.
 *
 * @param requestedId the ID of the user to delete
 * @param principal the principal object representing the currently authenticated user
 * @return a ResponseEntity with no content if the user was successfully deleted, or a ResponseEntity with not found status if the user does not exist or the authenticated user is not the owner
 */
   @DeleteMapping("/{requestedId}")
    private ResponseEntity<Void> deleteUser(@PathVariable Long requestedId, Principal principal){
        if(userRepository.existsByIdAndOwner(requestedId, principal.getName())) {
            userRepository.deleteById(requestedId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

        

          
    }

/**
 * Finds a user with the specified ID and owner.
 *
 * @param requestedId the ID of the user to find
 * @param principal the principal object representing the owner of the user
 * @return the user with the specified ID and owner, or null if not found
 */
   private User findUser(Long requestedId, Principal principal) {
        return userRepository.findByIdAndOwner(requestedId, principal.getName());
   }
}
