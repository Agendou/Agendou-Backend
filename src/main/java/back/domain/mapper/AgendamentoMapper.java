package back.domain.mapper;


import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import back.domain.model.Agendamento;
import back.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO) {
        Agendamento agendamento = new Agendamento();

        agendamento.setDataHoraCorte(agendamentoRequestDTO.getDataHoraCorte());
        agendamento.setProfissional(agendamentoRequestDTO.getProfissional());
        return agendamento;
    }


    public AgendamentoResponseDTO toAgendamentoResponseDto(Agendamento entity) {

        if (entity == null) {
            return null;
        }

        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();

        dto.setId(entity.getId());
        dto.setDataHoraCorte(entity.getDataHoraCorte());
        dto.setProfissional(entity.getProfissional());
        return dto;
    }
}
