package tw.org.iii.appps.wenxlufood;

public class User {
    private  String Name;
    private  String Password;
    private  String Phone;


    public User(){

    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPasswrod(String passwrod) {
        Password = Password;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }
}
