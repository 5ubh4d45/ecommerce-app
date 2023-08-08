package dev.ixale.ecommerceservice.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.ixale.ecommerceservice.enums.Authority;
import dev.ixale.ecommerceservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {
    private final UserService userService;
    private final RsaKeyProperties rsaKeys;

    public SecurityConfig(UserService userService, RsaKeyProperties rsaKeys) {
        this.userService = userService;
        this.rsaKeys = rsaKeys;
    }

    /*
     * This will allow the /token endpoint to use basic auth and everything else uses the SFC above
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityFilterChain authSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
                .securityMatcher(new AntPathRequestMatcher("/api/v1/auth/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/signup/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/login/**")).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authenticationManager(authenticationManager)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(auth -> auth
                                // TODO: update requestMatchers when the bug is fixed https://github.com/spring-projects/spring-security/issues/13568
                                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/tokenDetails")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/**")).hasAuthority(Authority.USER.toString())
                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority(Authority.ADMIN.toString())
                                // swagger & h2-console
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().authenticated()
                )
//                .authenticationManager(authenticationManager)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                )
                .exceptionHandling(
                        (ex) -> ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        // Gets the Authentication Manager Builder Class from the HttpSecurity Object
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authManagerBuilder.authenticationProvider(authenticationProvider(passwordEncoder));
        authManagerBuilder.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("SU")
                .authorities(Authority.ADMIN.toString(), Authority.WRITE.toString(), Authority.READ.toString());
        authManagerBuilder.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("user"))
                .authorities(Authority.USER.toString(), Authority.READ.toString());

        return authManagerBuilder.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return  jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
