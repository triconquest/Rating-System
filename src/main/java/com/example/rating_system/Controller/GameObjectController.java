package com.example.rating_system.Controller;

import com.example.rating_system.DTO.GameObjectDto;
import com.example.rating_system.Model.GameObject;
import com.example.rating_system.Services.GameObjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addGameObject(@Valid @RequestBody GameObjectDto dto, HttpServletRequest request) {
        return gameObjectService.addGameObject(dto, request);
    }

    // retrieve game objects
    @GetMapping
    public List<GameObject> getGameObjects() {
        return gameObjectService.getGameObjects();
    }

    // edit an object
    @PutMapping("/{objectId}")
    public ResponseEntity<String> editGameObject(@PathVariable UUID objectId, @Valid @RequestBody GameObjectDto dto, HttpServletRequest request) {
        return gameObjectService.editGameObject(objectId, dto, request);
    }

    @DeleteMapping("/{objectId}")
    public ResponseEntity<String> deleteGameObject(@PathVariable UUID objectId, HttpServletRequest request) {
        return gameObjectService.deleteGameObject(objectId, request);
    }
}
