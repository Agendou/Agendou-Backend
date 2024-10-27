package back.domain.mapper;

import back.domain.dto.request.EmpresaRequestDTO;
import back.domain.dto.response.EmpresaResponseDTO;
import back.domain.model.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDTO requestDTO) throws Exception {
        try {
            Empresa empresa = new Empresa();
            empresa.setNomeEmpresa(requestDTO.getNomeEmpresa());
            empresa.setCnpj(requestDTO.getCnpj());
            empresa.setSenha(requestDTO.getSenha());
            empresa.setEmail(requestDTO.getEmail());
            empresa.setTelefone(requestDTO.getTelefone());
            empresa.setRepresentanteLegal(requestDTO.getRepresentanteLegal());
            return empresa;
        } catch (Exception e) {
            throw new Exception("Erro ao mapear EmpresaRequestDTO para entidade: " + e.getMessage());
        }
    }

    public EmpresaResponseDTO toDTO(Empresa entity){
        try {
            EmpresaResponseDTO dto = new EmpresaResponseDTO();
            dto.setId(entity.getId());
            dto.setCnpj(entity.getCnpj());
            dto.setNomeEmpresa(entity.getNomeEmpresa());
            dto.setEmail(entity.getEmail());
            dto.setTelefone(entity.getTelefone());
            dto.setSenha(entity.getSenha());
            dto.setRepresentanteLegal(entity.getRepresentanteLegal());
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível mapear o usuário para o DTO: " + e.getMessage());
        }
    }
}
