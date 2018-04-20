package ru.akudinov.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.akudinov.test.jwt.CORSFilter;
import ru.akudinov.test.jwt.JWTAuthenticationFilter;
import ru.akudinov.test.jwt.JWTAuthorizationFilter;

import static ru.akudinov.test.jwt.SecurityConstants.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private BCryptPasswordEncoder passwordEncoder;
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String USER_ROLE = "ROLE_USER";

    public WebSecurityConfig(BCryptPasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/api/product").hasAuthority(ADMIN_ROLE)
                .antMatchers("/api/product/list/**").hasAnyAuthority(ADMIN_ROLE, USER_ROLE)
                .anyRequest().hasAnyAuthority(ADMIN_ROLE, USER_ROLE)
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        ;
    }

    @Configuration
    protected class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            String pwd = passwordEncoder.encode("password");
            auth
                    .inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder)
                    .withUser("user").password(pwd).roles("USER").and()
                    .withUser("admin").password(pwd).roles("ADMIN");
        }

    }
}