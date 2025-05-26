package back.service.service;


import back.domain.dto.request.ServicoRequestDTO;
import back.domain.dto.response.ServicoResponseDTO;
import back.domain.mapper.ServicoMapper;
import back.domain.model.Servico;
import back.domain.repository.ServicoRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServicoService {

    private ServicoRepository repository;
    private ServicoMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(ServicoService.class);

    public List<ServicoResponseDTO> listarServicos() {
        List<Servico> servicos = repository.findAll();
        return servicos.stream()
                .map(mapper::toServicoResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> buscarServicoPorID(Integer id) {
        Optional<Servico> servicoExistente = repository.findById(id);

        if (servicoExistente.isEmpty()) {
            logger.error("Serviço com id " + id + " não encontrado");
            return ResponseEntity.status(404).body("Serviço não encontrado");
        }

        Servico servico = servicoExistente.get();
        ServicoResponseDTO responseDTO = mapper.toServicoResponseDto(servico);

        return ResponseEntity.status(200).body(responseDTO);
    }

    public ResponseEntity<?> atualizarServico(Integer id, ServicoRequestDTO servicoRequest) {
        Optional<Servico> optionalServico = repository.findById(id);

        if (optionalServico.isEmpty()) {
            logger.error("Falha ao atualizar o serviço: Serviço não encontrado");
            return ResponseEntity.status(404).body("Serviço não encontrado.");
        }

            Servico servico = optionalServico.get();
            servico.setNome(servicoRequest.getNome());
            servico.setPreco(servicoRequest.getPreco());
            servico.setDescricao(servicoRequest.getDescricao());

            repository.save(servico);

            return ResponseEntity.status(200).body(mapper.toServicoResponseDto(servico));
    }


    public ResponseEntity<?> deletarServico(Integer id) {
        Optional<Servico> servicoExistente = repository.findById(id);

        if (servicoExistente.isEmpty()){
            logger.error("Falha ao deletar o serviço: Serviço não encontrado");
            return ResponseEntity.status(404).body("Serviço não encontrado.");
        }

        Servico servico = servicoExistente.get();
        logger.info("Serviço deletado com sucesso: " + servico.getNome());
        repository.delete(servico);

        return ResponseEntity.status(200).body(servico);
    }

    public ResponseEntity<?> cadastrarServico(ServicoRequestDTO dto) {

        Servico servico = mapper.toEntity(dto);
        servico.setId(dto.getId());
        servico.setNome(dto.getNome());
        servico.setPreco(dto.getPreco());
        servico.setDescricao(dto.getDescricao());
        Servico servicoSalvo = repository.save(servico);

        ServicoResponseDTO responseDTO = mapper.toServicoResponseDto(servicoSalvo);

        if (responseDTO == null) {
            logger.error("Falha ao cadastrar o serviço: O serviço está vazio");
            return ResponseEntity.status(400).build();
        }

        logger.info("Serviço cadastrado com sucesso: " + responseDTO.getNome());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    public byte[] getServicosCsv() throws IOException {

        List<Servico> servicos = repository.findAll();
        List<ServicoResponseDTO> servicoCsvDtos = servicos.stream()
                .map(servico -> {
                    try {
                        return mapper.toServicoResponseDto(servico);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao mapear Servico para ServicoRequestDTO", e);
                    }
                })
                .toList();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {

            writer.write("Nome;Descrição;Preço\n");

            for (ServicoResponseDTO dto : servicoCsvDtos) {
                writer.write(String.format("%s;%s;%.2f\n",
                        dto.getNome(),
                        dto.getDescricao(),
                        dto.getPreco()));
            }

            writer.flush();
            return outputStream.toByteArray();
        }
    }
}
