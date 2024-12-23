package pe.edu.cibertec.ProyectoDAWI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.cibertec.ProyectoDAWI.entity.Venta;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.VentaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/listar")
    public String listarVentas(Model model) {
        Iterable<Venta> ventasIterable = ventaRepository.findAll();
        List<Venta> ventas = StreamSupport.stream(ventasIterable.spliterator(), false)
                .collect(Collectors.toList()); // O ajusta según tu necesidad
        model.addAttribute("ventas", ventas);
        return "admin/listarVenta";
    }

    @GetMapping("/detalle/{id}")
    public String verDetallesVenta(@PathVariable("id") Integer id_venta, Model model) {
        Optional<Venta> venta = ventaRepository.findVentaWithProductos(id_venta);
        if (venta.isPresent()) {
            model.addAttribute("venta", venta.get());
            return "admin/detalleVenta";
        } else {
            model.addAttribute("error", "Venta no encontrada.");
            return "admin/listarVenta";
        }
    }

}
