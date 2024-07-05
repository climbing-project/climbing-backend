package com.climbing.auth.config;

import com.climbing.auth.jwt.JwtAuthenticationFilter;
import com.climbing.auth.jwt.JwtService;
import com.climbing.auth.login.JsonAuthenticationFilter;
import com.climbing.auth.login.LoginService;
import com.climbing.auth.login.handler.LoginFailureHandler;
import com.climbing.auth.login.handler.LoginSuccessHandler;
import com.climbing.auth.oauth2.CustomOAuth2MemberService;
import com.climbing.auth.oauth2.handler.OAuth2LoginFailureHandler;
import com.climbing.auth.oauth2.handler.OAuth2LoginSuccessHandler;
import com.climbing.domain.member.repository.MemberRepository;
import com.climbing.redis.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2MemberService customOAuth2MemberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(Collections.singletonList("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(false);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));
                    config.setMaxAge(3600L); //1시간
                    return config;
                }))
                .headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/ws/**", "/h2-console/**", "/members/**", "/gyms/**", "/home", "/chat/**", "/tags/**", "/admin/**").permitAll()
                                .anyRequest().authenticated())
                .logout((logout) ->
                        logout
                                .deleteCookies("JSESSIONID"))
                .oauth2Login((oauth2Login) ->
                        oauth2Login
                                .loginPage("/login")
                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2MemberService))
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                )
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.accessDeniedPage("/members/error")
                );
        http.addFilterAfter(jwtAuthenticationFilter(), LogoutFilter.class);
        http.addFilterAfter(jsonAuthenticationFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, memberRepository, redisService);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() {
        JsonAuthenticationFilter jsonLoginFilter = new JsonAuthenticationFilter(objectMapper);
        jsonLoginFilter.setAuthenticationManager(authenticationManager());
        jsonLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        jsonLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonLoginFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, memberRepository, redisService);
    }
}
