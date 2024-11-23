package back.domain.mapper;

import back.domain.dto.request.ServicoRequestDTO;
import back.domain.dto.response.ServicoResponseDTO;
import back.domain.model.Servico;
import back.service.service.ServicoService;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public Servico toEntity(ServicoRequestDTO requestDTO) {
        Servico result = null;
        if (requestDTO != null) {
            Servico servico = new Servico();
            servico.setNome(requestDTO.getNome());
            servico.setDescricao(requestDTO.getDescricao());
            servico.setPreco(requestDTO.getPreco());
            result = servico;
        }
        return result;

    }

    public ServicoResponseDTO toServicoResponseDto(Servico servico) {
        if (servico == null) {
            return null;
        }

        ServicoResponseDTO responseDTO = new ServicoResponseDTO();
        responseDTO.setNome(servico.getNome());
        responseDTO.setDescricao(servico.getDescricao());
        responseDTO.setPreco(servico.getPreco());

        return responseDTO;

    }
}

