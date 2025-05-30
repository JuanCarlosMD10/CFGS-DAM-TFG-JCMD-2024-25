//package cfgs.dam.tfg.jcmd.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
//import cfgs.dam.tfg.jcmd.services.UsuarioService;
//import cfgs.dam.tfg.jcmd.utils.JwtUtil;
//
//@RestController
//@RequestMapping("/app/auth")
//public class AuthController {
//
//	@Autowired
//	AuthenticationManager authenticationManager;
//
//	@Autowired
//	UsuarioService usuarioService;
//
//	@Autowired
//	PasswordEncoder encoder;
//
//	@Autowired
//	JwtUtil jwtUtils;
//
//	@PostMapping("/login")
//	public String login(@RequestBody UsuarioModelo usuario) {
//		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario.getUsuario(),
//				usuario.getClave());
//		Authentication authentication = authenticationManager.authenticate(token);
//		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		return jwtUtils.generateToken(userDetails.getUsername());
//	}
//
//	@PostMapping("/nuevousuario")
//	public String registerUser(@RequestBody UsuarioModelo user) {
//		System.out.println("Entro en alta");
//		if (usuarioService.existsByUsuario(user.getUsuario())) {
//			return "Error: Username is already taken!";
//		}
//// Create new user's account
//		UsuarioModelo newUser = new UsuarioModelo(null, user.getUsuario(), encoder.encode(user.getClave()));
//		usuarioService.saveUsuario(newUser);
//		return "User registered successfully!";
//	}
//}