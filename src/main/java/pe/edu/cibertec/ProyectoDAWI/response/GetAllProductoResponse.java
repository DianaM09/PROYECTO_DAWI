package pe.edu.cibertec.ProyectoDAWI.response;

import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDto;

import java.util.List;

public record GetAllProductoResponse(String code,
                                     String error,
                                     List<ProductoDto> data) {
}
