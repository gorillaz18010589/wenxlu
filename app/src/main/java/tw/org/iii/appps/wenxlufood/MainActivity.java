package tw.org.iii.appps.wenxlufood;
//1.建立主畫面
//app=>forrder=>assets Forder創出資源資料夾
//assets/底下創一個Directory叫fonts裡面放自行
//2.建立SignIn登入頁面
//layout=> activity =>空 activity

//TcreateFromAsset(AssetManager mgr, String path):創造字形(1,"取得的assets經理人2.引入的檔案")(回傳Typeface )
//setTypeface(@Nullable Typeface tf):設定字形方法(Typeface字形物件)
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnSignUp,btnSignIn;
    private TextView txtSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn =  findViewById(R.id.btnSignIn);
        btnSignUp =  findViewById(R.id.btnSignUp);

        txtSlogan = findViewById(R.id.txtSlogan);


        //設定Slogan字形為Nabila.ttf
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Nabila.ttf");//創造字形(1,"取得的assets經理人2.引入的檔案")
        txtSlogan.setTypeface(face);//把這段文字設定字形

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,signinaActivity.class);
                startActivity(intent);
                Log.v("brad","走到第二頁登入畫面");
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,signupActivity.class);
                startActivity(intent);
                Log.v("brad","進入signup頁面");

            }
        });
    }
}
