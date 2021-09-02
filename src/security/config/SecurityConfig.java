package security.config;

import business.api.BUSINESS_API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.security_logic.jwt.JWTFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //	@Autowired JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JWTFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();//allows for spring app.security 2 running POST requests
//		http.httpBasic();//enabling baseAuthentication

//		http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();

//        http.authorizeRequests().antMatchers("/**").permitAll();

        //everybody
        http.authorizeRequests().antMatchers(
                BUSINESS_API.GUEST + "/**",
                SECURITY_API.SECURITY + SECURITY_API.SIGN_IN,
                SECURITY_API.SECURITY + SECURITY_API.SIGN_UP
        ).permitAll();

        http.authorizeRequests().antMatchers(
                BUSINESS_API.USER + "/**"
        ).hasAnyRole("USER");

        http.authorizeRequests().antMatchers(
                BUSINESS_API.LESSOR + "/**"
        ).hasAnyRole("LESSOR");

        http.authorizeRequests().antMatchers(
                BUSINESS_API.ADMIN + "/**"
        ).hasAnyRole("ADMIN");

        http.authorizeRequests().antMatchers(
                SECURITY_API.SECURITY + SECURITY_API.SIGN_OUT,
                SECURITY_API.SECURITY + SECURITY_API.REFRESH_TOKEN)
                .hasAnyRole("USER", "LESSOR", "ADMIN");

        http.authorizeRequests().anyRequest().denyAll();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}