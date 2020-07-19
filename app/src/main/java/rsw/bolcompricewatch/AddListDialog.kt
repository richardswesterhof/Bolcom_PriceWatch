package rsw.bolcompricewatch

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import java.lang.ClassCastException

class AddListDialog(): AppCompatDialogFragment() {

    private lateinit var nameField: EditText
    private lateinit var idField: EditText
    private lateinit var dialogListener: AddListDialogListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater: LayoutInflater =  activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.layout_dialog, null)

        builder
            .setView(view)
            .setTitle("Add List")
            .setNegativeButton("cancel", DialogInterface.OnClickListener { _, i ->
                //user clicked cancel
            })
            .setPositiveButton("Confirm", DialogInterface.OnClickListener { _, i ->
                //user clicked ok
                val name: String = nameField.text.toString()
                val id: String = idField.text.toString()

                dialogListener.applyAdd(name, id)
            })

        nameField = view.findViewById(R.id.edit_newList_name);
        idField = view.findViewById(R.id.edit_newList_id);

        return builder.create()
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            dialogListener = context as AddListDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement AddListDialogListener")
        }
    }

    interface AddListDialogListener {
        fun applyAdd(name: String, id: String)
    }
}