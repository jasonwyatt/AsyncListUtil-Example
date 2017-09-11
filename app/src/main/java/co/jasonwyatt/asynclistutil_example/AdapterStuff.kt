package co.jasonwyatt.asynclistutil_example

import android.support.annotation.NonNull
import android.support.v7.util.AsyncListUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Adapter(private val listUtil: AsyncListUtil<Item>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindView(listUtil.getItem(position), position)
    }

    override fun getItemCount(): Int = listUtil.itemCount

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return ViewHolder(inf.inflate(R.layout.item, parent, false))
    }
}

class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView? = itemView?.findViewById(R.id.title)
    private val content: TextView? = itemView?.findViewById(R.id.content)

    fun bindView(item: Item?, position: Int) {
        title?.text = "$position ${item?.title ?: "loading"}"
        content?.text = item?.content ?: "loading"
    }
}

