package tw.org.iii.appps.wenxlufood;
//1.firebase節點取得,頁面元件抓到
//2.Recycle View的初始化配置
//3.要抓到商品分類的id,連接foods裡的menu_id,所以回到Home的setItemClickListener,把分類id參數送到這個頁面來
//4.取得home頁面送過來的intente categoryid
//5.把adtatr灌到recyclieview
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.Model.Food;
import tw.org.iii.appps.wenxlufood.ViewHolder.FoodViewHolder;

public class FoodListActivity extends AppCompatActivity {
    private RecyclerView recycler_food;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private String categoryId=""; //傳過來的參數id
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;//取得FibaseAdapter<1,Food_model,2寫好的FoodHloder// >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //1.firebase節點取得,頁面元件抓到
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        //2.Recycle View的初始化配置
       recycler_food =  findViewById(R.id.recycler_food);
       recycler_food.setHasFixedSize(true);
       layoutManager = new LinearLayoutManager(this); //把我們現在這個頁面,裝進RecycleView
       recycler_food.setLayoutManager(layoutManager);

       //3.取得Itente過來的categoryidK
        if(getIntent() != null){ //如果有itente進來時取得intent參數
          categoryId  =  getIntent().getStringExtra("CategoryId");
          Log.v("brad","Itente有近來" + categoryId);
        }
        if (!categoryId.isEmpty() && categoryId != null){//如果拿到了分類id參數後
            loadListFood(categoryId);
            Log.v("brad","id不為空呼叫loadListFood方法,參數id為:"+categoryId+"MenuId" +foodList.orderByChild("MenuId").toString());
        }
    }

    private void loadListFood (String categoryId){
        //1.條便器四個參數帶入
        Log.v("brad","food1");
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,//1.寫好的model類別
                R.layout.food_item,//2.資料要灌入的頁面
                FoodViewHolder.class,//3.我自己定義的ViewHodler
                foodList.orderByChild("MenuId").equalTo(categoryId)//4.SELECT * FROM Foods WHERE MenuId == ?(catrgoryId)
                ) {
            //2.用populateVirewHolder,把抓到的資料對應到要灌到food_item的地方
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                Log.v("brad","food2");
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext())//取得基本的版面
                        .load(food.getImage())//讀取讀片路徑(這邊是我資料庫的圖片)
                        .into(foodViewHolder.food_image);//圖片存放在(這邊是我寫好的food_item裡)
                Log.v("brad","有近來populateView"+food.getName()+ food.getImage()+"MenuId:" +food.getMenuId());
                //3.
                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodListActivity.this,local.getName(),Toast.LENGTH_SHORT).show();
                        Log.v("brad","ItemClickListener:" + local.getName()+":" + position);
                    }
                });
            }
        };
        Log.v("brad","adapter");
        recycler_food.setAdapter(adapter);
        Log.v("brad","adapter");

    }
}
