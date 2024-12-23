package pe.edu.cibertec.ProyectoDAWI.response;

import pe.edu.cibertec.ProyectoDAWI.dto.ProductoDetailDto;

public record GetDetailProductoResponse(String code,
                                        String error,
                                        ProductoDetailDto data) {
}
