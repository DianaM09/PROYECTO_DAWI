package pe.edu.cibertec.ProyectoDAWI.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.ProyectoDAWI.dto.CategoriaDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoCreateDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDetailDto;
import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;
import pe.edu.cibertec.ProyectoDAWI.dto.CategoriaDto;
import pe.edu.cibertec.ProyectoDAWI.entity.Categoria;
import pe.edu.cibertec.ProyectoDAWI.entity.Producto;
import pe.edu.cibertec.ProyectoDAWI.entity.Usuario;

import pe.edu.cibertec.ProyectoDAWI.repository.CategoriaRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.ProductoRepository;
import pe.edu.cibertec.ProyectoDAWI.repository.UsuarioRepository;
import pe.edu.cibertec.ProyectoDAWI.service.ManageProductoService;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ManageProductoServiceImpl implements ManageProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    // Obtener todos los productos
    @Override
    public List<ProductoDto> getAllProductos() {
        Iterable<Producto> productosIterable = productoRepository.findAll();

        // Convertir Iterable a List
        List<Producto> productos = StreamSupport.stream(productosIterable.spliterator(), false)
                .collect(Collectors.toList());

        return productos.stream().map(producto -> {
            ProductoDto dto = new ProductoDto();
            dto.setId_producto(producto.getId_producto());
            dto.setCodigo(producto.getCodigo());
            dto.setFoto(producto.getFoto());
            dto.setDescripcion(producto.getDescripcion());
            dto.setPrecio(producto.getPrecio());
            dto.setStock(producto.getStock());
            // Mapeo de la categoría
            if (producto.getCategoria() != null) {
                dto.setCategoriaDescripcion(producto.getCategoria().getDescripcion());
            }
            return dto;
        }).collect(Collectors.toList());
    }


    // Obtener detalle de un producto
    @Override
    public Optional<ProductoDetailDto> getDetailProducto(Integer id_producto) {
        Optional<Producto> productoOpt = productoRepository.findById(id_producto);
        return productoOpt.map(producto -> {
            ProductoDetailDto dto = new ProductoDetailDto();
            dto.setFoto(producto.getFoto());
            dto.setId_producto(producto.getId_producto());
            dto.setCodigo(producto.getCodigo());
            dto.setDescripcion(producto.getDescripcion());
            dto.setCategoria(producto.getCategoria());
            dto.setPrecio(producto.getPrecio());
            dto.setStock(producto.getStock());
            dto.setUsuarioRegistro(producto.getUsuarioRegistro());
            dto.setFechaRegistro(producto.getFechaRegistro());
            dto.setFechaModificacion(producto.getFechaModificacion());
            dto.setEstado(producto.getEstado());
            return dto;
        });
    }

    // Actualizar un producto
    @Override
    public boolean updateProducto(ProductoDto productoDto) {
        Optional<Producto> optionalProducto = productoRepository.findById(productoDto.getId_producto());
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setFoto(productoDto.getFoto());
            producto.setCodigo(productoDto.getCodigo());
            producto.setDescripcion(productoDto.getDescripcion());
            producto.setPrecio(productoDto.getPrecio());
            producto.setStock(productoDto.getStock());
            productoRepository.save(producto);  // Guardamos el producto actualizado
            return true;
        }
        return false;
    }

    // Eliminar un producto
    @Override
    public boolean deleteProducto(Integer idProducto) {
        Optional<Producto> optionalProducto = productoRepository.findById(idProducto);
        if (optionalProducto.isPresent()) {
            productoRepository.deleteById(idProducto);  // Eliminamos el producto por id
            return true;
        }
        return false;
    }

    // Crear un nuevo producto
    @Override
    public boolean createProducto(ProductoCreateDto productoCreateDto) {
        if (productoCreateDto.getId_categoria() == null || productoCreateDto.getId_usuario_registro() == null) {
            // Puedes agregar un log o mensaje para indicar que los valores son nulos
            return false;
        }
        Optional<Categoria> categoria = categoriaRepository.findById(productoCreateDto.getId_categoria());
        Optional<Usuario> usuario = usuarioRepository.findById(productoCreateDto.getId_usuario_registro());
        if (categoria.isPresent() && usuario.isPresent()) {
            Producto producto = new Producto();
            producto.setFoto(productoCreateDto.getFoto());
            producto.setCodigo(productoCreateDto.getCodigo());
            producto.setDescripcion(productoCreateDto.getDescripcion());
            producto.setCategoria(categoria.get());
            producto.setPrecio(productoCreateDto.getPrecio());
            producto.setStock(productoCreateDto.getStock());
            producto.setUsuarioRegistro(usuario.get());
            producto.setFechaRegistro(new java.util.Date()); // Se asigna la fecha actual
            producto.setFechaModificacion(new java.util.Date());
            producto.setEstado((short) 1); // Por defecto, estado activo (1)
            productoRepository.save(producto); // Guardamos el producto
            return true;
        }
        return false;
    }
    @Override
    public List<CategoriaDto> getAllCategorias() {
        // Convertir Iterable a List y luego usar stream
        List<Categoria> categorias = StreamSupport.stream(categoriaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return categorias.stream()
                .map(c -> new CategoriaDto(c.getId_categoria(), c.getDescripcion()))
                .collect(Collectors.toList());
    }
}
