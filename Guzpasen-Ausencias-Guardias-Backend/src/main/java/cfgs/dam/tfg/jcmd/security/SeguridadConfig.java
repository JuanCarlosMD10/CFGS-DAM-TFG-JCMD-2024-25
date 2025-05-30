//package cfgs.dam.tfg.jcmd.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SeguridadConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll()
//            )
//            .formLogin(form -> form.disable())
//            .httpBasic(httpBasic -> httpBasic.disable())
//            .oauth2ResourceServer(oauth2 -> oauth2.disable());
//
//        System.out.println("✅ Seguridad completamente deshabilitada");
//        return http.build();
//    }
//}