package tw.org.iii.appps.wenxlufood;
//版面搞定回到程式
//implementation 'com.android.support:cardview-v7:29.0.1'// cardview
// implementation 'com.android.support:recyclerview-v7:29.0.1' //recyclerview
//'com.squarep.picasso:picasso:2.5.2' //圖片讀取
// com.firebaseui:firebase-ui-database:1.2.0' //firebaseui
//1.創一個common類別,開放static方法讓大家可以玩User的屬性
//2.登入頁面耀intent過來,改過的工具列id要改
//3.設置登入名稱
//4.load menu用firebaseui將資料從Firebase灌到RecycleView,我們要先創建Category Model
//5.FirebaseRecyclerAdapter要準備4.樣東西
//   *Category.class,//1.寫好的model類別
//   *R.layout.menu_item,//2.資料要灌入的頁面
//   *int modelLayout,//3.我自己定義的ViewHodler
//   *Class<VH> viewHolderClass,//4.遠端要提取的資料庫節點(這邊是Category )
//*RecyclerView並沒有像ListView那樣提供了OnItemClick，OnItemLongClick等事件回撥介面，所以，我們需要自己寫介面去進行實現。
//*OnRecyclerViewClickListener自己創的介面
//*創造menu_item讓商品名跟圖片位置有地方放
//*MenuViewHolder去繼承 RecyclerView.ViewHolder implements View.OnClickListener,並時做方法,並在按下時觸發自己寫好的item介面


//6.用firebase_ui的FirebaseRecyclerAdapter來處理資料流的串接
//        FirebaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>:Firebase條便器(1,要玩的model資料,2.自己寫好的MenuViewHolder)
//        FirebaseRecyclerAdapter(//1.寫好的model類別
//        Class<T> modelClass,//2.資料要灌入的頁面
//        int modelLayout,//3.我自己定義的ViewHodler
//        Class<VH> viewHolderClass,//4.遠端要提取的資料庫節點(這邊是Category )



import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import tw.org.iii.appps.wenxlufood.Common.Common;
import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.Model.Category;
import tw.org.iii.appps.wenxlufood.Service.ListenOrder;
import tw.org.iii.appps.wenxlufood.ViewHolder.MenuViewHolder;

public class homeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private FirebaseDatabase database;
        private DatabaseReference category;
        private TextView txtFullName;
        private RecyclerView recycler_menu;
        private RecyclerView.LayoutManager layoutManager; //RecyclerView經理人
        private FirebaseRecyclerAdapter<Category, MenuViewHolder>  adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");//設定標題
        setSupportActionBar(toolbar);

        //1.init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");//取得資料庫分類節點

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(homeActivity.this,CartActivity.class);
                startActivity(cartIntent);
                Log.v("brad","FloatingActionButoon_fab");
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //2.Set name for user (控制案寶選單的使用者名稱)
        View  headerView= navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);//從headerView裡面找到txtFullName
        txtFullName.setText(Common.currentUser.getName()); //把標題名設置為(現在使用者,並取得她現在的名字)

        //3.Load menu
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

        //4.註冊Serive接收推播
        Intent service = new Intent(homeActivity.this, ListenOrder.class);
        startService(service);

        Log.v("brad","來到home頁面");
    }



    //4.firbase ui
    private  void loadMenu(){
        //FirebaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>:Firebase條便器(1,要玩的model資料,2.自己寫好的MenuViewHolder)
//        FirebaseRecyclerAdapter(//1.寫好的model類別
//        Class<T> modelClass,//2.資料要灌入的頁面
//        int modelLayout,//3.我自己定義的ViewHodler
//        Class<VH> viewHolderClass,//4.遠端要提取的資料庫節點(這邊是Category )
        //設定firebaseui_條便器
       adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                        Category.class,//1.寫好的model類別
                        R.layout.menu_item,//2.資料要灌入的頁面
                        MenuViewHolder.class,//3.我自己定義的ViewHodler
                        category//4.遠端要提取的資料庫節點(這邊是Category )
                ) {
                    @Override
                    protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                        menuViewHolder.txtMenuName.setText(category.getName());//寫好的介面裡面取得,標題名設置為(遠端取得的資料名)
                        Picasso.with(getBaseContext()).
                                load(category.getImage()) //讀取讀片路徑(這邊是我資料庫的圖片)
                                .into(menuViewHolder.imageView);//顯示圖片(View// )
                        final Category clickitem = category; //遠端資料庫的category的data,存入到clickitem

                        //這邊設置當點到item的觸發事件
                        menuViewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {//1.view 2.item的位置 3.
                                //5.當使用者按下所選取的分類商品,取得分類商品id參數送到Foodlist頁面做處理
                                Intent foodList = new Intent(homeActivity.this,FoodListActivity.class);
                                foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                                startActivity(foodList);
                                Toast.makeText(homeActivity.this,clickitem.getName(),Toast.LENGTH_SHORT).show();
                                Log.v("brad","position:" + position +"商品名:" + clickitem.getName());
                            }
                        });
                    }
                };
                recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Log.v("brad","onBackPressed_closeDrawer");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        Log.v("brad","onCreateOptionsMenu:" + menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.v("brad","id_settinggs=id" + item) ;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //當工具列被選取時
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(homeActivity.this,CartActivity.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(homeActivity.this,OrderStatusActivity.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_log_out) {
            Intent sigIn = new Intent(homeActivity.this,signinaActivity.class);
            sigIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//關掉上一個activity,登出
            startActivity(sigIn);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
