package back.domain.enums;

public enum StatusAgendamento {
    AGENDADO("Agendado"),
    CANCELADO("Cancelado"),
    REALIZADO("Realizado"),
    ALTERADO("Alterado"),
    ADIADO("Adiado");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
