package com.medplus.journals.config;

import com.medplus.journals.service.CurrentUserDetailsService;
import com.medplus.journals.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/home", "/", "/css/**", "/js/**", "/images/**", "/categories").permitAll()
				.antMatchers("/rest/journals", "/rest/journals/**").authenticated()
				.antMatchers("/publisher/**").hasAuthority("PUBLISHER").anyRequest().fullyAuthenticated()
				.and().formLogin().loginPage("/login").failureUrl("/login?error").usernameParameter("username").permitAll()
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll()
				.and().exceptionHandling().accessDeniedPage("/403")
				.and().csrf().disable();
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CurrentUserDetailsService(userService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}