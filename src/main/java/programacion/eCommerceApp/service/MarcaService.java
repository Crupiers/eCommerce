package programacion.eCommerceApp.service;

import org.springframework.stereotype.Service;
import programacion.eCommerceApp.controller.request.NewMarcaRequest;
import programacion.eCommerceApp.controller.response.MarcaResponse;
import programacion.eCommerceApp.mapper.CategoriaMapper;
import programacion.eCommerceApp.mapper.MarcaMapper;
import programacion.eCommerceApp.model.Categoria;
import programacion.eCommerceApp.model.Marca;
import programacion.eCommerceApp.repository.IMarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class MarcaService implements IMarcaService {

    @Autowired
    private IMarcaRepository modelRepository; //el repository no es instanciado nunca

    @Override
    public List<MarcaResponse> listar() {
        List<Marca> marcas = modelRepository.findByEstado(Marca.COMUN);
        return marcas.stream().map(MarcaMapper::toMarcaResponse).toList();
    }

    @Override
    public Marca buscarPorId(Integer id) {
        return modelRepository.findById(id).orElse(null);
    }

    @Override
    public MarcaResponse crear(NewMarcaRequest newMarcaRequest) {
        Marca model = MarcaMapper.toEntity(newMarcaRequest);
        Optional<Marca> marcaOptional = modelRepository.findByDenominacion(model.getDenominacion());

        if (marcaOptional.isPresent()) {
            Marca marcaExistente = marcaOptional.get();
            if(marcaExistente.getEstado() == Marca.ELIMINADO){
                marcaExistente.recuperar();
                marcaExistente.setDenominacion(model.getDenominacion());
                marcaExistente.setObservaciones(model.getObservaciones());

                return MarcaMapper.toMarcaResponse(modelRepository.save(marcaExistente));
            }else{
                throw new IllegalArgumentException("La marca ya existe");
            }
        }
        return MarcaMapper.toMarcaResponse(modelRepository.save(model));
    }

    @Override
    public void eliminar(Marca model) {
        model.eliminar();
        modelRepository.save(model);
    }

    @Override
    public void recuperar(Marca model) {
        model.recuperar();
        modelRepository.save(model);
    }

    @Override
    public MarcaResponse actualizar(NewMarcaRequest newMarcaRequest, Integer id) {
        Marca model = MarcaMapper.toEntity(newMarcaRequest);
        Optional<Marca> marcaOptional = modelRepository.findById(id);

        if(marcaOptional.isPresent()){
            Marca marca = marcaOptional.get();
            marca.setDenominacion(model.getDenominacion());
            marca.setObservaciones(model.getObservaciones());
            return MarcaMapper.toMarcaResponse(modelRepository.save(marca));
        }else{
            throw new IllegalArgumentException("La marca no existe.");
        }
    }
}
