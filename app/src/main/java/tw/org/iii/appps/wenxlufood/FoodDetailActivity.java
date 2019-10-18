package tw.org.iii.appps.wenxlufood;
//目標點選商品選擇數量,結帳
//1.版面好後到FoodList把id參數傳遞過來,foodid
//2.firebase foods準備,intt設置
//3.圖片,商品價格設定


//1)contentScrim：当Toolbar收缩到一定程度时的所展现的主体颜色。即Toolbar的颜色。
//2)title:当titleEnable设置为true的时候，在toolbar展开的时候，显示大标题，toolbar收缩时，显示为toolbar上面的小标题。
//3)scrimAnimationDuration：该属性控制toolbar收缩时，颜色变化的动画持续时间。即颜色变为contentScrim所指定的颜色进行的动画所需要的时间。
//4)expandedTitleGravity：指定toolbar展开时，title所在的位置。类似的还有expandedTitleMargin、collapsedTitleGravity这些属性。
//5)collapsedTitleTextAppearance：指定toolbar收缩时，标题字体的样式，类似的还有expandedTitleTextAppearance。

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import tw.org.iii.appps.wenxlufood.Database.Database;
import tw.org.iii.appps.wenxlufood.Model.Food;
import tw.org.iii.appps.wenxlufood.Model.Order;

public class FoodDetailActivity extends AppCompatActivity {
    private TextView food_name,food_price,food_description;
    private ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton NumberButton;

    String foodId = "";//傳來的參數一開始為空

    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //1.Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //2.init View
        btnCart = findViewById(R.id.btnCart);//購物車按鈕
        NumberButton = findViewById(R.id.number_button);//購物車選數量按鈕

        //5.按下add Cart將購物車,將點選到的購物車商品新增上去
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database database = new Database(getBaseContext());
                database.addCart(new Order(
                        foodId,//商品id
                        currentFood.getName(),
                        NumberButton.getNumber(),//取得你選取的數量
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetailActivity.this,"Added to Cart",Toast.LENGTH_SHORT).show();
                Log.v("brad","成功加入到購物車:" + foodId + currentFood.getName() + NumberButton.getNumber());
            }
        });

        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_description = findViewById(R.id.food_description);
        img_food = findViewById(R.id.img_food);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //3.取得foodId從foodList intent
        if(getIntent() != null)
              foodId =  getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty()){
            getDetialFood(foodId);
        }
    }
    //4.把firebase的資料,設定到指定欄位上
    private void getDetialFood(String foodId){
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              currentFood =  dataSnapshot.getValue(Food.class);

              //設置圖片,價格,商品名,產品介紹
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(img_food);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());

                food_name.setText(currentFood.getName());

                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
