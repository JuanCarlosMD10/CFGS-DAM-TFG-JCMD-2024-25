//package cfgs.dam.tfg.jcmd.security;
//
//import java.util.Collections;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import cfgs.dam.tfg.jcmd.exceptions.UsuarioNotFoundException;
//import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
//import cfgs.dam.tfg.jcmd.services.UsuarioService;
//
//@Service
//public class CustomerDetailService implements UserDetailsService {
//	@Autowired
//	private UsuarioService usuarioService;
//
//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		try {
//			UsuarioModelo usuario = usuarioService.findByEmail(email);
//			return new org.springframework.security.core.userdetails.User(email, usuario.getClave(),
//					Collections.emptyList());
//		} catch (UsuarioNotFoundException e) {
//			throw new UsernameNotFoundException(e.getMessage());
//		}
//	}
//}