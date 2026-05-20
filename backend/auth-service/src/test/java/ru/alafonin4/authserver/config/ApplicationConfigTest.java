package ru.alafonin4.authserver.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.alafonin4.authserver.entities.User;
import ru.alafonin4.authserver.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    void userDetailsServiceLoadsUserByEmail() {
        ApplicationConfig config = new ApplicationConfig();
        ReflectionTestUtils.setField(config, "userRepository", userRepository);

        User user = new User();
        user.setEmail("ivan@example.com");

        when(userRepository.findByEmail("ivan@example.com")).thenReturn(Optional.of(user));

        UserDetailsService service = config.userDetailsService();

        assertEquals("ivan@example.com", service.loadUserByUsername("ivan@example.com").getUsername());
    }

    @Test
    void userDetailsServiceThrowsForMissingUser() {
        ApplicationConfig config = new ApplicationConfig();
        ReflectionTestUtils.setField(config, "userRepository", userRepository);

        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> config.userDetailsService().loadUserByUsername("ghost@example.com"));
    }

    @Test
    void authenticationProviderUsesConfiguredServices() {
        ApplicationConfig config = new ApplicationConfig();
        ReflectionTestUtils.setField(config, "userRepository", userRepository);

        AuthenticationProvider provider = config.authenticationProvider();
        PasswordEncoder passwordEncoder = config.passwordEncoder();

        assertNotNull(provider);
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.matches("secret", passwordEncoder.encode("secret")));
    }

    @Test
    void authenticationManagerDelegatesToConfiguration() throws Exception {
        ApplicationConfig config = new ApplicationConfig();
        AuthenticationManager manager = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(manager);

        assertEquals(manager, config.authenticationManager(authenticationConfiguration));
    }

    private static void assertEquals(Object expected, Object actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }
}
