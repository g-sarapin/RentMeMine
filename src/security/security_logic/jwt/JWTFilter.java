package security.security_logic.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import security.security_logic.entities.BlockedToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JWTTokenUtil jwtTokenUtil;
    @Autowired
    @Qualifier(value="securityMongoTemplate")
    MongoTemplate securityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String username = null;
        String jwtToken = null;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {

                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                request.setAttribute("wrongToken", e.getMessage());
            }
        }

        BlockedToken blockedToken = null;
        if (jwtToken != null) blockedToken = securityRepository.findById(jwtToken, BlockedToken.class);

        if (username != null && blockedToken == null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities()));
        }

        chain.doFilter(request, response);

//        jwtTokenUtil.deleteExpiatedTokensFromRepository();
    }
}