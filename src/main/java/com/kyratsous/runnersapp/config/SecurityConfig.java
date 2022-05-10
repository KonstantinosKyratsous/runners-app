package com.kyratsous.runnersapp.config;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DataSource dataSource;

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
                .antMatchers("/races", "/exercises", "/diets", "/products").permitAll()
                .antMatchers("/user", "/user/**").authenticated()

                .antMatchers("/my-races/**").hasAnyAuthority("ADMIN", "ORGANIZER")
                .antMatchers("/my-diets/**").hasAnyAuthority("ADMIN", "NUTRITIONIST")
                .antMatchers("/my-exercises/**").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/my-products/**").hasAnyAuthority("ADMIN", "COACH")
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/process-login")
                .failureUrl("/login-error")
                .and()
                .httpBasic()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
    }
}
