package es.rczone.simonsays.model;


/**
 * This class is the user profile that will be stored in server.
 * Actually the server will store more fields but are not relevant in local device. 
 * 
 * @author Ricardo Champa
 *
 */
public class UserProfile extends SimpleObservable<UserProfile> {
	
	private String userName;
	private String password;
	private String email;
	
	public UserProfile(String userName, String password, String email) {
		this.userName = userName;
		this.password = password;
		this.email = email;
	}
	
	public String getNombreUsuario() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	
	public void setNombreUsuario(String userName) {
		this.userName = userName;
		notifyObservers(this);
	}
	public void setPassword(String password) {
		this.password = password;
		notifyObservers(this);
	}
	public void setEmail(String email) {
		this.email = email;
		notifyObservers(this);
	}

}
