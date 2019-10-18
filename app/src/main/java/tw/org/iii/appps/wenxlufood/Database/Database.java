package tw.org.iii.appps.wenxlufood.Database;
//1.繼承SQLiteAssetHelper,實作方法準備資料庫,版本
//2.寫Order_Model
//3.寫好新增購物車,刪除購物車,查詢購物車後
//4.到FoodDetail頁面去設定按鈕點擊事件addCart
//5.配置Cart版面 layout=>activity :cart
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import tw.org.iii.appps.wenxlufood.Model.Order;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="EatitDB.db";//表名檔案
    private static final int DB_VER =1;//版本數
    //1.設定資料庫
    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);//(1.活在這個activity,2.庫名3.游標指引4.版本數)
    }
    //SQLiteDatabase getReadableDatabase()
    // setTables(@RecentlyNullable String inTables)
    // Cursor query(SQLiteDatabase db,
    // String[] projectionIn,
    // String selection,
    // String[] selectionArgs,
    // String groupBy,
    // String having, String sortOrder)

    //2.將資料庫的資訊冠到購物車上
    public List<Order> getCarts(){
        SQLiteDatabase  db =  getReadableDatabase(); //取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId","ProductName","Quantity","Price","Discount"};//要查詢的欄位
        String sqlTable ="OrderDetail"; //表名

        qb.setTables(sqlTable);//設定資料庫表
        Cursor c=  qb.query(
                 db, //1.表格名稱
                 sqlSelect,//2.欄位名稱
                null,//3.查詢條件
                null,//4.查詢條件的值
                null,//5.欄位群組
                null,
                null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst()){//將指標移至第一筆資料
            do {
                result.add(new Order( //建構是方法把資料庫抓到的值設定上去
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                ));
            }while (c.moveToNext()); //將指標移至下一筆資料,跳出迴圈
        }
        return result;
    }

    //3.商品新增至購物車
    public void addCart(Order order){
        SQLiteDatabase  db =  getReadableDatabase(); //取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String sql = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount)VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(sql);
    }
    //4.清除購物車
    public void clearCart(){
        SQLiteDatabase  db =  getReadableDatabase(); //取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String sql = String.format("DELETE FROM  OrderDetail");
        db.execSQL(sql);
    }
}
