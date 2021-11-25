package ru.inversion.sbpapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trn_info.view.*
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.pojo.Transaction
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.fragments.MerchantFragment


class TrnInfoAdapter(private val context: MerchantFragment): RecyclerView.Adapter<TrnInfoAdapter.TrnInfoViewHolder>() {

    var trnInfoList: List<Transaction> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrnInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trn_info, parent,false)
        return TrnInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrnInfoViewHolder, position: Int) {
        val trn = trnInfoList[position]
        with(holder){
            with(trn) {

                val payDateText = context.resources.getString(R.string.PAY_DATE)
                val paySumText = context.resources.getString(R.string.PAY_SUM)
                val trnNumText = context.resources.getString(R.string.TRN_NUM)

                tvTrnnum.text = String.format(trnNumText,orderNum)
                tvPayDate.text = String.format(payDateText,getFormatedTime())
                tvPaySum.text = String.format(paySumText,"%.2f".format(mtrnsum))
            }
        }
    }

    override fun getItemCount() = trnInfoList.size

    inner class TrnInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTrnnum = itemView.tvTrnnum
        val tvPayDate = itemView.tvPaySumNumber
        val tvPaySum = itemView.tvPaySum
    }
}