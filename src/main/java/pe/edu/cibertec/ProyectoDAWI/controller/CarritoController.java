package pe.edu.cibertec.ProyectoDAWI.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ProyectoDAWI.dto.CategoriaDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;
import pe.edu.cibertec.ProyectoDAWI.entity.Producto;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta;
import pe.edu.cibertec.ProyectoDAWI.repository.ProductoRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.VentaRepository;
import pe.edu.cibertec.ProyectoDAWI.service.ManageProductoService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta;

@Controller
@RequestMapping("/cliente")
public class CarritoController {

    @Autowired
    private HttpSession session;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ManageProductoService productoService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private VentaRepository ventaRepository;

    // Mostrar productos en la tienda
    @GetMapping("/tienda")
    public String mostrarProductos(Model model) {
        try {
            // Obtener todos los productos
            List<ProductoDto> productos = productoService.getAllProductos();
            model.addAttribute("productos", productos);

            // Obtener las categorías para mostrar como filtros o en un menú (si es necesario)
            List<CategoriaDto> categorias = productoService.getAllCategorias();
            model.addAttribute("categorias", categorias);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al obtener los productos.");
        }

        return "cliente/VistaTienda"; // Vista de tienda
    }

    // Listar productos
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        List<Producto> productos = StreamSupport.stream(productoRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "cliente/vistaTienda";  // Vista que muestra los productos
    }

    // Agregar producto al carrito
    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Integer id_producto, @RequestParam int cantidad, HttpSession session) {
        Producto producto = productoRepository.findById(id_producto).orElse(null);

        if (producto != null) {
            // Obtener el carrito de la sesión
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }

            // Verificar si el producto ya está en el carrito
            Optional<Producto> productoExistente = carrito.stream()
                    .filter(p -> p.getId_producto().equals(id_producto))
                    .findFirst();

            if (productoExistente.isPresent()) {
                Producto productoEnCarrito = productoExistente.get();
                productoEnCarrito.setCantidad(productoEnCarrito.getCantidad() + cantidad);
            } else {
                producto.setCantidad(cantidad); // Establecer la cantidad inicial
                carrito.add(producto);
            }

            session.setAttribute("carrito", carrito);
        }

        return "redirect:/cliente/productos";  // Si tienes una página de carrito para ver el contenido
    }

    // Eliminar producto del carrito
    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam Integer id_producto, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null) {
            carrito.removeIf(producto -> producto.getId_producto().equals(id_producto));
            session.setAttribute("carrito", carrito);
        }

        return "redirect:/cliente/carrito";  // Si tienes una página de carrito para ver el contenido
    }

    // Ver carrito
    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        // Calcular total
        double total = 0;
        for (Producto producto : carrito) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "cliente/carrito";  // Vista que muestra el carrito
    }

    // Actualizar cantidad del producto en el carrito
    @PostMapping("/carrito/actualizarCantidad")
    public String actualizarCantidad(@RequestParam Integer id_producto, @RequestParam int cantidad, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null) {
            for (Producto producto : carrito) {
                if (producto.getId_producto().equals(id_producto)) {
                    producto.setCantidad(cantidad);
                    break;
                }
            }
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/cliente/carrito";  // Si tienes una página de carrito para ver el contenido
    }

    // Mostrar detalles de un producto
    @GetMapping("/producto/{id}")
    public String mostrarProducto(@PathVariable("id") Integer id_producto, Model model) {
        Producto producto = productoRepository.findById(id_producto).orElse(null);
        model.addAttribute("producto", producto);
        return "productoDetalle";  // Vista que muestra detalles del producto
    }

    @PostMapping("/pago/procesar")
    public String procesarPago(@RequestParam String nombre,
                               @RequestParam String numeroTarjeta,
                               @RequestParam String fechaExpiracion,
                               @RequestParam String cvv,
                               HttpSession session,
                               Model model) {

        // Simulación del pago
        boolean pagoExitoso = procesarPagoConPasarela(nombre, numeroTarjeta, fechaExpiracion, cvv);

        if (pagoExitoso) {
            // Obtener el usuario autenticado a través de Spring Security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User usuarioAuth = (User) authentication.getPrincipal();
            String username = usuarioAuth.getUsername();

            // Buscar el usuario en la base de datos
            Usuario objUsuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Crear una nueva venta
            Venta venta = new Venta();
            venta.setUsuarioRegistro(objUsuario);
            venta.setFechaRegistro(new Date());
            venta.setTotal(calcularTotal(session));

            // Guardar la venta
            ventaRepository.save(venta);

            // Obtener los productos del carrito
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

            // Asociar los productos a la venta
            if (carrito != null) {
                for (Producto producto : carrito) {
                    producto.getVentas().add(venta);  // Asociar la venta al producto

                    // Verificar si el stock es suficiente para la compra
                    if (producto.getStock() >= producto.getCantidad()) {
                        producto.setStock(producto.getStock() - producto.getCantidad());  // Reducir el stock
                        productoRepository.save(producto);  // Guardar los cambios en el producto
                    } else {
                        model.addAttribute("mensaje", "No hay suficiente stock para el producto: " + producto.getDescripcion());
                        return "cliente/pago";  // Redirigir al formulario de pago si no hay suficiente stock
                    }
                }
            }

            // Limpiar el carrito después del pago
            session.setAttribute("carrito", new ArrayList<>());  // Vaciar el carrito

            model.addAttribute("mensaje", "¡Pago exitoso! Gracias por tu compra.");
            return "cliente/compraExitosa";  // Vista de pago exitoso
        } else {
            model.addAttribute("mensaje", "Hubo un error con el pago. Intenta de nuevo.");
            return "cliente/pago";  // Vista de pago en caso de error
        }
    }


    private double calcularTotal(HttpSession session) {
        double total = 0;
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null) {
            for (Producto producto : carrito) {
                total += producto.getPrecio() * producto.getCantidad();
            }
        }
        return total;
    }

    private boolean procesarPagoConPasarela(String nombre, String numeroTarjeta, String fechaExpiracion, String cvv) {
        return true;  // Simulación de pago exitoso
    }

    // Mostrar formulario de pago
    @GetMapping("/pago/procesar")
    public String mostrarFormularioPago() {
        return "cliente/pago";  // Vista de formulario de pago
    }
}
