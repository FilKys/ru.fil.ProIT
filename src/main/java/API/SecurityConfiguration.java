package API;

import API.DB.GetDataFromDB;
import API.Data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private GetDataFromDB db = new GetDataFromDB();

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        List<User> usersList = db.getUsers();
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}123").roles("Admin")
//                .and()
//                .withUser("user2").password("{noop}123").roles("UserOiv").roles("UserDocs");

        for (User user:usersList){
            for (String roles:user.getRoleList()){
                auth.inMemoryAuthentication()
                        .withUser(user.getLogin()).password(user.getPass()).roles(roles);
            }
        }

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
