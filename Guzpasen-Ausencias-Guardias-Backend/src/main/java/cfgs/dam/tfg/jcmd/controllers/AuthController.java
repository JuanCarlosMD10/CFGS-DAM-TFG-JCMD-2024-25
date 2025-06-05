//package cfgs.dam.tfg.jcmd.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import cfgs.dam.tfg.jcmd.dto.UsuarioDTO;
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
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getClave()));
//
//		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		return jwtUtils.generateToken(userDetails.getUsername());
//	}
//
//	@PostMapping("/nuevousuario")
//	public String registerUser(@RequestBody UsuarioModelo user) {
//		String generatedUsername = generarNombreUsuario(user.getNombre(), user.getApellidos());
//
//		if (usuarioService.existsByGeneratedUsername(generatedUsername)) {
//			return "Error: Usuario ya registrado con ese nombre/apellidos.";
//		}
//
//		UsuarioDTO dto = new UsuarioDTO();
//		dto.setNombre(user.getNombre());
//		dto.setApellidos(user.getApellidos());
//		dto.setEmail(user.getEmail());
//		dto.setRol(user.getRol());
//		dto.setUsuarioMovil(user.getUsuarioMovil());
//		dto.setClave(encoder.encode(user.getClave()));
//
//		usuarioService.createUsuario(dto);
//		return "Usuario registrado correctamente. Usa: " + generatedUsername + " para iniciar sesión.";
//	}
//
//	private String generarNombreUsuario(String nombre, String apellidos) {
//		if (nombre == null || apellidos == null)
//			return null;
//		StringBuilder sb = new StringBuilder();
//		sb.append(Character.toLowerCase(nombre.charAt(0)));
//		String[] partes = apellidos.trim().split("\\s+");
//		for (String parte : partes) {
//			sb.append(parte.substring(0, Math.min(3, parte.length())).toLowerCase());
//		}
//		return sb.toString();
//	}
//}