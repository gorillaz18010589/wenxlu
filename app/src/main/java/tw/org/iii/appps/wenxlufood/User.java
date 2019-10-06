package tw.org.iii.appps.wenxlufood;

public class User {
    private  String Name;
    private  String Password;


    public User(){

    }

    public User(String name, String  password) {
        Name = name;
        Password = password;
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
