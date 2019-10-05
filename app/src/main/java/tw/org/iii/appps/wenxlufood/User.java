package tw.org.iii.appps.wenxlufood;

public class User {
    private  String Name;
    private  String Passwrod;


    public User(){

    }

    public User(String name, String passwrod) {
        Name = name;
        Passwrod = passwrod;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPasswrod(String passwrod) {
        Passwrod = passwrod;
    }

    public String getName() {
        return Name;
    }

    public String getPasswrod() {
        return Passwrod;
    }
}
