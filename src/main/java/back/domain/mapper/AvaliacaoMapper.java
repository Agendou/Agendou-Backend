package back.domain.mapper;

import back.domain.dto.response.AvaliacaoResponseDTO;
import back.domain.model.Avaliacao;

public class AvaliacaoMapper {
    public static AvaliacaoResponseDTO toDTO(Avaliacao avaliacao) {
        return new AvaliacaoResponseDTO(avaliacao.getFId(), avaliacao.getEstrelas());
    }
}
