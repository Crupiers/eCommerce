package programacion.eCommerceApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import programacion.eCommerceApp.DTO.CategoriaDTO;
import programacion.eCommerceApp.Mapper.CategoriaMapper;
import programacion.eCommerceApp.exception.RecursoNoEncontradoExcepcion;
import programacion.eCommerceApp.model.Categoria;
import programacion.eCommerceApp.model.Marca;
import programacion.eCommerceApp.service.ICategoriaService;

import java.util.List;

@RestController
@RequestMapping("/eCommerce")
@CrossOrigin(value=" http://localhost:8080")

public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    @Autowired
    private ICategoriaService modelService;

    @GetMapping("/categoria")
    public List<CategoriaDTO> getAll() {
        logger.info("entra y trae todas las Categorias");
        return modelService.listar();

    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaDTO> getPorId(@PathVariable Integer id){
        Categoria model = modelService.buscarPorId(id);

        if(model == null){
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        }
        CategoriaDTO modelDTO = CategoriaMapper.toDTO(model);
        return ResponseEntity.ok(modelDTO);
    }

    @PostMapping("/categoria")
    public CategoriaDTO guardar(@RequestBody CategoriaDTO model){

        return modelService.guardar(model);
    }

    @PutMapping("/categoria")
    public CategoriaDTO actualizar(@RequestBody CategoriaDTO model){

        return modelService.guardar(model);
    }

    @PutMapping("/categoria/{id}")
    public ResponseEntity<Void> recuperar(@PathVariable Integer id) {
        Categoria model = modelService.buscarPorId(id);
        if (model == null) {
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
        }

        model.recuperar(); // Cambia el estado a COMUN
        modelService.guardar(model); // Guarda el modelo actualizado

        return ResponseEntity.ok().build(); // Respuesta vacía con estado 200 OK
    }

    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {


        Categoria model = modelService.buscarPorId(id);
        if (model == null){
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
        }

        model.eliminar();
        modelService.eliminar(model);
        return ResponseEntity.ok().build();
    }
}
