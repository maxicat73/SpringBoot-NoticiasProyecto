
package com.Egg.noticias;


import com.Egg.noticias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter{

    @Autowired
    public UsuarioServicio us;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(us).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
                .authorizeRequests()
                .antMatchers("/panelAdmin/*").hasAnyRole("ADMIN","PERIODISTA")
                .antMatchers("/css/*","/js/*","/img/*","/**")
                    .permitAll()
                    .and()
                        .formLogin()
                .loginPage("/portal/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("nombreUsuario")
                .passwordParameter("password")
                .defaultSuccessUrl("/listar")
                .permitAll()
                .and().logout()
                        .logoutUrl("/portal/logout")
                    .logoutSuccessUrl("/portal/login")
                .permitAll()
                
           .and().csrf()
                .disable();
    }
    
    
}
