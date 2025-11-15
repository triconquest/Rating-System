package com.example.rating_system.Controller;

import com.example.rating_system.DTO.GameObjectDto;
import com.example.rating_system.Model.GameObject;
import com.example.rating_system.Services.GameObjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/object")
public class GameObjectController {

    private final GameObjectService gameObjectService;

    public GameObjectController(GameObjectService gameObjectService)
    {
        this.gameObjectService = gameObjectService;
    }

    // add a new game object
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> addGameObject(@Valid @RequestBody GameObjectDto dto) {
        return gameObjectService.addGameObject(dto);
    }

    // retrieve game objects
    @GetMapping
    public List<GameObject> getGameObjects() {
        return gameObjectService.getGameObjects();
    }

    // edit an object
    @PutMapping("/{objectId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> editGameObject(@PathVariable UUID objectId, @Valid @RequestBody GameObjectDto dto) {
        return gameObjectService.editGameObject(objectId, dto);
    }

    @DeleteMapping("/{objectId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> deleteGameObject(@PathVariable UUID objectId, @RequestParam UUID sellerId) {
        return gameObjectService.deleteGameObject(objectId, sellerId);
    }
}
