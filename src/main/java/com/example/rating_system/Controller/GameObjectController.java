package com.example.rating_system.Controller;

import com.example.rating_system.DTO.GameObjectDto;
import com.example.rating_system.Model.GameObject;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.GameObjectRepository;
import com.example.rating_system.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/object")
public class GameObjectController {

    private final GameObjectRepository gameObjectRepository;
    private final UserRepository userRepository;

    public GameObjectController(GameObjectRepository gameObjectRepository, UserRepository userRepository)
    {
        this.gameObjectRepository = gameObjectRepository;
        this.userRepository = userRepository;
    }

    // add a new game object
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> addGameObject(@RequestBody GameObjectDto dto) {
        User seller = userRepository.findById(dto.getSellerId()).orElseThrow(() -> new RuntimeException("This seller doesn't exist"));

        GameObject gameObject = new GameObject();
        gameObject.setTitle(dto.getTitle());
        gameObject.setText(dto.getText());
        gameObject.setSeller(seller);

        gameObjectRepository.save(gameObject);
        return ResponseEntity.ok(dto.getTitle() + " item " + dto.getText() + " successfully added");
    }

    // retrieve game objects
    @GetMapping
    public List<GameObject> getGameObjects() {
        return gameObjectRepository.findAll();
    }

    // edit an object
    @PutMapping("/{objectId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> editGameObject(@PathVariable UUID objectId, @RequestBody GameObjectDto dto) {
        GameObject gameObject = gameObjectRepository.findById(objectId).orElseThrow(() -> new RuntimeException("This object doesn't exist"));

        // check ownership, only author can edit
        if(gameObject.getSeller() != null) {
            if (!gameObject.getSeller().getId().equals(dto.getSellerId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the author can update this game object");
            }
        }

        gameObject.setTitle(dto.getTitle());
        gameObject.setText(dto.getText());
        gameObject.setUpdatedAt(LocalDateTime.now());
        gameObjectRepository.save(gameObject);
        return ResponseEntity.ok(gameObject.getTitle() + " object " + dto.getText() + " successfully updated.");
    }

    @DeleteMapping("/{objectId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> deleteGameObject(@PathVariable UUID objectId, @RequestParam UUID sellerId) {
        GameObject gameObject = gameObjectRepository.findById(objectId).orElseThrow(() -> new RuntimeException("This object doesn't exist"));

        // check if the seller is deleting
        if(gameObject.getSeller() != null) {
            if (!gameObject.getSeller().getId().equals(sellerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the author can delete this game object");
            }
        }

        gameObjectRepository.delete(gameObject);
        return ResponseEntity.ok("Object successfully deleted");
    }

}
