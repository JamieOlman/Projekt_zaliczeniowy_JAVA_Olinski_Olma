package JAVA_Olinski_Olma_zaliczenie.controller;

import JAVA_Olinski_Olma_zaliczenie.model.User;
import JAVA_Olinski_Olma_zaliczenie.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Operations on users")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all users from the database")
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing user", description = "Updates the user with the specified ID")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(name = "id", description = "The unique identifier of the user to update", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody User userDetails) {
        var updatedUser = userService.updateUser(id, userDetails);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a user by ID", description = "Deletes the user with the specified ID from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(name = "id", description = "The unique identifier of the user to delete", required = true, example = "1")
            @PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieve a user by ID", description = "Fetches a single user by their unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(name = "id", description = "The unique identifier of the user to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        var user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
