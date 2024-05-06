package example.users;

import java.net.URI;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;
import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @GetMapping("/{requestedId}")
   private ResponseEntity<User> findById(@PathVariable Long requestedId, Principal principal){
    
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByIdAndOwner(requestedId, principal.getName()));

        if(userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
        
   }


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

   @PostMapping
   private ResponseEntity<Void> createUser(@RequestBody User newUser, UriComponentsBuilder ucb, Principal principal){

        User userWithOwner = new User(null, newUser.getName(), newUser.getLastName(), newUser.getEmail(), newUser.getBirthday(), newUser.getPhoneNumber(), newUser.getAddress(), principal.getName());

        User savedUser = userRepository.save(userWithOwner);

        URI locationOfNewUser = ucb
            .path("/users/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();

        return ResponseEntity.created(locationOfNewUser).build();
   }
}
