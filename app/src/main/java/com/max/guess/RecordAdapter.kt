package com.max.guess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.max.guess.data.Record
import kotlinx.android.synthetic.main.row_record.view.*
/**
 * 此頁有兩個類別:
 * RecordAdapter & RecordViewHolder
 *
 */
// 繼承要留意 要選擇RecyclerView的Adapter ; 鑽石符號<>放進自定義的ViewHolder名稱 EX : RecordViewHolder ; 最後記得要加空的建構子()
class RecordAdapter(var records : List<Record>) :RecyclerView.Adapter<RecordViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_record,parent,false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) { // onBindViewHolder說明: 當資料有的時候,會被自動呼叫  , 就需要設定兩樣東西,一個是暱稱,一個是猜成功的次數
        holder.nickText.text = records.get(position).nickname               // 第一個是holder裡面的nickText , .text是因為是TextView ; records是一個集合,所以利用position得到那一個record值,再呼叫record值裡面的屬性(EX : .nickname)
        holder.counterText.text = records.get(position).counter.toString()  // counterText在實作的時候要小心,因為在record裡面她是個Int,所以得到counter的Int值之後,記得要toString,才可以設定到text裡面
    }

    override fun getItemCount(): Int {
        return records.size             // 這裡要留意 records 不是我們的屬性 , 要在RecordAdapter 參數裡面的records前面加var , 才會變成屬性 ,才能在這裡使用
    }

}

/**
 * 創建ViewHolder框架完 EX : class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view){ }
 * 需要再創建單列的layout EX : row_record.xml
 *
 */
class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var nickText = view.record_nickname     // row_record.xml的暱稱ID EX : record_nickname
    var counterText = view.record_counter   // row_record.xml的次數ID EX : record_counter
}