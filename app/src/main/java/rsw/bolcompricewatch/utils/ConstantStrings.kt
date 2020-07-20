package rsw.bolcompricewatch.utils

class ConstantStrings {
    //Keys for the json wishlist retrieved from bol.com
    companion object{
        const val bolWlUrl: String = "https://www.bol.com/nl/verlanglijstje/%s/index.html"

        const val wlScriptSelector: String = "script[type=text/javascript]"

        const val wlArrayName: String = "wishlistProductTiles"
        const val wlItemName: String = "wishlistProductTile"
        const val wlItemIdName: String = "globalId"
        const val wlItemPriceName: String = "price"
        const val wlItemTitleName: String = "title"
        const val wlItemImageUrlName: String = "imageUrl"
        const val wlItemPageUrlName: String = "productPageUrl"

        const val wlEntryFormat: String = "%s:%s"
        const val wlEntriesDir: String = ""
        const val wlFileName: String = "wishlists.txt"
    }


}