package pe.edu.cibertec.ProyectoDAWI.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usando BCrypt para el cifrado de contraseñas
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF si es necesario (es común en aplicaciones con APIs)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()  // Rutas abiertas
                        .requestMatchers("/admin/**").hasRole("administrador")  // Solo administradores pueden acceder
                        .requestMatchers("/cliente/**").hasAnyRole("administrador", "cliente")  // Administradores y clientes pueden acceder
                        .requestMatchers("/login", "/registro", "/css/**", "/js/**").permitAll()  // Rutas abiertas para login y recursos estáticos
                        .anyRequest().authenticated()  // El resto de rutas requieren autenticación
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Página personalizada para login
                        .permitAll()  // Permitir acceso sin autenticación
                        .successHandler(customAuthenticationSuccessHandler())  // Usar handler personalizado
                        .failureUrl("/login?error=true")  // Redirigir al login con error si la autenticación falla
                )
                .logout(logout -> logout.permitAll());  // Permitir logout sin autenticación

        return http.build();
    }

    // Implementamos directamente el UserDetailsService en el bean
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Cargar el usuario de la base de datos usando el repositorio
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // Obtener los roles del usuario desde la relación many-to-many
            List<String> roles = usuario.getRoles().stream()
                    .map(rol -> rol.getNombre())  // Suponiendo que 'Rol' tiene un método 'getAuthority()' que devuelve el nombre del rol
                    .collect(Collectors.toList());

            // Crear y devolver un objeto User con el nombre de usuario, la contraseña y los roles
            return User.builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword())
                    .roles(roles.toArray(new String[0]))  // Los roles convertidos a un arreglo de String
                    .build();
        };
    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                org.springframework.security.core.Authentication authentication)
                    throws IOException, ServletException {
                String redirectUrl = "/default";
                var authorities = authentication.getAuthorities();

                if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_administrador"))) {
                    redirectUrl = "/admin/start";
                } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_cliente"))) {
                    redirectUrl = "/cliente/tienda";
                }

                response.sendRedirect(redirectUrl);
            }
        };
    }

}
