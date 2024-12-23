package pe.edu.cibertec.ProyectoDAWI.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ProyectoDAWI.dto.CategoriaDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;
import pe.edu.cibertec.ProyectoDAWI.entity.Producto;
import pe.edu.cibertec.ProyectoDAWI.repository.ProductoRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;
import pe.edu.cibertec.ProyectoDAWI.service.ManageProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/cliente")
public class CarritoController {
    @Autowired
    private HttpSession session;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    ManageProductoService productoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/tienda")
    public String mostrarProductos(Model model) {
        try {
            // Obtener todos los productos
            List<ProductoDto> productos = productoService.getAllProductos();

            // Enviar todos los productos al modelo
            model.addAttribute("productos", productos);

            // Obtener las categorías para mostrar como filtros o en un menú (si es necesario)
            List<CategoriaDto> categorias = productoService.getAllCategorias();
            model.addAttribute("categorias", categorias);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al obtener los productos.");
        }

        // Retornar la vista con todos los productos
        return "cliente/VistaTienda"; // Asegúrate de que esta vista esté configurada correctamente
    }
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        // Obtener todos los productos desde la base de datos
        List<Producto> productos = StreamSupport.stream(productoRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());        model.addAttribute("productos", productos);
        return "productos/vistaTienda";  // Vista que muestra los productos
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Integer id_producto, @RequestParam int cantidad, HttpSession session) {
        System.out.println("Producto ID: " + id_producto);  // Verificar el valor del ID
        // Obtener el producto desde el repositorio por su ID
        Producto producto = productoRepository.findById(id_producto).orElse(null);

        if (producto != null) {
            // Obtener el carrito de la sesión
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }

            // Buscar si el producto ya está en el carrito
            Optional<Producto> productoExistente = carrito.stream()
                    .filter(p -> p.getId_producto().equals(id_producto))
                    .findFirst();

            if (productoExistente.isPresent()) {
                // Si el producto ya está en el carrito, actualizar la cantidad
                Producto productoEnCarrito = productoExistente.get();
                productoEnCarrito.setCantidad(productoEnCarrito.getCantidad() + cantidad);
            } else {
                // Si el producto no está en el carrito, agregarlo con la cantidad especificada
                producto.setCantidad(cantidad); // Establecer la cantidad inicial
                carrito.add(producto);
            }

            // Guardar el carrito actualizado en la sesión
            session.setAttribute("carrito", carrito);
        }

        return "redirect:/productos";  // Redirigir a la vista de productos
    }


    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam Integer id_producto, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null) {
            carrito.removeIf(producto -> producto.getId_producto().equals(id_producto));
            session.setAttribute("carrito", carrito);
        }

        return "redirect:/carrito";  // Redirigir a la vista del carrito
    }
    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {
        // Obtener el carrito de la sesión (la lista de productos)
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        // Si el carrito está vacío, inicializarlo
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        // Calcular el total
        double total = 0;
        for (Producto producto : carrito) {
            // Sumar el precio de cada producto multiplicado por su cantidad
            total += producto.getPrecio() * producto.getCantidad();  // Asegúrate de que 'cantidad' sea un atributo de Producto
        }

        // Agregar el carrito y el total al modelo
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);  // Pasar el total al frontend

        return "productos/carrito";  // Vista que muestra el carrito
    }


    @PostMapping("/carrito/actualizarCantidad")
    public String actualizarCantidad(@RequestParam Integer id_producto, @RequestParam int cantidad, HttpSession session) {
        // Recuperar el carrito de la sesión
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null) {
            // Buscar el producto en el carrito
            for (Producto producto : carrito) {
                if (producto.getId_producto().equals(id_producto)) {
                    // Actualizar la cantidad del producto
                    producto.setCantidad(cantidad);
                    System.out.println("Cantidad actualizada para producto " + producto.getDescripcion() + ": " + cantidad);

                    break;
                }
            }
        }

        // Guardar de nuevo el carrito en la sesión
        session.setAttribute("carrito", carrito);
        System.out.println("Carrito actualizado en la sesión");

        return "redirect:/carrito"; // Redirigir a la página del carrito
    }
    @GetMapping("/producto/{id}")
    public String mostrarProducto(@PathVariable("id_producto") Integer id_producto, Model model) {
        Producto producto = productoRepository.findById(id_producto).orElse(null);
        model.addAttribute("producto", producto);
        return "productoDetalle";  // La vista donde estás renderizando el formulario
    }


    @PostMapping("/pago/procesar")
    public String procesarPago(@RequestParam String nombre,
                               @RequestParam String numeroTarjeta,
                               @RequestParam String fechaExpiracion,
                               @RequestParam String cvv,
                               Model model) {
        // Aquí puedes procesar la información de pago.
        // En un escenario real, integras con un servicio de pago (como Stripe, PayPal, etc.).

        // Verifica si el pago es exitoso.
        boolean pagoExitoso = procesarPagoConPasarela(nombre, numeroTarjeta, fechaExpiracion, cvv);

        if (pagoExitoso) {
            // Realiza la acción después de un pago exitoso (por ejemplo, limpiar el carrito, registrar la compra, etc.).
            model.addAttribute("mensaje", "¡Pago exitoso! Gracias por tu compra.");
            return "productos/compraExitosa";  // Vista que muestra un mensaje de confirmación.
        } else {
            model.addAttribute("mensaje", "Hubo un error con el pago. Intenta de nuevo.");
            return "productos/pago";  // Volver a la vista de pago en caso de error.
        }
    }

    private boolean procesarPagoConPasarela(String nombre, String numeroTarjeta, String fechaExpiracion, String cvv) {
        // Simulación de procesamiento de pago.
        // En una aplicación real, integrarías una pasarela de pago como Stripe o PayPal.
        return true;  // Aquí devolveríamos true si el pago es exitoso.
    }
    @GetMapping("/pago/procesar")
    public String mostrarFormularioPago() {
        return "productos/pago"; // Show the payment form
    }

}
