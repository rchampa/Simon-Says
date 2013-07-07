package es.rczone.simonsays.model;

/**
 * This class is the user profile that will be stored in server.
 * Actually the server will store more fields but are not relevant in local device. 
 * 
 * @author Ricardo Champa
 *
 */
public class UserProfile {
	
	private String userName;
	private String password;
	
	
	public UserProfile(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public String getNombreUsuario() {
		return userName;
	}
	public void setNombreUsuario(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
