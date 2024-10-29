package back.domain.mapper;

import back.domain.dto.request.FuncionarioRequestDTO;
import back.domain.dto.response.FuncionarioResponseDTO;
import back.domain.model.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioRequestDTO funcionarioRequestDTO){
        Funcionario result = null;
        if(funcionarioRequestDTO != null){
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(funcionarioRequestDTO.getNome());
            funcionario.setEmail(funcionarioRequestDTO.getEmail());
            funcionario.setTelefone(funcionarioRequestDTO.getTelefone());
            funcionario.setSenha(funcionarioRequestDTO.getSenha());
            result = funcionario;
        }
        return result;
    }

    public FuncionarioResponseDTO toFuncionarioResponseDto(Funcionario funcionario){
        if (funcionario == null){
            return null;
        }

        FuncionarioResponseDTO funcionarioResponseDTO = new FuncionarioResponseDTO();
        funcionarioResponseDTO.setId(funcionario.getId());
        funcionarioResponseDTO.setNome(funcionario.getNome());
        funcionarioResponseDTO.setEmail(funcionario.getEmail());
        funcionarioResponseDTO.setSenha(funcionario.getSenha());
        funcionarioResponseDTO.setTelefone(funcionario.getTelefone());

        return funcionarioResponseDTO;
    }
}
