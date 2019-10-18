package tw.org.iii.appps.wenxlufood;
//目的:訂單送出後,出現在訂單上
// 1.Request需要增加status,好知道訂單狀況
// 2.創建OrderViewHloder
// 3.創建Activity:OrderStatus
// 4.XML配置,創建Order_layout
//5.OrderViewHloder配置
//6.去Home頁面將指定頁面Itente到該去地方
//7.到firebase規則加上index=>phone


// FirebaseRecyclerAdapter(
// 1.Class<T> modelClass,//Request.class,//1.寫好的model類別
// 2.int modelLayout,// R.layout.order_layout,//2.資料要灌入的頁面
// 3.Class<VH> viewHolderClass,R.layout.order_layout,//2.資料要灌入的頁面
// 4.Query ref// Requests.orderByChild("phone").equalTo(phone)//4.SELECT * FROM Requests WHERE phone == ?(Common.currentUser.getPhone()
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tw.org.iii.appps.wenxlufood.Common.Common;
import tw.org.iii.appps.wenxlufood.Model.Request;
import tw.org.iii.appps.wenxlufood.ViewHolder.OrderViewHloder;

public class OrderStatusActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHloder> adapter;


    FirebaseDatabase database;
    DatabaseReference Requests;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //1.firebase設定
      database =  FirebaseDatabase.getInstance();
      Requests =  database.getReference("Requests");

        //2.init設定
        recyclerView = findViewById(R.id.listOrders);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());//輸入使用者的電話,讀取訂單狀態
    }

//3.FirebaseRecyclerAdapter<Request,OrderViewHloder>,配置訂單的id,訂單狀態,手機號碼.地址
    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHloder>(
                Request.class,//1.寫好的model類別
                R.layout.order_layout,//2.資料要灌入的頁面
                OrderViewHloder.class,//3.我自己定義的ViewHodler
                Requests.orderByChild("phone").equalTo(phone)//4.SELECT * FROM Requests WHERE phone == ?(Common.currentUser.getPhone())
        ) {@Override
            protected void populateViewHolder(OrderViewHloder orderViewHloder, Request request, int i) {
                orderViewHloder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderViewHloder.txtOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                orderViewHloder.txtOrderPhone.setText(request.getPhone());
                orderViewHloder.txtOrderAddress.setText(request.getAddress());
            }
        };
        recyclerView.setAdapter(adapter);
    }
    //4.當狀態是多少時,回復什麼話
    private String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Placed";
        }else if(status.equals("1")){
            return "On My Way";
        }else {
            return "Shipped";
        }
    }
}
