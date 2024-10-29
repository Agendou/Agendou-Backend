package back.domain.mapper;

import back.domain.dto.request.EmpresaRequestDTO;
import back.domain.dto.response.EmpresaResponseDTO;
import back.domain.model.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDTO empresaRequestDTO){
        Empresa result = null;
        if (empresaRequestDTO != null){
            Empresa empresa = new Empresa();
            empresa.setNomeEmpresa(empresa.getNomeEmpresa());
            empresa.setEmail(empresa.getEmail());
            empresa.setRepresentante(empresa.getRepresentante());
            empresa.setSenha(empresa.getSenha());
            empresa.setTelefone(empresa.getTelefone());
            empresa.setCnpj(empresa.getCnpj());
            result = empresa;
        }
        return result;
    }

    public EmpresaResponseDTO toEmpresaResponseDto(Empresa empresa){
        if (empresa == null){
            return null;
        }

        EmpresaResponseDTO empresaResponseDTO = new EmpresaResponseDTO();
        empresaResponseDTO.setId(empresa.getId());
        empresaResponseDTO.setNomeEmpresa(empresa.getNomeEmpresa());
        empresaResponseDTO.setRepresentante(empresa.getRepresentante());
        empresaResponseDTO.setEmail(empresa.getEmail());
        empresaResponseDTO.setSenha(empresa.getSenha());
        empresaResponseDTO.setTelefone(empresa.getTelefone());
        empresaResponseDTO.setCnpj(empresa.getCnpj());

        return empresaResponseDTO;
    }
}
