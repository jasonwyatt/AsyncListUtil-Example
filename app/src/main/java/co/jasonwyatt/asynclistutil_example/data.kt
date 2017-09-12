package co.jasonwyatt.asynclistutil_example

class Item(var title: String, var content: String)

interface ItemSource {
    fun getCount(): Int
    fun getItem(position: Int): Item
    fun close()
}
