package com.example.proyectofinal.fragments.Register

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentFirstBinding
import com.example.proyectofinal.databinding.FragmentThirdBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [skillsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class ThirdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val PICK_IMAGE_REQUEST = 1
    private var imageViews: Array<ImageView?> = arrayOfNulls(4)
    private var imageButtons: Array<ImageButton?> = arrayOfNulls(4)
    private var seekBars: Array<SeekBar?> = arrayOfNulls(6)
    private var sendDataFromFragment: SendDataFromFragment3? = null
    private var allDataChecked: Boolean = false
    private var images: MutableList<Bitmap> = mutableListOf()
    private var skillsValues: HashMap<String, Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val registerButton =  view.findViewById<Button>(R.id.btnRegister)

        imageViews[0] = view.findViewById(R.id.imageView1)
        imageViews[1] = view.findViewById(R.id.imageView2)
        imageViews[2] = view.findViewById(R.id.imageView3)
        imageViews[3] = view.findViewById(R.id.imageView4)

        seekBars[0] = view.findViewById(R.id.seekBarDribbling)
        seekBars[1] = view.findViewById(R.id.seekBarShooting)
        seekBars[2] = view.findViewById(R.id.seekBarDefending)
        seekBars[3] = view.findViewById(R.id.seekBarSpeed)
        seekBars[4] = view.findViewById(R.id.seekBarPassing)
        seekBars[5] = view.findViewById(R.id.seekBarPhysicality)

        imageButtons[0] = view.findViewById(R.id.button1)
        imageButtons[1] = view.findViewById(R.id.button2)
        imageButtons[2] = view.findViewById(R.id.button3)
        imageButtons[3] = view.findViewById(R.id.button4)

        for (i in imageButtons.indices) {
            imageButtons[i]?.setOnClickListener {
                chooseImage(i)
                imageButtons[i]?.setImageDrawable(null)
            }
        }
        registerButton.setOnClickListener(){
           /* if(imageViews[0]?.drawable != null || imageViews[1]?.drawable != null ||
                imageViews[2]?.drawable != null || imageViews[3]?.drawable != null){

                sendDataFromFragment?.checkThirdFragment()
                sendDataFromFragment?.sendDataThirdFragment(images, addSkillsToHashMap())

                /*if(allDataChecked){
                }else{
                    Toast.makeText(context, "Check fields", Toast.LENGTH_SHORT).show()
                }*/
                Log.d("IMAGEN", "CONTIENE")
            }else{
                Toast.makeText(context, "Select at least 1 image", Toast.LENGTH_SHORT).show()
            }*/
            // Get the data from an ImageView as bytes
            // Create a storage reference from our app
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("users/9Q84WY5l2InH6c1pHDA2/profilePhoto.jpg")
            imageViews[0]?.isDrawingCacheEnabled = true
            imageViews[0]?.buildDrawingCache()
            val bitmap = (imageViews[0]?.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = storageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
        }
        return view
    }

    private fun addSkillsToHashMap(): HashMap<String, Int>{
        skillsValues["dribbling"] = seekBars[0]?.progress ?: 0
        skillsValues["shooting"] = seekBars[1]?.progress ?: 0
        skillsValues["defending"] = seekBars[2]?.progress ?: 0
        skillsValues["speed"] = seekBars[3]?.progress ?: 0
        skillsValues["passing"] = seekBars[4]?.progress ?: 0
        skillsValues["physicality"] = seekBars[5]?.progress ?: 0

        return skillsValues
    }

    private fun chooseImage(imageViewIndex: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST + imageViewIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageViewIndex = requestCode - PICK_IMAGE_REQUEST
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            val contentResolver = context?.contentResolver
            val bitmap = BitmapFactory.decodeStream(imageUri?.let {
                contentResolver?.openInputStream(
                    it
                )
            })
            imageViews[imageViewIndex]?.setImageBitmap(bitmap)
            imageButtons[imageViewIndex]?.setImageDrawable(null)
            images.plus(bitmap)
        }
    }

    interface SendDataFromFragment3 : FirstFragment.SendDataFromFragment1 {
        fun checkThirdFragment()
        fun sendDataThirdFragment(images: List<Bitmap>,skills: HashMap<String, Int>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            sendDataFromFragment = activity as SendDataFromFragment3?
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }
    }

    fun dataReceived(firstFragChecked: Boolean, secondFragChecked: Boolean){
        if(firstFragChecked && secondFragChecked){
            allDataChecked = true
            Log.d("DATARECEIVED", "DATARECEIVED")
        }else{
            allDataChecked = false
            Log.d("DATARECEIVED", "FALSE")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment skillsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
