package API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired      // here is configuration related to spring boot basic authentication
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}123").roles("Admin")
                .and()
                .withUser("user2").password("{noop}123").roles("UserOiv").roles("UserDocs");// those are user name and password
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/kladr/**").access("hasRole('UserKladr') or hasRole('Admin')")
                .antMatchers("/oiv/**").access("hasRole('UserOiv') or hasRole('Admin')")
                .antMatchers("/docs/**").access("hasRole('UserDocs') or hasRole('Admin')")
                .antMatchers("/v2/api-docs",
                        "/swagger-ui.html#/**").access("hasRole('Admin')")
        ;
    }
}
