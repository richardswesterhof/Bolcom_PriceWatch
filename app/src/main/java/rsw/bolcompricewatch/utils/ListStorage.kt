package rsw.bolcompricewatch.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ListStorage {

    companion object {
        fun getWishlistEntryPairs(dir: String, filename: String): ArrayList<Pair<String, String>> {
            val array = ArrayList<Pair<String, String>>()
            array.add(Pair("PriceWatch", "1y356631333462656330636532636437323566636634376331"))
            return array
        }


        fun getWishlistIds(dir: String, filename: String): ArrayList<String> {
            val array = ArrayList<String>()
            getWishlistEntryPairs(dir, filename).forEach{item -> array.add(item.second)}
            return array
        }

        fun updateFile(context: Context, fileName: String, entries: ArrayList<Pair<String, String>>): Boolean {
            val lines = ArrayList<String>()
            entries.forEach{entry -> lines.add(formatAsWishlistEntry(entry))}
            val dir = File(context.filesDir, ConstantStrings.wlEntriesDir)
            if(!dir.exists()) {
                if(!dir.mkdir()) return false
            }

            var wishlistEntries = File("")

            try {
                wishlistEntries = File(dir, fileName)
                val writer = FileWriter(wishlistEntries)
                lines.forEach{line ->
                    writer.append(line)
                    writer.append('\n')
                }
                writer.flush()
                writer.close()
            } catch(e: IOException) {
                Log.e("FileWrite", "Could not write to file: $wishlistEntries")
                return false
            }
            return true
        }

        fun formatAsWishlistEntry(name: String, id: String): String {
            return ConstantStrings.wlEntryFormat.format(name, id)
        }

        fun formatAsWishlistEntry(pair: Pair<String, String>): String {
            return ConstantStrings.wlEntryFormat.format(pair.first, pair.second)
        }
    }
}