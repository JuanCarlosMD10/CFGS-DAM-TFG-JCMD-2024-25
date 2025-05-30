//package cfgs.dam.tfg.jcmd.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//	@Autowired
//	CustomerDetailService detalleUsuarioServicio;
//
//	@Autowired
//	private AuthEntryPoint entryPoint;
//
//	@Bean
//	public AuthTokenFilter authenticationJwtTokenFilter() {
//		return new AuthTokenFilter();
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//			throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable()) // Disable CSRF
//				.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(entryPoint))
//				.sessionManagement(
//						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/hogwarts/auth/**")
//						.permitAll().anyRequest().authenticated());
//		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}
//}