package back.domain.mapper;


import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.dto.response.AgendamentoSimplificadoResponseDTO;
import back.domain.model.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO) {
        if (agendamentoRequestDTO == null) return null;

        Agendamento agendamento = new Agendamento();

        agendamento.setId(agendamentoRequestDTO.getId());
        agendamento.setData(agendamentoRequestDTO.getData());
        agendamento.setDescricao(agendamentoRequestDTO.getDescricao());
        agendamento.setStatus(agendamentoRequestDTO.getStatus());

        if (agendamentoRequestDTO.getFkUsuario() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(agendamentoRequestDTO.getFkUsuario());
            agendamento.setFkUsuario(usuario);
        }

        if (agendamentoRequestDTO.getFkServico() != null) {
            Servico servico = new Servico();
            servico.setId(agendamentoRequestDTO.getFkServico());
            agendamento.setFkServico(servico);
        }

        if (agendamentoRequestDTO.getFkEmpresa() != null) {
            Empresa empresa = new Empresa();
            empresa.setId(agendamentoRequestDTO.getFkEmpresa());
            agendamento.setFkEmpresa(empresa);
        }
        return agendamento;
    }

    public static AgendamentoSimplificadoResponseDTO toAgendamentoSimplificadoResponseDTO(Agendamento agendamento) {
        if(agendamento == null || agendamento.getFkUsuario() == null || agendamento.getData() == null) {
            return null;
        }

        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return new AgendamentoSimplificadoResponseDTO(
                agendamento.getFkUsuario().getNome(),
                agendamento.getData().format(dataFormatter),
                agendamento.getData().format(horaFormatter)
        );
    }
    public AgendamentoResponseDTO toAgendamentoResponseDto(Agendamento entity) {
        if (entity == null) return null;

        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();

        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setDescricao(entity.getDescricao());
        dto.setFkUsuario(entity.getFkUsuario());
        dto.setFkServico(entity.getFkServico());
        dto.setFkEmpresa(entity.getFkEmpresa());

        return dto;
    }
}
