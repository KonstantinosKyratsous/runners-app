package com.kyratsous.runnersapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/signup").permitAll() //.anonymous()
                .antMatchers("/races", "/training-plans", "/diets", "/products").permitAll()
                .antMatchers("/user", "/profile", "/user/**", "/favorites", "/notifications").authenticated()

                .antMatchers("/my-races/**").hasAnyAuthority("ADMIN", "ORGANIZER")
                .antMatchers("/my-diets/**").hasAnyAuthority("ADMIN", "NUTRITIONIST")
                .antMatchers("/my-training-plans/**").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/my-products/**").hasAnyAuthority("ADMIN", "COACH", "ATHLETE")

                .antMatchers("/races/**/add-favorite", "/races/**/remove-favorite").authenticated()
                .antMatchers("/products/**/add-favorite", "/products/**/remove-favorite").authenticated()
                .antMatchers("/diets/**/add-favorite", "/diets/**/remove-favorite").authenticated()
                .antMatchers("/training-plans/**/add-favorite", "/training-plans/**/remove-favorite").authenticated()

                // Login Configurations
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/process-login")
                .successForwardUrl("/create-cookies")
                .failureUrl("/login?error=true")

                .and()
                .httpBasic()

                // Logout Configurations
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)

                .and()
                .sessionManagement()
                .invalidSessionUrl("/login?invalid-session=true");
    }
}
