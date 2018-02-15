package Models;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;

    public User() {
        setId(0);
        setFirstname("");
        setLastname("");
        setUsername("");
        setPassword("");
    }

    public User(int id, String username, String password, String email, String firstname, String lastname) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setFirstname(firstname);
        setLastname(lastname);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
