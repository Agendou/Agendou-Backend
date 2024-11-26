package back.domain.mapper;

import back.domain.dto.request.ServicosVinculadosRequestDTO;
import back.domain.dto.response.ServicosVinculadosResponseDTO;
import back.domain.model.ServicosVinculados;
import org.springframework.stereotype.Component;

@Component
public class ServicosVinculadosMapper {

    public ServicosVinculados toEntity(ServicosVinculadosRequestDTO requestDTO) {
        ServicosVinculados result = null;
        if (requestDTO != null) {
            ServicosVinculados servicosVinculados = new ServicosVinculados();
            servicosVinculados.setFk_funcionario(requestDTO.getFk_funcionario());
            servicosVinculados.setFk_servico(requestDTO.getFk_servico());
            result = servicosVinculados;
        }
        return result;
    }

    public ServicosVinculadosResponseDTO toServicosVinculadosResponseDto(ServicosVinculados servicosVinculados) {
        if (servicosVinculados == null) {
            return null;
        }

        ServicosVinculadosResponseDTO responseDTO = new ServicosVinculadosResponseDTO();
        responseDTO.setFk_funcionario(servicosVinculados.getFk_funcionario());
        responseDTO.setFk_servico(servicosVinculados.getFk_servico());

        return responseDTO;
    }
}
