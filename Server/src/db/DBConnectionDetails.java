package db;


/**
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

/**
 * The DBConnectionDetails class is an entity of the required data in order to be able to connect the database.
 */
public class DBConnectionDetails {

	private String ip;
	private String name;
	private String username;
	private String password;
	
	public DBConnectionDetails(String name, String username, String password) {
		this.name=name;
		this.username=username;
		this.password=password;
	}
	
	public DBConnectionDetails() {}
	
	public String getIp() {
		return ip;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username=username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
}
