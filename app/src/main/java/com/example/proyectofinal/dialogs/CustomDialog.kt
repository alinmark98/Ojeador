import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.proyectofinal.R

class CustomDialog : DialogFragment() {
    private var listener: OnDialogClickListener? = null

    interface OnDialogClickListener {
        fun onOkClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDialogClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnDialogClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_signin_check, container, false)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        val messageType = arguments?.getInt(ARG_MESSAGE_TYPE, MESSAGE_TYPE_EMPTY_FIELDS)

        // Configurar el mensaje en función del tipo de mensaje
        val messageResId = when (messageType) {
            MESSAGE_TYPE_EMPTY_FIELDS -> R.string.empty_fields_message
            MESSAGE_TYPE_INVALID_CREDENTIALS -> R.string.invalid_credentials_message
            else -> R.string.default_message
        }
        val message = getString(messageResId)
        view.findViewById<TextView>(R.id.txtMessage).text = message

        buttonOk.setOnClickListener {
            listener?.onOkClicked()
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    companion object {
        private const val ARG_MESSAGE_TYPE = "message_type"
        const val MESSAGE_TYPE_EMPTY_FIELDS = 0
        const val MESSAGE_TYPE_INVALID_CREDENTIALS = 1

        fun newInstance(messageType: Int): CustomDialog {
            val fragment = CustomDialog()
            val args = Bundle()
            args.putInt(ARG_MESSAGE_TYPE, messageType)
            fragment.arguments = args
            return fragment
        }
    }
}
