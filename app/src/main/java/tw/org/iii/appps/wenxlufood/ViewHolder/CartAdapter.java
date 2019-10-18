package tw.org.iii.appps.wenxlufood.ViewHolder;
//1.新增一個class:CartViewHolder
//2.新增一個layout :Cartlayout
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.Model.Order;
import tw.org.iii.appps.wenxlufood.R;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txt_cart_name, txt_cart_price; //商品名稱,價格
    public ImageView img_cart_count;//商品折扣

    private ItemClickListener itemClickListener; //自己寫的介面

    //3.自己設置一個可以修改的seter
    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }
    //2.建構式取得畫面元件(cart_layout.xml)
    public CartViewHolder (@NonNull View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);
    }
    //2.實作方法
    @Override
    public void onClick(View view) {

    }
}
//**
//1.繼承RecyclerView.Adapter<CartViewHolder>
//2.實做三個方法
//3.
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    //3.設定 listData資料結構,跟Context context,並且用出建構式
    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate(int resource, @Nullable ViewGroup root, boolean attachToRoot):(回傳View)
        //4.
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    //5.設定CartViewHolder,裡的txt_cart_price,txt_cart_name,img_cart_count
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        //TextDrawable buildRound(String text, int color)
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);


        //Locale(@RecentlyNonNull String language):
        //getCurrencyInstance(@RecentlyNonNull Locale inLocale):(回傳NumberFormat)
        //String format(long number):
        Locale locale = new Locale("en","US");
        NumberFormat  fmt = NumberFormat.getCurrencyInstance(locale);
        int price =(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_cart_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
