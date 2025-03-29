package br.com.pugliese.authorization.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtServiceTest {

    private JwtService jwtServiceDefault;
    private JwtService jwtServiceCustom;
    private UserDetails userDetails;
    private static final String CUSTOM_SECRET_KEY = "7A25432A462D4A614E645267556B58703273357638792F423F4528482B4D6251";

    @BeforeEach
    public void setUp() {
        jwtServiceDefault = new JwtService();
        jwtServiceCustom = new JwtService(CUSTOM_SECRET_KEY);
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    public void testExtractSubWithDefaultKey() {
        String token = jwtServiceDefault.generateToken(userDetails);
        String username = jwtServiceDefault.extractSub(token);
        assertEquals("testuser", username, "O subject extraído com a chave padrão deve ser igual ao username");
    }

    @Test
    public void testExtractSubWithCustomKey() {
        String token = jwtServiceCustom.generateToken(userDetails);
        String username = jwtServiceCustom.extractSub(token);
        assertEquals("testuser", username, "O subject extraído com a chave personalizada deve ser igual ao username");
    }

    @Test
    public void testExtractClaimWithDefaultKey() {
        String token = jwtServiceDefault.generateToken(userDetails);
        Date issuedAt = jwtServiceDefault.extractClaim(token, Claims::getIssuedAt);
        assertNotNull(issuedAt, "A data de emissão com a chave padrão não deve ser nula");
        assertTrue(issuedAt.before(new Date()) || issuedAt.equals(new Date()),
                "A data de emissão deve ser anterior ou igual ao momento atual");
    }

    @Test
    public void testGenerateTokenWithoutExtraClaimsWithDefaultKey() {
        String token = jwtServiceDefault.generateToken(userDetails);
        assertNotNull(token, "O token gerado com a chave padrão não deve ser nulo");
        assertFalse(token.isEmpty(), "O token gerado não deve ser vazio");

        String subject = jwtServiceDefault.extractClaim(token, Claims::getSubject);
        assertEquals("testuser", subject, "O subject do token deve corresponder ao username");
    }

    @Test
    public void testGenerateTokenWithExtraClaimsWithCustomKey() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        String token = jwtServiceCustom.generateToken(extraClaims, userDetails);

        assertNotNull(token, "O token gerado com claims extras e chave personalizada não deve ser nulo");

        Claims claims = jwtServiceCustom.extractAllClaims(token);
        assertEquals("testuser", claims.getSubject(), "O subject do token deve corresponder ao username");
        assertEquals("ADMIN", claims.get("role"), "O claim extra 'role' deve estar presente e correto");
    }

    @Test
    public void testIsTokenValidWithValidTokenDefaultKey() {
        String token = jwtServiceDefault.generateToken(userDetails);
        assertTrue(jwtServiceDefault.isTokenValid(token, userDetails),
                "O token válido com a chave padrão deve retornar true para o mesmo UserDetails");
    }

    @Test
    public void testIsTokenValidWithValidTokenCustomKey() {
        String token = jwtServiceCustom.generateToken(userDetails);
        assertTrue(jwtServiceCustom.isTokenValid(token, userDetails),
                "O token válido com a chave personalizada deve retornar true para o mesmo UserDetails");
    }

    @Test
    public void testIsTokenValidWithInvalidUsername() {
        String token = jwtServiceDefault.generateToken(userDetails);
        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("differentuser");
        assertFalse(jwtServiceDefault.isTokenValid(token, differentUser),
                "O token deve ser inválido para um UserDetails com username diferente");
    }

    @Test
    public void testIsTokenValidWithExpiredToken() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(CUSTOM_SECRET_KEY);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        Map<String, Object> claims = new HashMap<>();
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtServiceCustom.isTokenValid(expiredToken, userDetails));
    }

    @Test
    public void testExtractExpirationWithDefaultKey() {
        String token = jwtServiceDefault.generateToken(userDetails);
        Date expiration = jwtServiceDefault.extractExpiration(token);
        assertNotNull(expiration, "A data de expiração com a chave padrão não deve ser nula");
        assertTrue(expiration.after(new Date()), "A data de expiração deve estar no futuro");
    }

    @Test
    public void testTokenGeneratedWithDefaultKeyInvalidWithCustomKey() {
        String token = jwtServiceDefault.generateToken(userDetails);
        assertThrows(io.jsonwebtoken.SignatureException.class, () -> jwtServiceCustom.extractSub(token),
                "Um token gerado com a chave padrão deve falhar na validação com a chave personalizada");
    }
}