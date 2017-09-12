package co.jasonwyatt.asynclistutil_example

import android.support.annotation.NonNull
import android.support.v7.util.AsyncListUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class AsyncAdapter(itemSource: ItemSource, recyclerView: RecyclerView) : RecyclerView.Adapter<ViewHolder>() {
    private val viewCallback = ViewCallback(recyclerView)
    private val dataCallback = DataCallback(itemSource)
    private val listUtil = AsyncListUtil(Item::class.java, 500, dataCallback, viewCallback)
    private val onScrollListener = ScrollListener(listUtil)

    fun onStart(recyclerView: RecyclerView?) {
        if (recyclerView == null) {
            return
        }
        recyclerView.addOnScrollListener(onScrollListener)
        listUtil.refresh()
    }

    fun onStop(recyclerView: RecyclerView?) {
        recyclerView?.removeOnScrollListener(onScrollListener)
        dataCallback.close()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindView(listUtil.getItem(position), position)
    }

    override fun getItemCount(): Int = listUtil.itemCount

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return ViewHolder(inf.inflate(R.layout.item, parent, false))
    }
}

private class DataCallback(val itemSource: ItemSource) : AsyncListUtil.DataCallback<Item>() {
    override fun fillData(data: Array<Item>?, startPosition: Int, itemCount: Int) {
        if (data != null) {
            for (i in 0 until itemCount) {
                data[i] = itemSource.getItem(startPosition + i)
            }
        }
    }

    override fun refreshData(): Int = itemSource.getCount()

    fun close() {
        itemSource.close()
    }
}

private class ViewCallback(val recyclerView: RecyclerView) : AsyncListUtil.ViewCallback() {
    override fun onDataRefresh() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun getItemRangeInto(outRange: IntArray?) {
        if (outRange == null) {
            return
        }
        (recyclerView.layoutManager as LinearLayoutManager).let { llm ->
            outRange[0] = llm.findFirstVisibleItemPosition()
            outRange[1] = llm.findLastVisibleItemPosition()
        }

        if (outRange[0] == -1 && outRange[1] == -1) {
            outRange[0] = 0
            outRange[1] = 0
        }
    }

    override fun onItemLoaded(position: Int) {
        recyclerView.adapter.notifyItemChanged(position)
    }
}

private class ScrollListener(val listUtil: AsyncListUtil<in Item>) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        listUtil.onRangeChanged()
    }
}