package pe.edu.cibertec.ProyectoDAWI.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoCreateDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDetailDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;
import pe.edu.cibertec.ProyectoDAWI.response.*;
import pe.edu.cibertec.ProyectoDAWI.service.ManageProductoService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ManageProductoApi {

    @Autowired
    ManageProductoService productoService;

    // Obtener todos los productos
    @GetMapping("/all")
    public GetAllProductoResponse getAllProductos() {

        try {
            List<ProductoDto> productos = productoService.getAllProductos();
            if (!productos.isEmpty())
                return new GetAllProductoResponse("01", null, productos);
            else
                return new GetAllProductoResponse("02", "No se encontraron productos", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new GetAllProductoResponse("99", "Ocurrió un error, por favor intente nuevamente", null);
        }
    }

    // Obtener detalle de un producto
    @GetMapping("/detail")
    public GetDetailProductoResponse getDetailProducto(@RequestParam(value = "id", defaultValue = "0") String id) {

        try {
            Optional<ProductoDetailDto> optional = productoService.getDetailProducto(Integer.parseInt(id));
            return optional.map(producto ->
                    new GetDetailProductoResponse("01", null, producto)
            ).orElse(
                    new GetDetailProductoResponse("02", "Producto no encontrado", null)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new GetDetailProductoResponse("99", "Ocurrió un error, por favor intente nuevamente", null);
        }
    }

    // Actualizar un producto
    @PutMapping("/update")
    public UpdateProductoResponse updateProducto(@RequestBody ProductoDto productoDto) {

        try {
            if (productoService.updateProducto(productoDto))
                return new UpdateProductoResponse("01", null);
            else
                return new UpdateProductoResponse("02", "Actualización fallida");

        } catch (Exception e) {
            e.printStackTrace();
            return new UpdateProductoResponse("99", "Ocurrió un error, por favor intente nuevamente");
        }
    }

    // Eliminar un producto
    @DeleteMapping("/delete/{id}")
    public DeleteProductoResponse deleteProducto(@PathVariable String id) {

        try {
            if (productoService.deleteProducto(Integer.parseInt(id)))
                return new DeleteProductoResponse("01", null);
            else
                return new DeleteProductoResponse("02", "Eliminación fallida");

        } catch (Exception e) {
            e.printStackTrace();
            return new DeleteProductoResponse("99", "Ocurrió un error, por favor intente nuevamente");
        }
    }

    // Crear un nuevo producto
    @PostMapping("/create")
    public CreateProductoResponse createProducto(@RequestBody ProductoCreateDto productoCreateDto) {

        try {
            if (productoService.createProducto(productoCreateDto))
                return new CreateProductoResponse("01", null);
            else
                return new CreateProductoResponse("02", "Creación fallida");

        } catch (Exception e) {
            e.printStackTrace();
            return new CreateProductoResponse("99", "Ocurrió un error, por favor intente nuevamente");
        }
    }
}
