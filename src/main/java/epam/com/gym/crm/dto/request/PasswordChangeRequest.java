package epam.com.gym.crm.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank(message = "Username is mandatory")
        String username,
        @NotBlank(message = "Old password is mandatory")
        String oldPassword,
        @NotBlank(message = "New password is mandatory")
        String newPassword
) {}
