package cfgs.dam.tfg.jcmd.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

/**
 * Clase de utilidad para la generación, validación y análisis de tokens JWT.
 */
@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private int jwtExpirationMs;

	private SecretKey key;

	/**
	 * Genera un token JWT para el nombre de usuario proporcionado.
	 * 
	 * @param username nombre de usuario.
	 * @return token JWT generado.
	 */
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Valida un token JWT.
	 * 
	 * @param token token a validar.
	 * @return true si el token es válido, false en caso contrario.
	 */
	public boolean validateJwtToken(String token) {
		boolean valido = false;
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			valido = true;
		} catch (SecurityException e) {
			System.out.println("Invalid JWT signature: " + e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("JWT token is expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty: " + e.getMessage());
		}
		return valido;
	}

	/**
	 * Extrae el nombre de usuario del token JWT.
	 * 
	 * @param token token JWT.
	 * @return nombre de usuario extraído.
	 */
	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Inicializa la clave secreta para firmar y verificar los tokens JWT.
	 */
	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
}