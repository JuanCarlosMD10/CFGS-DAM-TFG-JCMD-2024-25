//package cfgs.dam.tfg.jcmd.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.mysql.cj.protocol.AuthenticationProvider;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//	@Autowired
//	private CustomerDetailService customerDetailService;
//
//	@Autowired
//	private AuthEntryPoint unauthorizedHandler;
//
//	@Bean
//	public AuthTokenFilter authenticationJwtTokenFilter() {
//		return new AuthTokenFilter();
//	}
//
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(customerDetailService);
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//		return authConfig.getAuthenticationManager();
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//	    return NoOpPasswordEncoder.getInstance();  // No hace hashing, usa texto plano (no seguro)
//	}
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf().disable();
//		http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.authorizeHttpRequests().requestMatchers("/app/auth/**").permitAll().anyRequest().authenticated();
//		http.authenticationProvider(authenticationProvider());
//		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}
//}