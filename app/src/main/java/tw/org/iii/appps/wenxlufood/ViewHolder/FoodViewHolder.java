package tw.org.iii.appps.wenxlufood.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView food_image;//food_item的圖片
    public TextView food_name;//food_item的食物名

    private ItemClickListener itemClickListener;//自己寫好的按鈕介面

    //1.初始化時當itemView被物件實體化時,itemView近來,將我們需要的原件抓到
    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        food_name = itemView.findViewById(R.id.food_name);
        food_image = itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
    }

    //2.寫一個設置ItemClickListener的事件(丟入寫號的介面),當點到這個item時,把你點到的質抓到
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    //3.這是一個監聽者,點下去時呼叫寫好的itemClickListener介面
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
