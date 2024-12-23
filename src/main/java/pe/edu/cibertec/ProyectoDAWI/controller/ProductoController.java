package pe.edu.cibertec.ProyectoDAWI.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ProyectoDAWI.dto.*;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoCreateDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;
import pe.edu.cibertec.ProyectoDAWI.service.ManageProductoService;


import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductoController {

    @Autowired
    ManageProductoService productoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/start")
    public String start(Model model) {

        try {
            List<ProductoDto> productos = productoService.getAllProductos();
            model.addAttribute("productos", productos);
            model.addAttribute("error", null);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al obtener los productos.");
        }

        return "admin/Home";  // Sin "productos/"
    }
    // Mostrar el formulario de agregar producto
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        try {
            ProductoCreateDto productoCreateDto = new ProductoCreateDto();
            model.addAttribute("producto", productoCreateDto);  // Esto agrega el objeto al modelo correctamente

            // Obtener las categorías desde el servicio o repositorio
            List<CategoriaDto> categorias = productoService.getAllCategorias(); // Método que debe devolver las categorías
            model.addAttribute("categorias", categorias);

            return "admin/Agregar";  // Vista para crear producto
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al cargar las categorías.");
            return "admin/Home";  // Sin "productos/"
        }
    }

    @PostMapping("/crear")
    public String crearProducto(ProductoCreateDto productoCreateDto, Model model) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();

                // Buscar el usuario en la base de datos por su nombre de usuario
                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                // Asignar el ID del usuario autenticado al DTO
                productoCreateDto.setId_usuario_registro(usuario.getId_usuario());
            }

            // Imprimir valores para verificar
            System.out.println("ID Categoría: " + productoCreateDto.getId_categoria());
            System.out.println("ID Usuario Registro: " + productoCreateDto.getId_usuario_registro());

            // Guardar el producto a través del servicio
            productoService.createProducto(productoCreateDto);

            // Agregar mensaje de éxito
            model.addAttribute("success", "Producto registrado exitosamente.");

            // Redirigir a la página de inicio
            return "redirect:/admin/start";
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, mostrar mensaje de error
            model.addAttribute("error", "Ocurrió un error al crear el producto.");
            return "admin/Agregar";
        }
    }
    // Eliminar Producto
    @GetMapping("/eliminar")
    public String eliminarProducto(@RequestParam("id") Integer id_producto, Model model) {
        // Lógica para eliminar el producto
        boolean eliminado = productoService.deleteProducto(id_producto);

        if (eliminado) {
            model.addAttribute("success", "Producto eliminado con éxito");
        } else {
            model.addAttribute("error", "Hubo un problema al eliminar el producto");
        }

        return "redirect:/admin/start";  // Redirige a la lista de productos
    }


    // Mostrar formulario de edición
    @GetMapping("/editar")
    public String mostrarFormularioEdicion(@RequestParam("id") Integer id_producto, Model model) {
        try {
            ProductoDetailDto productoDetailDto = productoService.getDetailProducto(id_producto)
                    .orElseThrow(() -> new Exception("Producto no encontrado"));

            model.addAttribute("producto", productoDetailDto);

            // Obtener las categorías desde el servicio
            List<CategoriaDto> categorias = productoService.getAllCategorias();
            model.addAttribute("categorias", categorias);

            return "admin/Editar";  // Vista para editar producto
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al cargar el producto.");
            return "admin/Home";  // Redirigir a la página principal en caso de error
        }
    }

    // Actualizar Producto
    @PostMapping("/editar")
    public String actualizarProducto(ProductoDto productoDto, Model model) {
        try {
            boolean success = productoService.updateProducto(productoDto);

            if (success) {
                model.addAttribute("success", "Producto actualizado exitosamente.");
                return "redirect:/admin/start";  // Redirigir a la página principal
            } else {
                model.addAttribute("error", "No se pudo actualizar el producto.");
                return "admin/Editar";  // Volver al formulario de edición
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al actualizar el producto.");
            return "admin/Editar";  // Volver al formulario de edición
        }
    }



}
