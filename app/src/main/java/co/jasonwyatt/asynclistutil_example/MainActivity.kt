package co.jasonwyatt.asynclistutil_example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AsyncAdapter
    private lateinit var itemSource: SQLiteItemSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler)

        itemSource = SQLiteItemSource(getDatabase(this, "database.sqlite"))
        adapter = AsyncAdapter(itemSource, recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.onStart(recyclerView)
    }

    override fun onStop() {
        super.onStop()
        adapter.onStop(recyclerView)
    }
}

