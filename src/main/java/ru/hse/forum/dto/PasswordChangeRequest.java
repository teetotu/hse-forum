package ru.hse.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
