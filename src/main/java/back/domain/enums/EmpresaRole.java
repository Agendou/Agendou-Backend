package back.domain.enums;

public enum EmpresaRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String role;

    EmpresaRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
