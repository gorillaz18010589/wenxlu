package tw.org.iii.appps.wenxlufood.ViewHolder;
//寫一個MenuViewHolder類別繼承RecyclerView.ViewHolder ,並且要實作View.OnClickListener方法,要建構是跟實作指定方法
//因為FirebaseApadter需要ViewHolder
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtMenuName; //自己定義的要得標提名
    public ImageView imageView; //自己定義的圖片名

    private ItemClickListener itemClickListener; //自己寫好的介面

    //1.當取得itemView時,要先創一個item view(Card View)讓並抓到這個id
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName =  itemView.findViewById(R.id.menu_name);
        imageView= itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    //2.創建一個ItemClickListener的事件(丟入寫號的介面)
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //1.要實作的方法之一,當下按鈕呼叫自己寫好的介面建構式
    @Override
    public void onClick(View view) {
        //3.呼叫自己定義的介面
        itemClickListener.onClick(view,getAdapterPosition(),false);//1.view,2.取得點擊的itemo位置
    }
}
