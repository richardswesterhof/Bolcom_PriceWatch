package rsw.bolcompricewatch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.work.*

import kotlinx.android.synthetic.main.activity_main.*
import rsw.bolcompricewatch.utils.ConstantStrings
import rsw.bolcompricewatch.utils.CustomListAdapter
import rsw.bolcompricewatch.utils.ListStorage.Companion.getWishlistEntryPairs
import rsw.bolcompricewatch.utils.ListStorage.Companion.updateFile

class MainActivity: AppCompatActivity(), AddListDialog.AddListDialogListener{

    private lateinit var listView: ListView
    private lateinit var listAdapter: CustomListAdapter
    private lateinit var entries: ArrayList<Pair<String, String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener{openDialog()}

        listView = findViewById(R.id.wishlistsList)
        entries = getWishlistEntryPairs(
            this.getExternalFilesDir(null).toString() + ConstantStrings.wlEntriesDir,
            ConstantStrings.wlFileName)

        Log.d("PriceWatcher", "Retrieved the following entries: $entries")

        listAdapter = CustomListAdapter(this, entries)
        listView.adapter = listAdapter


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            Toast.makeText(this, "Clicked item $i", Toast.LENGTH_SHORT).show()
        }


        //TODO: make sure this work is scheduled instead of at startup
        //add the work to be done periodically:
        val priceWatchRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<PriceWatcher>().build()

        WorkManager.getInstance(this.applicationContext).enqueueUniqueWork(
            "testPriceWatcher",
            ExistingWorkPolicy.KEEP,
            priceWatchRequest)

    }

    private fun openDialog() {
        val dialog = AddListDialog()
        dialog.show(supportFragmentManager, "Add List Dialog")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addToList(name: String, id: String): Boolean {
        for(entry: Pair<String, String> in entries) {
            if(entry.second.equals(id)) return false
        }
        listAdapter.add(Pair(name, id))
        return true
    }

    override fun applyAdd(name: String, id: String) {
        val sanitisedName: String = name.replace(':', '-')
        val sanitisedId: String = id.replace(':', '-')
        if(!addToList(sanitisedName, sanitisedId)) Toast.makeText(this, "List with id $sanitisedId already exists", Toast.LENGTH_SHORT).show()
        else updateFile(this, ConstantStrings.wlFileName, entries)
    }
}
