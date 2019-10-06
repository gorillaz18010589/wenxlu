package tw.org.iii.appps.wenxlufood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class signupActivity extends AppCompatActivity {
    private EditText editPhone,editName,editPassword;
    private Button signup_signup;
    private DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editPhone = findViewById(R.id.editPhone);
        editName = findViewById(R.id.editName);
        editPassword = findViewById(R.id.editPassword);

        signup_signup = findViewById(R.id.signup_signup);

       FirebaseDatabase database = FirebaseDatabase.getInstance();
       table_user = database.getReference("User");

        signup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Signup();
            }
        });
    }
    //註冊帳號方法
    private void Signup (){
        Log.v("brad","按下按鈕有近來");
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //檢查是否已經有電話帳號
                if(dataSnapshot.child(editPhone.getText().toString()).exists()){//如果這個data變化的兒子裡面包含使用者輸入的資料的話
                    Toast.makeText(signupActivity.this,"電話帳號已經有人使用",Toast.LENGTH_SHORT).show();
                    Log.v("brad","電話帳號已經有人使用" + dataSnapshot);
                }else{
                    User user = new User(editName.getText().toString(),editPassword.getText().toString());
                    table_user.child(editPhone.getText().toString()).setValue(user);//在你輸入的這個User的兒子電話id,在設置新寫好的user上去
                    Toast.makeText(signupActivity.this,"新帳號以註冊成功",Toast.LENGTH_SHORT).show();
                    Log.v("brad","新的帳號以註冊成功:" + user.getName()+user.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
