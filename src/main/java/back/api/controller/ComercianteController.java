package back.api.controller;

import back.domain.dto.request.ComercianteRequestDTO;
import back.domain.dto.response.ComercianteResponseDTO;
import back.domain.mapper.ComercianteMapper;
import back.domain.model.Comerciante;
import back.domain.repository.ComercianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comerciantes")
public class ComercianteController {

    @Autowired
    private ComercianteRepository comercianteRepository;

    @Autowired
    private ComercianteMapper comercianteMapper;

    @GetMapping
    public List<ComercianteResponseDTO> listarComerciantes() {
        return comercianteRepository.findAll().stream()
                .map(comercianteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ComercianteResponseDTO listarComercianteById(@PathVariable Integer id) {
        return comercianteRepository.findById(id)
                .map(comercianteMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comerciante não encontrado com o id " + id));
    }

    @PostMapping
    public ComercianteResponseDTO criarComerciante(@RequestBody ComercianteRequestDTO comercianteDTO) {
        Comerciante comerciante = comercianteMapper.toEntity(comercianteDTO);
        Comerciante savedComerciante = comercianteRepository.save(comerciante);
        return comercianteMapper.toResponseDTO(savedComerciante);
    }

    @PutMapping("/{id}")
    public ComercianteResponseDTO atualizarComerciante(@PathVariable Integer id, @RequestBody ComercianteRequestDTO comercianteDTO) {
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comerciante não encontrado com o id " + id));
        comerciante.setNome(comercianteDTO.getNome());
        comerciante.setEmail(comercianteDTO.getEmail());
        comerciante.setTelefone(comercianteDTO.getTelefone());
        Comerciante updatedComerciante = comercianteRepository.save(comerciante);
        return comercianteMapper.toResponseDTO(updatedComerciante);
    }

    @DeleteMapping("/{id}")
    public void deleteComerciante(@PathVariable Integer id) {
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comerciante não encontrado com o id " + id));
        comercianteRepository.delete(comerciante);
    }
}