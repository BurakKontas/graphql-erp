package tr.kontas.erp.core.application.identity;

public interface PasswordEncoder {
    boolean matches(String password, String hashedPassword);

    String hash(String password);
}
