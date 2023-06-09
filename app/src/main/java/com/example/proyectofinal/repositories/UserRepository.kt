package com.example.proyectofinal.repositories

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.models.Scout
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val playersCollection = db.collection("players")
    private val scoutsCollection = db.collection("scouts")
    private val auth = FirebaseAuth.getInstance()
    private var ONE_MEGABYTE: Long = 1024 * 1024
    private var userType: String = ""

    private fun createUserDocument(docID: String, user: Any, bitmaps: List<Bitmap>) {

        if(user is Player){
            userType = "players"
            user.setId(docID)
        }else if (user is Scout){
            userType = "scouts"
            user.setId(docID)
        }

        db.collection(userType)
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

    fun registerUser(user: Any, email: String, password: String, bitmaps: List<Bitmap>, listener: OnRegistrationCompleteListener) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val docID = firebaseUser?.uid

                    if (docID != null) {
                        signInWithEmail(email, password){ success ->
                            if(success){
                                sendEmailVerification()
                                createUserDocument(docID,user,bitmaps)
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

    fun registerUserWithGoogle(user: Any, bitmaps: List<Bitmap>, listener: OnRegistrationCompleteListener) {
        val firebaseUser = auth.currentUser
        val docID = firebaseUser?.uid

        if (docID != null) {
            createUserDocument(docID,user,bitmaps)
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
                var uri: String = ""

                uri = if(userType == "players"){
                    "players/$actualUser/photo$index.jpg"
                }else{
                    "scouts/$actualUser/photo$index.jpg"
                }

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
        val userDocRef: DocumentReference?
        if(userType == "players"){
            userDocRef = user?.let { playersCollection.document(it) }
        }else{
            userDocRef = user?.let { scoutsCollection.document(it) }
        }

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
        playersCollection
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

    fun fetchPlayerUsers(
        onSuccess: (List<Player>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        playersCollection.get()
            .addOnSuccessListener { result ->
                val players = mutableListOf<Player>()

                for (document in result) {

                    val name = document.getString("name")
                    val surname = document.getString("surname")
                    val born = document.getString("born")
                    val location = document.getString("location")
                    val position = document.getString("position")
                    val height = document.getDouble("height")
                    val weight = document.getDouble("weight")
                    val description = document.getString("description")
                    val visibility = document.getLong("visibility")?.toInt()

                    if (name != null && description != null && surname != null &&
                        born != null && location != null && position != null &&
                        height != null && weight != null) {

                        // Omitir el usuario con visibilidad 0
                        if (visibility != null && visibility == 0) {
                            continue
                        }

                        val photosData = document.get("photos") as? HashMap<*, *>
                        val photo0 = photosData?.get("photo0") as? String ?: ""

                        val skillsData = document.get("skills") as? HashMap<*, *>
                        val dribbling = skillsData?.get("dribbling")?.toString()?.toIntOrNull() ?: 0
                        val shooting = skillsData?.get("shooting")?.toString()?.toIntOrNull() ?: 0
                        val defending = skillsData?.get("defending")?.toString()?.toIntOrNull() ?: 0
                        val speed = skillsData?.get("speed")?.toString()?.toIntOrNull() ?: 0
                        val passing = skillsData?.get("passing")?.toString()?.toIntOrNull() ?: 0
                        val physicality = skillsData?.get("physicality")?.toString()?.toIntOrNull() ?: 0

                        val skills = Player.Skills(
                            dribbling = dribbling,
                            shooting = shooting,
                            defending = defending,
                            speed = speed,
                            passing = passing,
                            physicality = physicality
                        )

                        val player = Player(
                            name = name,
                            surname = surname,
                            position = position,
                            born = born,
                            location = location,
                            height = height,
                            weight = weight,
                            description = description,
                            photos = Player.Photos(photo0 = photo0),
                            skills = skills
                        )

                        players.add(player)
                    }
                }

                onSuccess(players)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun fetchScoutsUsers(
        onSuccess: (List<Scout>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        scoutsCollection.get()
            .addOnSuccessListener { result ->
                val scouts = mutableListOf<Scout>()

                for (document in result) {
                    val name = document.getString("name")
                    val surname = document.getString("surname")
                    val born = document.getString("born")
                    val location = document.getString("location")
                    val category = document.getString("category")
                    val description = document.getString("description")
                    val team = document.getString("teamID")
                    val visibility = document.getLong("visibility")?.toInt()

                    if (name != null && description != null && surname != null &&
                        born != null && location != null && team != null && category != null) {

                        val photosData = document.get("photos") as? HashMap<*, *>
                        val photo0 = photosData?.get("photo0") as? String ?: ""

                        // Omitir el usuario con visibilidad 0
                        if (visibility != null && visibility == 0) {
                            continue
                        }

                        val scout = Scout(
                            name = name,
                            surname = surname,
                            born = born,
                            location = location,
                            description = description,
                            teamID = team,
                            category = category,
                            photos = Scout.Photos(photo0 = photo0)
                        )

                        scouts.add(scout)
                    }
                }

                onSuccess(scouts)
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
                    val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false
                    if (isNewUser) {
                        // Usuario nuevo, redirigir a la actividad de selección de cuenta
                        callback.onNewUserRegistration()
                    } else {
                        // Usuario existente, iniciar sesión
                        callback.onAuthenticationSuccess()
                    }
                } else {
                    // El inicio de sesión con Google falló
                    Log.e(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    callback.onAuthenticationFailure()
                }
            }
    }


    fun checkDocumentExists(parametro: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        val playersQuery = playersCollection.whereEqualTo(FieldPath.documentId(), parametro)
        val scoutsQuery = scoutsCollection.whereEqualTo(FieldPath.documentId(), parametro)

        val playersTask = playersQuery.get()
        val scoutsTask = scoutsQuery.get()

        Tasks.whenAllComplete(playersTask, scoutsTask)
            .addOnSuccessListener { tasks ->
                var documentExists = false

                for (task in tasks) {
                    if (task.isSuccessful) {
                        val querySnapshot = task.result as QuerySnapshot
                        if (!querySnapshot.isEmpty) {
                            documentExists = true
                            break
                        }
                    } else {
                        onFailure(task.exception as Exception)
                        return@addOnSuccessListener
                    }
                }
                onSuccess(documentExists)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    suspend fun checkUserCollection(): String {
        val firebaseUser = auth.currentUser
        val uid = firebaseUser?.uid

        if (uid != null) {
            val playersCollection = db.collection("players").document(uid)
            val scoutsCollection = db.collection("scouts").document(uid)

            val playerSnapshot = playersCollection.get().await()
            if (playerSnapshot.exists()) {
                // El usuario pertenece a la colección "players"
                return "players"
            } else {
                val scoutSnapshot = scoutsCollection.get().await()
                if (scoutSnapshot.exists()) {
                    // El usuario pertenece a la colección "scouts"
                    return "scouts"
                }
            }
        }
        // El usuario no pertenece a ninguna colección
        return ""
    }

    private fun deleteUserFirestore(userId: String, collectionName: String): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection(collectionName)

        return collection.document(userId).delete()
    }

    private fun deleteUserStorage(userId: String, collectionName: String) {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        // Eliminar documento de Firestore
        val collection = db.collection(collectionName)
        collection.document(userId).delete()

        // Eliminar carpeta de Storage
        val storageReference = storage.reference.child(collectionName).child(userId)
        storageReference.delete()
    }

    fun deleteUserAuthentication(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = auth.currentUser

        if (user != null) {
            user.delete()
                .addOnSuccessListener {
                    val userid = auth.currentUser?.uid
                    if (userid != null) {
                        val collection = getCollectionName(userid)
                        if (collection != null) {
                            deleteUserFirestore(userid, collection)
                            deleteUserStorage(userid, collection)
                        } else {
                            // Manejar el caso cuando no se encuentra la colección
                            Log.d(TAG, "No se encontró la colección para el usuario con ID: $userid")
                        }
                    }else {
                        // Manejar el caso cuando no se encuentra la colección
                        Log.d(TAG, "No se encontró el usuario con ID: $userid")
                    }
                    onSuccess.invoke()
                }
                .addOnFailureListener { exception ->
                    onFailure.invoke(exception)
                }
        } else {
            // Manejar el caso cuando el usuario actual es nulo
            val exception = Exception("No hay usuario autenticado")
            onFailure.invoke(exception)
        }
    }

    private fun getCollectionName(docId: String): String? {
        val collections = listOf("players", "scouts") // Agrega aquí los nombres de las colecciones que deseas verificar

        for (collectionName in collections) {
            val documentReference: DocumentReference = db.collection(collectionName).document(docId)
            val documentSnapshot = documentReference.get()

            if (documentSnapshot.isSuccessful) {
                val snapshot = documentSnapshot.result
                if (snapshot != null && snapshot.exists()) {
                    return collectionName
                }
            }
        }

        return null
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
        fun onNewUserRegistration()
    }

    interface OnRegistrationCompleteListener {
        fun onRegistrationSuccess()
        fun onRegistrationFailure(exception: Exception?)
    }

}