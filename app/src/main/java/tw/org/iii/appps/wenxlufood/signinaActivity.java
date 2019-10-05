package tw.org.iii.appps.wenxlufood;
//這是登入電話號碼密碼
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

                final ProgressDialog mDialog = new ProgressDialog(signinaActivity.this);
                mDialog.setMessage("請稍等.....");
                mDialog.show();
                Log.v("brad","按按鈕有反應但沒進入到 ValueEventLtener");
                table_user.addValueEventListener(new ValueEventListener() {//這個User節點設一個值得監聽者

                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {//這個User節點質有變化時近來

                        mDialog.dismiss();
                        //如果資料本來就存在才跑這些
                        if(dataSnapshot.child(editPhone.getText().toString()).exists()){

                            //*當資料變更時取得使用者輸入的User物件存放到User
                            User user =  dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            Log.v("brad","帳好有存在:" + user);
                            if(user.getPasswrod().equals(editPassword.getText().toString())){
                                Toast.makeText(signinaActivity.this,"登入成功",Toast.LENGTH_SHORT).show();
                                Log.v("brad","近來有錯誤");
                            }else{
                                Toast.makeText(signinaActivity.this,"登入失敗",Toast.LENGTH_SHORT).show();;
                            }
                        }else{
                            mDialog.dismiss();
                            Toast.makeText(signinaActivity.this,"帳號不存在",Toast.LENGTH_SHORT).show();
                            Log.v("brad","帳號沒存在");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
