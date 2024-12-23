package pe.edu.cibertec.ProyectoDAWI.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // Método para encriptar la contraseña
    public static String encode(String password) {
        System.out.println("Encriptando la contraseña: " + password); // Imprime la contraseña antes de encriptar
        String encodedPassword = encoder.encode(password);
        System.out.println("Contraseña encriptada: " + encodedPassword); // Imprime la contraseña encriptada
        return encodedPassword;
    }

    // Método para verificar la contraseña encriptada
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}