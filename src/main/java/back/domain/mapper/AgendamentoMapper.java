package back.domain.mapper;


import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO) {
        if (agendamentoRequestDTO == null) {return null;}
        return Agendamento.builder()
                .descricao(agendamentoRequestDTO.getDescricao())
                .data(agendamentoRequestDTO.getData())
                .build();
    }

    public AgendamentoResponseDTO toDTO(Agendamento entity) {
        if (entity == null) {return null;}
        return AgendamentoResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .data(entity.getData())
                .build();
    }
}
