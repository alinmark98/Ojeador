import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.authentication.AccountSelectionActivity
import com.example.proyectofinal.activities.authentication.RegisterActivity
import com.example.proyectofinal.activities.terms.TermsAndConditionsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean>
        get() = _signInSuccess

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean>
        get() = _signUpSuccess

    private val _sendVerificationEmailSuccess = MutableLiveData<Boolean>()
    val sendVerificationEmailSuccess: LiveData<Boolean>
        get() = _sendVerificationEmailSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        _signInSuccess.value = false
        _signUpSuccess.value = false
        _sendVerificationEmailSuccess.value = false
    }

    fun showDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_signup_layout, null)
        val btnConditions = dialogView.findViewById<Button>(R.id.btnConditions)

        btnConditions.setOnClickListener {
            val intent = Intent(context, TermsAndConditionsActivity::class.java)
            context.startActivity(intent)
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
            .setPositiveButton("Aceptar y continuar") { dialog, which ->
                val intent = Intent(context, AccountSelectionActivity::class.java)
                context.startActivity(intent)
            }
            .setNegativeButton("Cancelar y salir") { dialog, which ->
                (context as? Activity)?.finish()
            }

        val dialog = builder.create()
        dialog.show()
    }

}