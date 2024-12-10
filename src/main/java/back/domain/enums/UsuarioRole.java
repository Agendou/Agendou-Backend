package back.domain.enums;

public enum UsuarioRole {

    USER("ROLE_USER");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
