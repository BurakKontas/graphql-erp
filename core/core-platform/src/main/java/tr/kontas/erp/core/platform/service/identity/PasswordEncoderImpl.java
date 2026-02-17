package tr.kontas.erp.core.platform.service.identity;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.identity.PasswordEncoder;

@Service
public class PasswordEncoderImpl implements PasswordEncoder {
    private static final int STRENGTH = 12;

    @Override
    public boolean matches(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public String hash(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(STRENGTH));
    }
}
