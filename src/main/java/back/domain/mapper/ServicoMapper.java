package back.domain.mapper;

import back.domain.dto.request.ServicoRequestDTO;
import back.domain.dto.response.ServicoResponseDTO;
import back.domain.model.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public Servico toEntity(ServicoRequestDTO servicoRequestDTO) {
        if(servicoRequestDTO == null) return null;

        Servico servico = new Servico();
        servico.setId(servicoRequestDTO.getId());
        servico.setNome(servicoRequestDTO.getNome());
        servico.setDescricao(servicoRequestDTO.getDescricao());
        servico.setPreco(servicoRequestDTO.getPreco());

        return servico;
    }

    public ServicoResponseDTO toServicoResponseDto(Servico servico) {
        if (servico == null) return null;

        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getPreco(),
                servico.getDescricao()
        );
    }
}

