package mp.dw;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import mp.dw.db.user.UserE;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DWUserSession implements Serializable {

	private String name;
	
	private boolean authenticated = false;
	
	private boolean canEdit = false;
	
	private boolean canAccept = false;
	
	private boolean isAdmin = false;
	
	private UserE user;


	
	public DWUserSession() {}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAuthenticated(boolean auth) {
		this.authenticated = auth;
	}
	
	public boolean getAuthenticated() {
		return this.authenticated;
	}
	
	public void setCanEdit(boolean can) {
		this.canEdit = can;
	}
	
	public boolean getCanEdit( ) {
		return this.canEdit;
	}

	public boolean isCanAccept() {
		return canAccept;
	}

	public void setCanAccept(boolean canAccept) {
		this.canAccept = canAccept;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public UserE getUser() {
		return this.user;
	}
	
	public void setUser(UserE user) {
		this.user = user;
	}
	
	
	public void updateOnAuthentication() {
		if(!this.authenticated) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null) {
				if(auth.isAuthenticated()) {
					this.setAuthenticated(true);
					Object principal = auth.getPrincipal();
					String username;
					if(principal instanceof UserDetails) {
						username = ((UserDetails) principal).getUsername();
						if(principal instanceof UserE) {
							this.user = (UserE) principal;
							this.canEdit = this.user.getCanEdit();
							this.canAccept = this.user.getCanAccept();
							this.isAdmin = this.user.getIsAdmin();
						} else {
							Collection<GrantedAuthority> authCollection = (Collection<GrantedAuthority>) ((UserDetails) principal).getAuthorities();
							for(GrantedAuthority gAuth : authCollection) {
								if(gAuth.toString().equals("EDIT")) {
									this.canEdit = true;
								} else if(gAuth.toString().equals("ACCEPT")) {
									this.canAccept = true;
								} else if(gAuth.toString().equals("ADMIN")) {
									this.isAdmin = true;
								}
							}
						}
					} else {
						username = principal.toString();
					}
					this.name = username;
				}
			}
		}
	}
	
	public String toJson() {
		return "{\n" +
				"\"name\":\"" + this.name + "\",\n" +
				"\"canEdit\":\"" + this.canEdit +"\",\n"+
				"\"canAccept\":\"" + this.canAccept +"\",\n"+
				"\"isAdmin\":\"" + this.isAdmin +"\"\n"+
				"}";
	}
}
