package programacion.eCommerceApp.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import programacion.eCommerceApp.controller.request.NewCategoriaRequest;
import programacion.eCommerceApp.controller.response.CategoriaResponse;
import programacion.eCommerceApp.mapper.CategoriaMapper;
import programacion.eCommerceApp.model.Categoria;
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
    public List<CategoriaResponse> getAll() {
        logger.info("Entra y trae todas las categorias.");
        return modelService.listar();
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Integer id){
        Categoria model = modelService.buscarPorId(id);

        if(model == null || model.getEstado() == Categoria.ELIMINADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO SE ENCONTRÓ EL ID: "+id);
        }

        CategoriaResponse categoriaResponse = CategoriaMapper.toCategoriaResponse(model);
        return ResponseEntity.ok(categoriaResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categoria")
    public CategoriaResponse crear(@RequestBody @Valid NewCategoriaRequest newCategoriaRequest){
        return modelService.crear(newCategoriaRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/categoria/actualizar/{id}")
    public CategoriaResponse actualizar(@RequestBody @Valid NewCategoriaRequest newCategoriaRequest, @PathVariable Integer id){
        return modelService.actualizar(newCategoriaRequest, id);
    }

    @PutMapping("/categoria/recuperar/{id}")
    public ResponseEntity<Void> recuperar(@PathVariable Integer id) {
        Categoria model = modelService.buscarPorId(id);

        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO SE ENCONTRÓ EL ID: "+id);
        }

        modelService.recuperar(model); // Guarda el modelo actualizado
        return ResponseEntity.ok().build(); // Respuesta vacía con estado 200 OK
    }

    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        Categoria model = modelService.buscarPorId(id);

        if (model == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO SE ENCONTRÓ EL ID: "+id);
        }

        modelService.eliminar(model);
        return ResponseEntity.ok().build();
    }
}
