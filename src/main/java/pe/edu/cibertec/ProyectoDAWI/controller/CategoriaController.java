package pe.edu.cibertec.ProyectoDAWI.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ProyectoDAWI.entity.Categoria;
import pe.edu.cibertec.ProyectoDAWI.repository.CategoriaRepository;

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Mostrar todas las categorías
    @GetMapping("/listar")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "admin/listarCategoria";
    }

    // Mostrar formulario para agregar nueva categoría
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/crearCategoria";
    }

    // Crear nueva categoría
    @PostMapping("/crear")
    public String crearCategoria(@ModelAttribute Categoria categoria, Model model) {
        try {
            categoriaRepository.save(categoria);
            model.addAttribute("success", "Categoría creada exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear la categoría.");
        }
        return "admin/crearCategoria";
    }

    // Mostrar formulario para editar categoría
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id_categoria, Model model) {
        Categoria categoria = categoriaRepository.findById(id_categoria).orElse(null);
        if (categoria != null) {
            model.addAttribute("categoria", categoria);
            return "admin/editarCategoria";
        }
        model.addAttribute("error", "Categoría no encontrada.");
        return "redirect:/admin/categoria/listar";
    }

    // Editar categoría
    @PostMapping("/editar")
    public String editarCategoria(@ModelAttribute Categoria categoria, Model model) {
        try {
            categoriaRepository.save(categoria);
            model.addAttribute("success", "Categoría actualizada exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la categoría.");
        }
        return "redirect:/admin/categoria/listar";
    }

    // Eliminar categoría
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id_categoria, Model model) {
        try {
            categoriaRepository.deleteById(id_categoria);
            model.addAttribute("success", "Categoría eliminada exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar la categoría.");
        }
        return "redirect:/admin/categoria/listar";
    }
}
