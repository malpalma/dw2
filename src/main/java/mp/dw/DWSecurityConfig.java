package mp.dw;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import mp.dw.db.user.UserDAOImpl;
import mp.dw.db.user.UserE;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DWSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDAOImpl userDao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder).and()
			.userDetailsService(userDetailsService())
			.and().eraseCredentials(true);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
//	enable X-Frame-Options sameorigin
			.and().headers().frameOptions().sameOrigin()
			.and().authorizeRequests()
				.antMatchers("/get*", "/save*", "/delete*").authenticated()
			.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and().logout().clearAuthentication(true).invalidateHttpSession(true);
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				try {
// superuser
					if(username.equals("super")) {
						BCryptPasswordEncoder pwdEnc = new BCryptPasswordEncoder();
						User user = new User("superuser", pwdEnc.encode("superPwd"), AuthorityUtils.createAuthorityList("USER", "EDIT", "ACCEPT", "ADMIN"));
						return user;
// defined users
					} else {
						UserE userE = userDao.getByName(username);
						if(userE.getActive()) {
							Collection<GrantedAuthority> authCollection = AuthorityUtils.createAuthorityList("USER");
							if(userE.getCanEdit()) {
								GrantedAuthority editAuth = new SimpleGrantedAuthority("EDIT");
								authCollection.add(editAuth);
							}
							if(userE.getCanAccept()) {
								GrantedAuthority acceptAuth = new SimpleGrantedAuthority("ACCEPT");
								authCollection.add(acceptAuth);
							}
							if(userE.getIsAdmin()) {
								GrantedAuthority adminAuth = new SimpleGrantedAuthority("ADMIN");
								authCollection.add(adminAuth);
							}
							User user = new User(userE.getName(), userE.getPass(), authCollection);
							return user;
						} else {
							throw new LockedException("Użytkownik nie aktywny.");
						}
					}
				} catch(Exception e) {
					throw new UsernameNotFoundException("Autentykacja nie powiodła się. " + e.getMessage());
				}
			}
		};
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

}
