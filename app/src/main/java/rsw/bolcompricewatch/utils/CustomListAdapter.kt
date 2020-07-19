package rsw.bolcompricewatch.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import rsw.bolcompricewatch.R


class CustomListAdapter(context: Context, entries: ArrayList<Pair<String, String>>, placeholders: ArrayList<String>):
        ArrayAdapter<String>(context, R.layout.row, R.id.wishlistTitle, placeholders) {

    private var ctxt: Context
    private var entries: List<Pair<String, String>>


    init {
        ctxt = context
        this.entries = entries
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var row: View

        row = convertView ?: inflater.inflate(R.layout.row, parent, false)

        val name: TextView = row.findViewById(R.id.wishlistTitle)
        val id: TextView = row.findViewById(R.id.wishlistURL)

        name.text = entries[position].first
        id.text = entries[position].second

        return row
    }
}