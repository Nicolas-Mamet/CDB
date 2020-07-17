package com.excilys.cdb.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.excilys.cdb.security.JwtTokenUtil;
import com.excilys.cdb.security.JwtUserDetailsService;
import com.excilys.cdb.security.controller.JwtAuthenticationController;
import com.excilys.cdb.security.controller.JwtRequestFilter;

@ComponentScan(
    basePackageClasses = { JwtUserDetailsService.class, JwtTokenUtil.class,
            JwtRequestFilter.class, JwtAuthenticationController.class })
@Configuration
@EnableWebSecurity
public class SpringConfigSecurity extends WebSecurityConfigurerAdapter {

//    @Autowired
//    AuthenticationEntryPoint entryPoint;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public AuthenticationEntryPoint entryPoint() {
        BasicAuthenticationEntryPoint entryPoint =
                new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("cdb");
        return entryPoint;
    }

    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(
            HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests()
//                .antMatchers("/authenticate").permitAll().
                // all other requests need to be authenticated
                .anyRequest().authenticated().and()
                .formLogin().and()
                // make sure we use stateless session; session won't be
                // used to
                // store user's state.
//                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter,
                BasicAuthenticationFilter.class);
    }
}
