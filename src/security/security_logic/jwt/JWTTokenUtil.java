package security.security_logic.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import security.security_logic.entities.BlockedToken;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTTokenUtil {

    @Value("${jwt.maxExpiration}")
    private int expiration;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    MongoTemplate securityRepository;

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(Authentication authentication) {
        return Jwts.builder().setClaims(new HashMap<>())
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .claim("roles", authentication.getAuthorities())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public void deleteExpiatedTokensFromRepository(){
        long currTime = System.currentTimeMillis();
        for (BlockedToken token : securityRepository.findAll(BlockedToken.class)) {
            if (getExpirationDateFromToken(token.getToken()).getTime() < (currTime + expiration * 1000L))
                securityRepository.remove(token);
        }
    }
}