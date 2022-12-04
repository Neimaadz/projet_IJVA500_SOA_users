package com.cedalanavi.projet_IJVA500_SOA_users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cedalanavi.projet_IJVA500_SOA_users.Utils.JwtAuthEntryPoint;
import com.cedalanavi.projet_IJVA500_SOA_users.Utils.JwtRequestFilter;
  
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)	// enable annotation e.g: @preAuthorize()...
public class SecurityConfig {
	
	@Autowired
	private JwtAuthEntryPoint jwtAuthEntryPoint;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.csrf().disable()
			// make sure we use stateless session; session won't be used to
			// store user's state.
		  	.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
		  	.and()
		  	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		  	.and()
			// dont authenticate this particular request
			.authorizeRequests().antMatchers("/authentication-user/**").permitAll()
			.and()
			.authorizeRequests().antMatchers("/manage-user/create").permitAll()
			// all other requests need to be authenticated
			.anyRequest().authenticated();
    	
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*");
            }
        };
    }
}