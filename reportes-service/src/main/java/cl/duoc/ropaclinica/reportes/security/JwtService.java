package cl.duoc.ropaclinica.reportes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Solo valida tokens: los emite auth-service. Debe usar el MISMO secreto. */
@Service
public class JwtService {
    private final SecretKey key;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // Verifica firma y expiración; lanza JwtException si el token no sirve.
    public Claims leer(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
