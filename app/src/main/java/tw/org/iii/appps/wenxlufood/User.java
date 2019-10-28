package tw.org.iii.appps.wenxlufood;

public class User {
    private  String Name;
    private  String Password;
    private  String Phone;
    private  String isStaff;


    public User(){

    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        isStaff ="false";//當消費者使用者輸入時,員工狀態預設為false
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
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
