package tw.org.iii.appps.wenxlufood.Common;

import tw.org.iii.appps.wenxlufood.User;

public class Common {
    public static User currentUser;

    //設定狀態方法
    public  static  String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Placed";
        }else if(status.equals("1")){
            return "On My Way";
        }else {
            return "Shipped";
        }
    }

}

