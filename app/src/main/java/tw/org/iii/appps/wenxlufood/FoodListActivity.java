package tw.org.iii.appps.wenxlufood;
//1.firebase節點取得,頁面元件抓到
//2.Recycle View的初始化配置
//3.要抓到商品分類的id,連接foods裡的menu_id,所以回到Home的setItemClickListener,把分類id參數送到這個頁面來
//4.取得home頁面送過來的intente categoryid
//5.把adtatr灌到recyclieview
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.Model.Food;
import tw.org.iii.appps.wenxlufood.ViewHolder.FoodViewHolder;
import androidx.appcompat.widget.SearchView;

public class FoodListActivity extends AppCompatActivity {
    private RecyclerView recycler_food;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private String categoryId=""; //傳過來的參數id
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;//取得FibaseAdapter<1,Food_model,2寫好的FoodHloder// >

    //搜尋adapter宣告
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>(); //提示陣列
    SearchView searchView;
    String z ="";
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
        //5.Search
        searchView = findViewById(R.id.searchBar);
        searchView.setQueryHint("Enter your food");//設置提示訊息

        loadSuggest(categoryId);


        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    startSearch(s);
                    Log.v("brad","onQueryTextSubmit:" +s);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(searchView != null){
                        search(s);
                        Log.v("brad","onQueryTextChange:" +s);
                    }
                    if(searchView == null){
                        loadListFood(categoryId);
                        Log.v("brad","no search text");
                    }
                    return true;
                }
            });
        }

    }



    //8.search條件
    private void search(String s) {

        List<String> suggest = new ArrayList<>();
        for(String search : suggestList){
           if(search.toLowerCase().contains(s.toLowerCase())){
               suggest.add(search);
               Log.v("brad","s:" + s  +",search.toLowerCaser" + search.toLowerCase()+",s:"+ s.toLowerCase()+"syggest" +suggest) ;
           }
           for( final String z : suggest){
               z.toString();
               searchView.setQueryHint(z);
               Log.v("brad","Z:" +z);
               Log.v("brad","startSearch, seachText:" + z);
               searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                       Food.class,
                       R.layout.food_item,
                       FoodViewHolder.class,
                       foodList.orderByChild("Name").equalTo(z)
               ) {
                   @Override
                   protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                       Log.v("brad","z:" + z);
                       foodViewHolder.food_name.setText(food.getName());
                       Picasso.with(getBaseContext())//取得基本的版面
                               .load(food.getImage())//讀取讀片路徑(這邊是我資料庫的圖片)
                               .into(foodViewHolder.food_image);//圖片存放在(這邊是我寫好的food_item裡)
                       Log.v("brad","有近來populateView"+food.getName()+ food.getImage()+"MenuId:" +food.getMenuId());

                       final Food local = food;
                       foodViewHolder.setItemClickListener(new ItemClickListener() {
                           @Override
                           public void onClick(View view, int position, boolean isLongClick) {
                               Intent foodDteail = new Intent(FoodListActivity.this,FoodDetailActivity.class);
                               foodDteail.putExtra("FoodId",searchAdapter.getRef(position).getKey());
                               startActivity(foodDteail);
                               Toast.makeText(FoodListActivity.this,local.getName(),Toast.LENGTH_SHORT).show();
                               Log.v("brad","ItemClickListener:" + local.getName()+":" + position);
                           }
                       });
                   }
               };
               recycler_food.setAdapter(searchAdapter);//設定RecycleView搜尋結果呈現
           }

        }

    }


    //7.開始搜尋呈現的Adapter
    private void startSearch(String searchText){
        Log.v("brad","startSearch, seachText:" + searchText);
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(searchText.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext())//取得基本的版面
                        .load(food.getImage())//讀取讀片路徑(這邊是我資料庫的圖片)
                        .into(foodViewHolder.food_image);//圖片存放在(這邊是我寫好的food_item裡)
                Log.v("brad","有近來populateView"+food.getName()+ food.getImage()+"MenuId:" +food.getMenuId());

                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDteail = new Intent(FoodListActivity.this,FoodDetailActivity.class);
                        foodDteail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDteail);
                        Toast.makeText(FoodListActivity.this,local.getName(),Toast.LENGTH_SHORT).show();
                        Log.v("brad","ItemClickListener:" + local.getName()+":" + position);
                    }
                });
            }
        };
        recycler_food.setAdapter(searchAdapter);//設定RecycleView搜尋結果呈現
    }

    //6.讀取提示方法
    private List<String> loadSuggest(String categoryId) {
        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot :dataSnapshot.getChildren()) //尋訪MenuId == 分類id
                {
                 Food item =  postSnapshot.getValue(Food.class); //取得id的類別用Food類別,存起來較item
                 suggestList.add(item.getName());//item裡面取得他的商品名稱,掛上去陣列suggestList
                 Log.v("brad","item:name:" + item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return suggestList;
    }

    //4.讀取食物
    private void loadListFood (String categoryId){
        //1.條便器四個參數帶入
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,//1.寫好的model類別
                R.layout.food_item,//2.資料要灌入的頁面
                FoodViewHolder.class,//3.我自己定義的ViewHodler
                foodList.orderByChild("MenuId").equalTo(categoryId)//4.SELECT * FROM Foods WHERE MenuId == ?(catrgoryId)
                ) {
            //2.用populateVirewHolder,把抓到的資料對應到要灌到food_item的地方
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
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
                        Intent foodDteail = new Intent(FoodListActivity.this,FoodDetailActivity.class);
                        foodDteail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDteail);
                        Toast.makeText(FoodListActivity.this,local.getName(),Toast.LENGTH_SHORT).show();
                        Log.v("brad","ItemClickListener:" + local.getName()+":" + position);
                    }
                });
            }
        };
        recycler_food.setAdapter(adapter);
        Log.v("brad","adapter");

    }


}
