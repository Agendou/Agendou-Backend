package back.domain.mapper;

import back.domain.dto.response.AvaliacaoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {
    public static AvaliacaoResponseDTO toDTO(Avaliacao avaliacao) {
        return new AvaliacaoResponseDTO(avaliacao.getAId(), avaliacao.getJfId(), avaliacao.getEstrelas());
    }
}
