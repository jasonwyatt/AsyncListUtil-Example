package co.jasonwyatt.asynclistutil_example

import android.content.Context
import android.database.Cursor
import android.support.v7.util.AsyncListUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

/**
 *
 */
class MyDataCallback(c: Context, private val assetFileName: String) : AsyncListUtil.DataCallback<Item>() {
    private var cursor: Cursor? = null
    private val context: Context = c.applicationContext

    override fun fillData(data: Array<Item>?, startPosition: Int, itemCount: Int) {
        Log.d("MyDataCallback", "fillData: $startPosition, $itemCount")
        var c = cursor
        if (c == null || c.isClosed) {
            cursor = buildItemCursor(context, assetFileName)
            c = cursor
        }
        if (c != null && data != null) {
            c.moveToPosition(startPosition)
            for (i in 0 until itemCount) {
                val item: Item? = data[i]
                if (item == null) {
                    data[i] = Item(c.getString(0), c.getString(1))
                } else {
                    item.title = c.getString(0)
                    item.content = c.getString(1)
                }

                c.moveToNext()
            }
        }
    }

    override fun refreshData(): Int {
        cursor = buildItemCursor(context, assetFileName)
        Log.d("MyDataCallback", "refreshData: ${cursor?.count ?: 0}")
        return cursor?.count ?: 0
    }

    fun tearDown() {
        cursor?.close()
    }
}

class MyViewCallback(val recyclerView: RecyclerView) : AsyncListUtil.ViewCallback() {
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

class AsyncListScrollListener(val listUtil: AsyncListUtil<in Item>) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        listUtil.onRangeChanged()
    }
}