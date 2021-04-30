package com.jeff.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jeff.security.UsuarioUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String[] PUBLIC_MATCHERS = {"/"};
	private static final String[] PUBLIC_MATCHERS_POST = {"/funcionarios/**"};
	
	
	private static final String USUARIO_POR_LOGIN = "SELECT login, senha, ativo FROM ajv.usuario"
            + " WHERE login = ?";
   
	private static final String PERMISSOES_POR_USUARIO = "SELECT u.login, p.nome FROM ajv.usuario_permissoes up"
            + " JOIN ajv.usuario u ON u.id = up.usuarios_id"
            + " JOIN ajv.permissao p ON p.id = up.permissoes_id"
            + " WHERE u.login = ?";
   
	private static final String PERMISSOES_POR_GRUPO = "SELECT g.id, g.nome, p.nome FROM ajv.grupo_permissoes gp"
            + " JOIN ajv.grupo g ON g.id = gp.grupos_id"
            + " JOIN ajv.permissao p ON p.id = gp.permissoes_id"
            + " JOIN ajv.usuario_grupos ug ON ug.grupos_id = g.id"
            + " JOIN ajv.usuario u ON u.id = ug.usuarios_id"
            + " WHERE u.login = ?";
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UsuarioUserDetailsService usuarioUserDetailsService;
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(PUBLIC_MATCHERS).permitAll()
		.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
		.anyRequest().authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		//http.cors().disable();
	}*/
	//algaworks
	 @Override
	  protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests().antMatchers("/webjars/**","/imgs/**","/css/**").permitAll();
		 http
	        .authorizeRequests()
	        	.antMatchers("/obras/**","/funcionarios/**").not().hasRole("VISITANTE")
	            // Para qualquer requisição (anyRequest) é preciso estar 
	            // autenticado (authenticated).
	            .anyRequest().authenticated()
	        .and().csrf().disable().cors()
	        .and()
	        .formLogin()
	        	.loginPage("/login")
	        	.defaultSuccessUrl("/", true).permitAll()
	        	.and()
	        	.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
	        	.and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86000);
	    
	    //httpbasic : tela de login simples do browser
	    //formLogin : tela customizada do springsecurity
	    System.out.println("configure httpbasic");
	  }
	
	//usuario em memoria apenas para o desenvolvimento
	 /*
	 @Override
	  public void configure(AuthenticationManagerBuilder builder) throws Exception {
	    //noop para n utilizar o password encoder, apenas ambiente de desenvolvimento
		 builder
	        .inMemoryAuthentication()
	        .withUser("jef").password("{noop}123").roles("ADMIN")
	        .and()
	        .withUser("user").password("{noop}123").roles("USER");
	    System.out.println("COFIGURe");
	  }
	 */
	 /* autenticacao jdbc banco de dados
	 @Override
	  protected void configure(AuthenticationManagerBuilder builder) throws Exception {
	    builder
	        .jdbcAuthentication()
	        .dataSource(dataSource)
	        .passwordEncoder(new BCryptPasswordEncoder())
	        .usersByUsernameQuery(USUARIO_POR_LOGIN)
	        .authoritiesByUsernameQuery(PERMISSOES_POR_USUARIO)
	        .rolePrefix("ROLE_");
	 System.out.println("Configuree jbdc"); 
	 }
	*/
	 @Override
	  protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		 System.out.println("configure JPA userdetails");
	    builder
	        .userDetailsService(usuarioUserDetailsService)
	        .passwordEncoder(new BCryptPasswordEncoder());
	  }
	 
	 
	 
	//@Override
	//protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//	auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	//}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	 
	 
}
