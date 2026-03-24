package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.facade.GymFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "Endpoints for managing Gym Users' password and active/deactive status")
public class UserController {

    private GymFacade gymFacade;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PutMapping("/{username}/password")
    @Operation(summary = "Change User Password", description = "Updates password if old credentials are valid")
    public ResponseEntity<Void> changePassword(
            @PathVariable String username,
            @Valid @RequestBody PasswordChangeRequest request) {
        log.info("REST: Password change attempt for user: {}", username);

        gymFacade.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Activate or Deactivate User", description = "Toggles the active status of a user profile")
    public ResponseEntity<Void> toggleUserStatus(
            @PathVariable String username,
            @RequestParam boolean isActive) {

        log.info("REST: Toggling status for user {} to {}", username, isActive);

        if (isActive) {
            gymFacade.activateUser(username);
        } else {
            gymFacade.deactivateUser(username);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
