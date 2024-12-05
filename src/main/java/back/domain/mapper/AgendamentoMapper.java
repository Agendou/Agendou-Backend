package back.domain.mapper;


import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import back.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO) {
        Agendamento agendamento = new Agendamento();

        agendamento.setId(agendamentoRequestDTO.getId());
        agendamento.setData(agendamentoRequestDTO.getData());

        if (agendamentoRequestDTO.getFkFuncionario() != null) {
            Funcionario funcionario = new Funcionario();
            funcionario.setId(agendamentoRequestDTO.getFkFuncionario());
            agendamento.setFkFuncionario(funcionario);
        }

        if (agendamentoRequestDTO.getFkUsuario() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(agendamentoRequestDTO.getFkUsuario());
            agendamento.setFkUsuario(usuario);
        }

        if (agendamentoRequestDTO.getFkServicos() != null) {
            List<Servico> servicos = agendamentoRequestDTO.getFkServicos().stream()
                    .map(id -> {
                        Servico servico = new Servico();
                        servico.setId(id);
                        return servico;
                    }).toList();
            agendamento.setFkServicos(servicos);
        }

        return agendamento;
    }


    public AgendamentoResponseDTO toAgendamentoResponseDto(Agendamento entity) {

        if (entity == null) {
            return null;
        }

        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();

        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setFkFuncionario(entity.getFkFuncionario());
        dto.setFkUsuario(entity.getFkUsuario());
        dto.setFkServicos(entity.getFkServicos());
        dto.setFkAvaliacao(entity.getFkAvaliacao());

        return dto;
    }
}
