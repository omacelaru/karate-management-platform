package ro.unibuc.fmi.karate_auth_service.dtos.user;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private List<String> roles;
}
