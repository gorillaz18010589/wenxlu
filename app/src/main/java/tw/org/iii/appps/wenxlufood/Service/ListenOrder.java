package tw.org.iii.appps.wenxlufood.Service;
//1. onBind設定return null
//2.on Create init Firebase
//3.onStartCommand啟動時每次都被呼叫,新增一個節點子,由這service實作ChildEventListener監聽
//4.ChildEventListener實作方法,共有5個監聽
//5.當節點裡的兒子資料庫改變時,將新抓取的資料結點,存放到request裡(ChildEventListener監聽實作之一)
//6.秀出通知推播設定方法
//7.到Home頁面註冊Service接收推播

//通知:
//getActivity(Context context,//1.頁面
// int requestCode,//2.回應碼
// Intent[] intents,//3.要intent的物件
//int flags://4.intent的功能設定為

//取得PendingIntent物件實體(回傳PendingIntent)不會馬上執行,搭配推播使用,當點選通知可以跳activity

//setAutoCancel(boolean autoCancel)://設定點擊後關閉通知(true/false)(回傳Builder)
//setDefaults(int defaults)://設定接收通知音效(設定為依你手機的狀態為主)(回傳Builder)
//setTicker(CharSequence tickerText)://通知標題顯示在狀態欄(回傳Builder)

//getSystemService(String var1):取得系統伺服器(伺服器)(回傳Object )
//notify(int id, Notification notification)://推播通知的發送按鈕(1.用來標示為一的通知2.要發送推播的對象)_

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.firebase.ui.database.ChangeEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tw.org.iii.appps.wenxlufood.Common.Common;
import tw.org.iii.appps.wenxlufood.Model.Request;
import tw.org.iii.appps.wenxlufood.OrderStatusActivity;
import tw.org.iii.appps.wenxlufood.R;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase database;
    DatabaseReference requests;

    public ListenOrder() {
    }

    //1.Service原生方法return null
    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    //2.on Create init Firebase,啟動時只被呼叫一次
    @Override
    public void onCreate() {
        super.onCreate();

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        Log.v("brad","Service_onCreate()");
    }

    //3.onStartCommand啟動時每次都被呼叫,新增一個節點子,由這service實作ChildEventListener監聽
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);//每次開啟都有人監聽
        Log.v("brad","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    //當節點裡的兒子資料庫增加時(ChildEventListener監聽實作之一)
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    //5.當節點裡的兒子資料庫改變時,將新抓取的資料結點,存放到request裡(ChildEventListener監聽實作之一)
    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Request request = dataSnapshot.getValue(Request.class);//抓取節點的資料(用Requset的物件)將新的資料放入request中
        showNotification(dataSnapshot.getKey(),request);//顯示通知方法(1.節點的id,2.新的request資料結點)
        Log.v("brad","ListenOrder_onChildChanged");

    }
    //6.秀出通知
    private void showNotification(String key, Request request) {
        Intent intent = new Intent(getBaseContext(), OrderStatusActivity.class);//從這個Service到Order頁面
        intent.putExtra("userPhone",request.getPhone());//傳送新的使用者電話參數,給orderStatuse使用

        //點選推播跳轉到activity的PendingIntent設定
        PendingIntent contentIntent =  PendingIntent.getActivity(
                this, //1.頁面
                0, //2.回應碼(較少用到)
                 intent, //3.要intent的物件
                PendingIntent.FLAG_UPDATE_CURRENT);//4.intent的功能設定為(馬上更新前一則推播參數)

        //推播內容設定
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)//設定點擊後關閉通知(true/false)
                .setDefaults(Notification.DEFAULT_ALL)//設定接收通知音效(設定為依你手機的狀態為主)
                .setWhen(System.currentTimeMillis())//設定推播時間
                .setTicker("Hello")//通知標題顯示在狀態欄
                .setContentInfo("Your order was Update")
                .setContentText("訂單:#" + key +"已經更新狀態為:" + Common.convertCodeToStatus(request.getStatus()))//設定內容顯示下拉通知蘭
                .setContentIntent(contentIntent)//設定推播的intenet(這邊式PendingIntent,可以連接activity)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);//設定推播小圖片

        //從這個頁面.取得系統伺服器(Context.推播通知伺服器),放入 NotificationManage去做處理
        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());//推播通知的發送按鈕(1.用來標示為一的通知2.要發送推播的對象)_
        Log.v("brad","送出推播");
    }

    //當節點裡的兒子資料刪除完後(ChildEventListener監聽實作之一)
    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    //當節點裡的兒子資料庫移動後(ChildEventListener監聽實作之一)
    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    //當節點裡的兒子資料庫取消後(ChildEventListener監聽實作之一)
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
