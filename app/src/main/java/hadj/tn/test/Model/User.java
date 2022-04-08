package hadj.tn.test.Model;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {

    private int id;
    private  String username;
    private  String email;
    private  String phone;
    private String password;
    private String appUserRole;
    private String region;
    private Boolean enabled=false;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public User(String email) {
        this.email = email;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public User() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppUserRole() {
        return appUserRole;
    }
    public void setAppUserRole(String appUserRole) {
        this.appUserRole = appUserRole;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
