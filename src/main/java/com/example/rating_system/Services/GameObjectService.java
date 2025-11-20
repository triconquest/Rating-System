package com.example.rating_system.Services;

import com.example.rating_system.DTO.GameObjectDto;
import com.example.rating_system.Model.GameObject;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.GameObjectRepository;
import com.example.rating_system.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final UserRepository userRepository;

    public GameObjectService(GameObjectRepository gameObjectRepository, UserRepository userRepository)
    {
        this.gameObjectRepository = gameObjectRepository;
        this.userRepository = userRepository;
    }

    // add a new game object
    public ResponseEntity<String> addGameObject(GameObjectDto dto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        UUID userId = (UUID)session.getAttribute("sellerId");

        User seller = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("This seller doesn't exist"));

        if(seller.getRole() != Role.SELLER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only verified sellers can create game objects");

        GameObject gameObject = new GameObject();
        gameObject.setTitle(dto.getTitle());
        gameObject.setText(dto.getText());
        gameObject.setSeller(seller);

        gameObjectRepository.save(gameObject);
        return ResponseEntity.ok(dto.getTitle() + " item " + dto.getText() + " successfully added");
    }

    // retrieve game objects
    public List<GameObject> getGameObjects() {
        return gameObjectRepository.findAll();
    }

    // edit an object
    public ResponseEntity<String> editGameObject(UUID objectId, GameObjectDto dto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        UUID sellerId = (UUID) session.getAttribute("sellerId");

        GameObject gameObject = gameObjectRepository.findById(objectId).orElseThrow(() -> new RuntimeException("This object doesn't exist"));

        // check ownership, only author can edit
        if(gameObject.getSeller() != null) {
            if (!gameObject.getSeller().getId().equals(sellerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the author can update this game object");
            }
        }

        gameObject.setTitle(dto.getTitle());
        gameObject.setText(dto.getText());
        gameObject.setUpdatedAt(LocalDateTime.now());
        gameObjectRepository.save(gameObject);
        return ResponseEntity.ok(gameObject.getTitle() + " object " + dto.getText() + " successfully updated.");
    }

    public ResponseEntity<String> deleteGameObject(UUID objectId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        UUID sellerId = (UUID) session.getAttribute("sellerId");

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
