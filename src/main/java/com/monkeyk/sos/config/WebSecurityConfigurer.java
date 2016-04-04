package com.monkeyk.sos.config;

import com.monkeyk.sos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

/**
 * 2016/4/3
 * <p/>
 * Replace security.xml
 *
 * @author Shengzhao Li
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserService userService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.expressionHandler(new OAuth2WebSecurityExpressionHandler());
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/oauth/**").hasAnyRole("ROLE_USER,ROLE_UNITY,ROLE_MOBILE")
                .antMatchers("/**").anonymous()
                .and()
                .exceptionHandling().accessDeniedPage("/login.jsp?authorization_error=2")
                .and()
                .csrf().disable()
                .formLogin().loginPage("/login.jsp")
                .failureUrl("/login.jsp?authentication_error=1")
                .defaultSuccessUrl("/index.jsp")
                .loginProcessingUrl("/login.do")
                .and()
                .logout().logoutUrl("/logout.do")
                .logoutSuccessUrl("/index.jsp")
                .and()
                .anonymous();


    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(new Md5PasswordEncoder());
    }

}
