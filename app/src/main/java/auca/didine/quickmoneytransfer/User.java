package auca.didine.quickmoneytransfer;

public class User {
    private String names;

    private String telephone;

    private String nid;

    private String email;

    private String password;

    private String country;


    public User() {
    }


    public User(String names, String telephone, String nid, String email, String password, String country) {
        this.names = names;
        this.telephone = telephone;
        this.nid = nid;
        this.email = email;
        this.password = password;
        this.country = country;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
