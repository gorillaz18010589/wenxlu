package tw.org.iii.appps.wenxlufood.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.org.iii.appps.wenxlufood.Interface.ItemClickListener;
import tw.org.iii.appps.wenxlufood.R;

public class OrderViewHloder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderId, txtOrderStatus, txtOrderPhone , txtOrderAddress;
    public ItemClickListener itemClickListener;

    //1.初始化時抓取order_layout裡要玩的原件
    public OrderViewHloder(@NonNull View itemView) {
        super(itemView);
        txtOrderId= itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderAddress = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
    }

    //3.寫一個設置ItemClickListener的事件(丟入寫號的介面),當點到這個item時,把你點到的質抓到
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //2.設定點及下去的方法為,自己定義的取得id位置,跟這個view
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);//1.view,2.取得點擊的itemo位置
    }
}
