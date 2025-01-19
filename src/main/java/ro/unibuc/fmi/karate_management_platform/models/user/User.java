package ro.unibuc.fmi.karate_management_platform.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new LinkedHashSet<>();

    @Column(name = "last_name", length = 60)
    private String lastName;

    @Column(name = "first_name", length = 120)
    private String firstName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nationality", length = 100)
    @Size(min = 2, max = 100, message = "Nationality should be between 2 and 100 characters")
    private String nationality;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.copyOf(roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}

