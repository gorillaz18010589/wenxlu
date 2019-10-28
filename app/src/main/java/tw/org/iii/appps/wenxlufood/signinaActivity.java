package tw.org.iii.appps.wenxlufood;
//這是登入電話號碼密碼
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tw.org.iii.appps.wenxlufood.Common.Common;

public class signinaActivity extends AppCompatActivity {
    private EditText editPhone,editPassword;
    private Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signina);
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.signin_sign);

        //1.加入database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        //2.按下登入按鈕後確認datbase的帳號密碼是否正確
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addValueEventListener(new ValueEventListener() {//這個User節點設一個值得監聽者
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {//這個User節點質有變化時近來

                        //如果這個user節點裡的你輸入的兒子電話帳號有存在的話
                        if(dataSnapshot.child(editPhone.getText().toString()).exists()){
                            //*當資料變更時取得使用者輸入的User物件存放到User
                            User user =  dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);//取得使用者輸入的電話,取得這個User資料
                            Log.v("brad","帳好有存在:" + user.getPassword());
                            user.setPhone(editPhone.getText().toString()); //設置電話

                            //如果這個使用者資料庫的密碼,跟輸入的一樣的話,進入menu頁面
                            if(user.getPassword().equals(editPassword.getText().toString())){
                                Intent homeIntent = new Intent(signinaActivity.this,homeActivity.class);
                                Common.currentUser = user; //把你輸入的使用者,存到currentUser
                                startActivity(homeIntent);
                                Log.v("brad","登入成功使用者輸入密碼是:" + editPassword.getText().toString()+"帳號是:" +user.getName());
                                finish();
                            }else{
                                Toast.makeText(signinaActivity.this,"密碼錯誤",Toast.LENGTH_SHORT).show();
                                Log.v("brad","登入失敗" + user.getPassword()+user.getName());
                            }

                        }else{

                            Toast.makeText(signinaActivity.this,"帳號不存在",Toast.LENGTH_SHORT).show();
                            Log.v("brad","帳號不存在");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        Log.v("brad","signin_onCreate");
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.v("brad","signin_onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("brad","signin_onResume()");    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("brad","signin_onRestart()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("brad","signin_onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("brad","signin_onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("brad","signin_onDestroy()");

    }
}
