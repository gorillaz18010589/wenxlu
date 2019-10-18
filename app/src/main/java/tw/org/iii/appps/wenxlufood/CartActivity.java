package tw.org.iii.appps.wenxlufood;
//1.繼承SQLiteAssetHelper,實作方法準備資料庫,版本
//2.寫Order_Model
//3.寫好新增購物車,刪除購物車,查詢購物車後
//4.到FoodDetail頁面去設定按鈕點擊事件addCart
//5.配置Cart版面 layout=>activity :cart
//6.firebae跟init準備
//7.準備一個CartAdapter去玩sqlite
//8回來設置條便器,跟資料庫
//9.去home頁面設置fab購物車,點下去itent到我們寫好的訂單頁面
//10.寫
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import tw.org.iii.appps.wenxlufood.Common.Common;
import tw.org.iii.appps.wenxlufood.Database.Database;
import tw.org.iii.appps.wenxlufood.Model.Order;
import tw.org.iii.appps.wenxlufood.Model.Request;
import tw.org.iii.appps.wenxlufood.ViewHolder.CartAdapter;

public class CartActivity extends AppCompatActivity {
     RecyclerView recyclerView;
     RecyclerView.LayoutManager layoutManager;

     FirebaseDatabase database;
     DatabaseReference requests;

     TextView txtTotalPrice;
     FButton btnPlaceOrder;

     List<Order> cart = new ArrayList<>();
     CartAdapter adapter;

     int total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //1.firebase節點準備
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //2.init準備
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        //3.按下按鈕出現視窗,輸入地址,送出將資料傳遞到firebase
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        loadListFood();

    }
    //4.
    private void showAlertDialog(){
        //Builder(@NonNull Context context):
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("One more set");//設置標題
        alertDialog.setMessage("Enter your address");//設置訊息

        //setLayoutParams(LayoutParams params)
        //LayoutParams(int width, int height)
        //setView(View view):(回傳Builder)
        //setPositiveButton(CharSequence text, final OnClickListener listener):(回傳Builder)
        final EditText edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, //設定寬(全寬)
                LinearLayout.LayoutParams.MATCH_PARENT //設定高(泉高)
        );
        edtAddress.setLayoutParams(lp);//執行LayoutParams
        alertDialog.setView(edtAddress);//設置畫面
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);//設置icon圖片

        //當使用者輸入好按下yse,將得到的資訊,更新上去存到 Request
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(
                        Common.currentUser.getPhone(),//1.現在的使用者電話
                        Common.currentUser.getName(),//2.現在的使用者姓名
                        edtAddress.getText().toString(),//3.使用者輸入的地址
                        txtTotalPrice.getText().toString(),//4.價格總和
                        cart//5.
                );
                //送出後到Firebase儲存,使用System.CurrentMilli to key
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);//把這個訂單資訊上傳到firebase
                Log.v("brad","dialoginterface:" + dialogInterface +",i:" + i);

                //清除購物車
                new Database(getBaseContext()).clearCart();
                Toast.makeText(CartActivity.this,"Thank you, Order Place",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //如果使用者按下no的話
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();//執行顯示出來Dialog
    }

    private void loadListFood(){
        //1.設定資料庫,跟條便器
        cart =  new Database(this).getCarts(); //從資料庫類別哩,取得購物車資訊
        adapter = new CartAdapter(cart,this); //CartAdapter設定(1.我的資料庫結構,2.this content)
        recyclerView.setAdapter(adapter);

        //2.計算價格總和
        for(Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en","US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
            Log.v("brad"," loadListFood()有近來");
    }
}
