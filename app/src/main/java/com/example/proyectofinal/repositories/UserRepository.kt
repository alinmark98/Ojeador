package com.example.proyectofinal.repositories

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.proyectofinal.modelos.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val usersCollection = db.collection("players")
    private val auth = FirebaseAuth.getInstance()
    private var ONE_MEGABYTE: Long = 1024 * 1024

    private fun addCollection(docID: String, user: User, bitmaps: List<Bitmap>) {
        db.collection("players")
            .document(docID) // Especifica el ID personalizado del documento
            .set(user)
            .addOnSuccessListener {
                Log.d("ADDSUCCESS", "Documento creado con ID: $docID")
                uploadPhotos(bitmaps)
            }
            .addOnFailureListener { e ->
                // Ha ocurrido un error al crear el documento
                // Maneja el error de alguna manera
                Log.d("ADDFAILURE", "Error al crear el documento: ${e.message}")
            }
    }

    fun registerUser(user: User, password: String, bitmaps: List<Bitmap>, listener: OnRegistrationCompleteListener) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val docID = firebaseUser?.uid

                    if (docID != null) {
                        signInWithEmail(user.email, password){ success ->
                            if(success){
                                sendEmailVerification()
                                addCollection(docID,user,bitmaps)
                            }else{
                                Log.d("USREP-SIGNIN", "ERROR")
                            }
                        }
                    }

                    listener.onRegistrationSuccess()
                } else {
                    // Ha ocurrido un error durante el registro
                    val exception = task.exception
                    listener.onRegistrationFailure(exception)
                }
            }
    }

    fun uploadPhotos(bitmaps: List<Bitmap>) {
        if (isUserAuthenticated()) {
            val storageRef = storage.reference
            val actualUser = getCurrentUserId()

            for ((index, bitmap) in bitmaps.withIndex()) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uri = "players/$actualUser/photo$index.jpg"
                val photoRef = storageRef.child(uri)

                updatePhotoAtIndex(actualUser,index,uri,
                    onSuccess = {
                        Log.e("USR-REP", "UPDATE PHOTOS SUCCESS")
                    },
                    onFailure = { exception ->
                        Log.e("USR-REP", exception.toString())
                    }
                )

                val uploadTask = photoRef.putBytes(data)
                uploadTask.addOnFailureListener { exception ->
                    // Error al subir la imagen
                    Log.d("IMAGE UPLOAD", "Error al subir la imagen: ${exception.message}")
                }.addOnSuccessListener { taskSnapshot ->
                    // Imagen subida exitosamente
                    Log.d("IMAGE UPLOAD", "Imagen subida exitosamente.")
                }
            }
        }
    }

    private fun updatePhotoAtIndex(user: String?, index: Int, photoUrl: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userDocRef = user?.let { usersCollection.document(it) }

        val photoField = when (index) {
            0 -> "photos.photo0"
            1 -> "photos.photo1"
            2 -> "photos.photo2"
            3 -> "photos.photo3"
            else -> throw IllegalArgumentException("Invalid index")
        }

        val updatedData = hashMapOf<String, Any>(
            photoField to photoUrl
        )

        userDocRef?.update(updatedData)?.addOnSuccessListener {
            onSuccess.invoke()
        }?.addOnFailureListener { exception ->
            onFailure.invoke(exception)
        }
    }

    private fun sendEmailVerification() {
        val currentUser = auth.currentUser

        currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("EMAIL SENT", "SUCCESFULL")
                } else {
                    Log.d("EMAIL SENT", "FAILED")
                }
            }
    }

    fun checkEmailVerification(completion: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.reload()
        completion(currentUser?.isEmailVerified == true)
    }

    fun getCurrentUserId(): String? {
        val currentUser = auth.currentUser
        return currentUser?.uid
    }

     fun isUserAuthenticated(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun signInWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    callback(true)
                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    callback(false)
                }
            }
    }

    fun checkEmailExists(email: String, onComplete: (Boolean) -> Unit) {
        usersCollection
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    val emailExists = querySnapshot != null && !querySnapshot.isEmpty
                    onComplete(emailExists)
                } else {
                    onComplete(false) // Se produjo un error al consultar Firestore
                }
            }
    }

    fun downloadProfileImages(
        imagePath: String,
        onSuccess: (Bitmap) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageRef = storage.reference
        val imageRef = storageRef.child(imagePath)

        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                onSuccess(bitmap)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun fetchUsers(
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        usersCollection.get()
            .addOnSuccessListener { result ->
                val users = mutableListOf<User>()

                for (document in result) {
                    val name = document.getString("name")
                    val surname = document.getString("surname")
                    val born = document.getString("born")
                    val location = document.getString("location")
                    val position = document.getString("position")
                    val height = document.getDouble("height")
                    val weight = document.getDouble("weight")
                    val description = document.getString("description")

                    if (name != null && description != null && surname != null &&
                        born != null && location != null && position != null &&
                        height != null && weight != null) {

                        val photosData = document.get("photos") as? HashMap<*, *>
                        val photo0 = photosData?.get("photo0") as? String ?: ""

                        val skillsData = document.get("skills") as? HashMap<*, *>
                        val dribbling = skillsData?.get("dribbling")?.toString()?.toIntOrNull() ?: 0
                        val shooting = skillsData?.get("shooting")?.toString()?.toIntOrNull() ?: 0
                        val defending = skillsData?.get("defending")?.toString()?.toIntOrNull() ?: 0
                        val speed = skillsData?.get("speed")?.toString()?.toIntOrNull() ?: 0
                        val passing = skillsData?.get("passing")?.toString()?.toIntOrNull() ?: 0
                        val physicality = skillsData?.get("physicality")?.toString()?.toIntOrNull() ?: 0


                        Log.e("USER-REP", dribbling.toString())
                        Log.e("USER-REP", photo0)

                        val skills = User.Skills(
                            dribbling = dribbling,
                            shooting = shooting,
                            defending = defending,
                            speed = speed,
                            passing = passing,
                            physicality = physicality
                        )

                        val user = User(
                            name = name,
                            surname = surname,
                            position = position,
                            born = born,
                            location = location,
                            height = height,
                            weight = weight,
                            description = description,
                            photos = User.Photos(photo0 = photo0),
                            skills = skills
                        )

                        users.add(user)
                    }
                }

                Log.d("USREP", users.count().toString())
                onSuccess(users)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun firebaseAuthWithGoogle(idToken: String?, callback: AuthCallback) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    callback.onAuthenticationSuccess()
                } else {
                    // El inicio de sesión con Google falló
                    Log.e(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    callback.onAuthenticationFailure()
                }
            }
    }

    fun isUserSignedOut(): Boolean {
        val currentUser = auth.currentUser
        return currentUser == null
    }

    fun signOut() {
        auth.signOut()
    }

    interface AuthCallback {
        fun onAuthenticationSuccess()
        fun onAuthenticationFailure()
    }

    interface OnRegistrationCompleteListener {
        fun onRegistrationSuccess()
        fun onRegistrationFailure(exception: Exception?)
    }
}