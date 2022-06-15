package sogorae.auth.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final String EMAIL = "email";

    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtProvider(@Value("${security.jwt.token.secret-key}") String key,
      @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
          .claim(EMAIL, email)
          .setIssuedAt(now)
          .setExpiration(validity)
          .signWith(secretKey, SignatureAlgorithm.HS256)
          .compact();
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = getClaims(token);

            return !claims.getBody()
              .getExpiration()
              .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
    }

    public String getPayload(String token) {
        return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .get(EMAIL, String.class);
    }
}
