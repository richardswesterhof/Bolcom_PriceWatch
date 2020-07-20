package rsw.bolcompricewatch

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import rsw.bolcompricewatch.utils.ConstantStrings
import rsw.bolcompricewatch.utils.ListStorage.Companion.getWishlistIds
import java.util.*

class PriceWatcher(appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val listIds: List<String> = getWishlistIds(
            applicationContext.getExternalFilesDir(null).toString() + ConstantStrings.wlEntriesDir,
            ConstantStrings.wlFileName)
        val allWishlistItems = JSONObject()


        for(listId: IndexedValue<String> in listIds.withIndex()) {
            val wishlist: JSONArray = getWishlistItems(listId.value)

            for(i: Int in 0 until wishlist.length()) {
                val nextItem: JSONObject = wishlist.getJSONObject(i)
                allWishlistItems.putOpt(
                    nextItem.getJSONObject(ConstantStrings.wlItemName).getString(ConstantStrings.wlItemIdName),
                    nextItem)
            }
        }

        Log.d("PriceWatcher", "PriceWatcher retrieved all wishlists: $allWishlistItems")

        return Result.success()
    }

    private fun getWishlistItems(wishlistId: String): JSONArray {
        Log.d("PriceWatcher", "Retrieving wishlist id: $wishlistId")

        val url: String = ConstantStrings.bolWlUrl.format(wishlistId)
        Log.d("PriceWatcher", "URL was set to: $url")

        var jsonArray = JSONArray()

        try {
            val document: Document = Jsoup.connect(url).get()
            val scripts: Elements = document.select(ConstantStrings.wlScriptSelector)
            val rawExtractedJson: String = scripts[0].html().trim().dropWhile{c -> c != '{'}.dropLast(1)
            Log.d("PriceWatcher", "Raw json from response: $rawExtractedJson")
            val json = JSONObject(rawExtractedJson)
            jsonArray = json.getJSONArray(ConstantStrings.wlArrayName)
        }
        catch(e: HttpStatusException) {
            Log.e("PriceWatcher", "GET request to $url failed with code ${e.statusCode}: ${e.message}")
        }

        return jsonArray
    }
}