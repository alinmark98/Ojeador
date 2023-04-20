import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectofinal.HomeActivity
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

    fun signIn(email: String, password: String, function: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                _signInSuccess.value = true
                Log.w(ContentValues.TAG, "signInWithEmail:success")
                // Llama a la función lambda proporcionada después del inicio de sesión exitoso
                function()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "signInWithEmail:failure", exception)
                _errorMessage.value = "Authentication failed: ${exception.message}"
            }
    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                _signUpSuccess.value = true
                Log.w(ContentValues.TAG, "createUserWithEmail:success")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", exception)
                _errorMessage.value = "Authentication failed: ${exception.message}"
            }
    }

    fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnSuccessListener {
                _sendVerificationEmailSuccess.value = true
                Log.w(ContentValues.TAG, "Verification email sent to ${user.email}")
            }
            ?.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "sendEmailVerification:failure", exception)
                _errorMessage.value = "Error sending verification email: ${exception.message}"
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun onCleared() {
        super.onCleared()
        // Limpieza de recursos si es necesario
    }
}