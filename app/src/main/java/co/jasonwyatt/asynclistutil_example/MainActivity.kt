package co.jasonwyatt.asynclistutil_example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.AsyncListUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var listUtil: AsyncListUtil<Item>
    private lateinit var dataCallback: MyDataCallback
    private lateinit var viewCallback: MyViewCallback
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        dataCallback = MyDataCallback(this, "database.sqlite")
        viewCallback = MyViewCallback(recyclerView)

        listUtil = AsyncListUtil(Item::class.java, 100, dataCallback, viewCallback)

        adapter = Adapter(listUtil)

        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(AsyncListScrollListener(listUtil))
    }

    override fun onStop() {
        super.onStop()
        dataCallback.tearDown()
    }
}

