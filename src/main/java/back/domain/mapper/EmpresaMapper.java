package back.domain.mapper;

import back.domain.dto.request.EmpresaRequestDTO;
import back.domain.dto.response.EmpresaResponseDTO;
import back.domain.model.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDTO empresaRequestDTO){
        if(empresaRequestDTO == null) return null;

        Empresa empresa = new Empresa();
        empresa.setId(empresaRequestDTO.getId());
        empresa.setNomeEmpresa(empresaRequestDTO.getNomeEmpresa());
        empresa.setRepresentante(empresaRequestDTO.getRepresentante());
        empresa.setEmail(empresaRequestDTO.getEmail());
        empresa.setSenha(empresaRequestDTO.getSenha());
        empresa.setTelefone(empresaRequestDTO.getTelefone());
        empresa.setCnpj(empresaRequestDTO.getCnpj());
        empresa.setRole(empresaRequestDTO.getRole());

        return empresa;
    }

    public EmpresaResponseDTO toEmpresaResponseDto(Empresa empresa){
        if (empresa == null) return null;

        EmpresaResponseDTO empresaResponseDTO = new EmpresaResponseDTO();
        empresaResponseDTO.setId(empresa.getId());
        empresaResponseDTO.setNomeEmpresa(empresa.getNomeEmpresa());
        empresaResponseDTO.setRepresentante(empresa.getRepresentante());
        empresaResponseDTO.setEmail(empresa.getEmail());
        empresaResponseDTO.setSenha(empresa.getSenha());
        empresaResponseDTO.setTelefone(empresa.getTelefone());
        empresaResponseDTO.setCnpj(empresa.getCnpj());
        empresaResponseDTO.setRole(empresa.getRole());

        return empresaResponseDTO;
    }
}
