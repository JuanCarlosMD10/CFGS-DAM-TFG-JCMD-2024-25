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
//import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
//import cfgs.dam.tfg.jcmd.repositories.UsuarioRepository;
//
//@Service
//public class CustomerDetailService implements UserDetailsService {
//	@Autowired
//	private UsuarioRepository repoUsuario;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UsuarioModelo user = repoUsuario.findByUsuario(username);
//		if (user == null) {
//			throw new UsernameNotFoundException("User Not Found with username: " + username);
//		}
//		return new org.springframework.security.core.userdetails.User(user.getUsuario(), user.getClave(),
//				Collections.emptyList());
//	}
//}