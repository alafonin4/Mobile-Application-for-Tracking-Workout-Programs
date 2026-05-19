package ru.alafonin4.authserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.alafonin4.authserver.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    UserRole role = UserRole.CUSTOMER;

    /**
     * LocalDateTime.now.
     * @return result of the operation
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Returns the authorities.
     * @return result of the operation
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the username.
     * @return resulting text value
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns the password.
     * @return resulting text value
     */
    @Override
    public String getPassword(){
        return passwordHash;
    }

    /**
     * Indicates whether account non expired.
     * @return true when the condition is satisfied; otherwise false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether account non locked.
     * @return true when the condition is satisfied; otherwise false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether credentials non expired.
     * @return true when the condition is satisfied; otherwise false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether enabled.
     * @return true when the condition is satisfied; otherwise false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the first name.
     * @return resulting text value
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Updates the first name.
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     * @return resulting text value
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Updates the last name.
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the email.
     * @return resulting text value
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email.
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password hash.
     * @return resulting text value
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Updates the password hash.
     * @param passwordHash new password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the role.
     * @return result of the operation
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Updates the role.
     * @param role new role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Returns the created at.
     * @return result of the operation
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Updates the created at.
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
