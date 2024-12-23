package pe.edu.cibertec.ProyectoDAWI.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.cibertec.ProyectoDAWI.entity.Rol;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;
import pe.edu.cibertec.ProyectoDAWI.repository.RolRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        Iterable<Rol> rolesIterable = rolRepository.findAll();
        List<Rol> roles = new ArrayList<>();
        rolesIterable.forEach(roles::add);  // Convertir Iterable a List
        model.addAttribute("roles", roles);  // Añadir los roles al modelo
        model.addAttribute("usuario", new Usuario());  // Crear un nuevo objeto Usuario
        return "/registro";  // El nombre de la vista
    }


    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario,
                                   @RequestParam("rol") Integer rolId,
                                   Model model) {
        System.out.println("Rol recibido: " + rolId);  // Para verificar el valor recibido

        // Obtener el rol del repositorio por id
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        // Encriptar la contraseña antes de guardarla
        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);  // Establecer la contraseña encriptada

        // Asignar el rol al usuario
        usuario.setRoles(Collections.singletonList(rol));

        // Guardar el usuario
        usuarioRepository.save(usuario);

        // Mensaje de éxito
        model.addAttribute("exito", "Usuario registrado exitosamente");
        return "/login";
    }



}
