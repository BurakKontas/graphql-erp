package tr.kontas.erp.core.platform.service.identity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.identity.JwtPrincipal;
import tr.kontas.erp.core.application.identity.JwtService;
import tr.kontas.erp.core.application.identity.TokenResult;
import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.repositories.PermissionRepository;
import tr.kontas.erp.core.domain.identity.repositories.RoleRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.platform.configuration.JwtProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtServiceImpl implements JwtService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final SecretKey key;
    private final long accessExpirationSec;
    private final long refreshExpirationSec;

    public JwtServiceImpl(JwtProperties properties, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.key = Keys.hmacShaKeyFor(
                properties.secret().getBytes(StandardCharsets.UTF_8)
        );
        this.accessExpirationSec = properties.expirationSeconds();
        this.refreshExpirationSec = properties.refreshExpirationSeconds();

        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public TokenResult generate(UserAccount user) {

        Instant now = Instant.now();

        Instant accessExp = now.plusSeconds(accessExpirationSec);
        Instant refreshExp = now.plusSeconds(refreshExpirationSec);

        Set<String> roles = user.getRoles()
                .stream()
                .map(roleId -> roleId.getValue().toString())
                .collect(Collectors.toSet());

        Set<String> permissions = resolvePermissions(user);

        // ACCESS TOKEN
        String accessToken = Jwts.builder()
                .subject(user.getId().getValue().toString())
                .claim("tenant", user.getTenantId().getValue().toString())
                .claim("username", user.getUsername().getValue())
                .claim("roles", roles)
                .claim("type", "access")
                .claim("permissions", permissions)
                .claim("authVersion", user.getAuthVersion())
                .issuedAt(Date.from(now))
                .expiration(Date.from(accessExp))
                .signWith(key)
                .compact();

        // REFRESH TOKEN
        String refreshToken = Jwts.builder()
                .subject(user.getId().getValue().toString())
                .claim("type", "refresh")
                .claim("authVersion", user.getAuthVersion())
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshExp))
                .signWith(key)
                .compact();

        return new TokenResult(
                accessToken,
                accessExp.getEpochSecond(),
                refreshToken,
                refreshExp.getEpochSecond()
        );
    }

    @Override
    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public JwtPrincipal parse(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (!"access".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid token type");
        }

        @SuppressWarnings("unchecked")
        List<String> rolesList = claims.get("roles", List.class);
        Set<String> roles = new HashSet<>(rolesList);

        @SuppressWarnings("unchecked")
        List<String> permissionsList = claims.get("permissions", List.class);
        Set<String> permissions = new HashSet<>(permissionsList);

        return new JwtPrincipal(
                UUID.fromString(claims.getSubject()),
                UUID.fromString(claims.get("tenant", String.class)),
                claims.get("username", String.class),
                roles,
                permissions,
                claims.get("authVersion", Long.class)
        );
    }

    @Override
    public UserId parseRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid refresh token");
        }

        return UserId.of(UUID.fromString(claims.getSubject()));
    }

    @Override
    public RefreshTokenInfo parseRefreshTokenWithAuthVersion(String refreshToken) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid refresh token");
        }

        UserId userId = UserId.of(UUID.fromString(claims.getSubject()));
        long authVersion = claims.get("authVersion", Long.class);

        return new RefreshTokenInfo(userId, authVersion);
    }

    private Set<String> resolvePermissions(UserAccount user) {
        if (user.getRoles().isEmpty()) {
            return Set.of();
        }

        Set<Role> roles = roleRepository.findAllByIds(user.getRoles());

        if (roles.isEmpty()) {
            return Set.of();
        }

        List<PermissionId> permissionIds = roles.stream()
                .map(Role::getPermissions)
                .flatMap(Set::stream)
                .toList();

        if (permissionIds.isEmpty()) {
            return Set.of();
        }

        List<Permission> permissions = permissionRepository.findAllByIds(permissionIds);

        return permissions
                .stream()
                .map(Permission::asKey)
                .collect(Collectors.toSet());
    }
}

