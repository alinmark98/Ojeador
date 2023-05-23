package com.example.proyectofinal.fragments.Register

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import org.w3c.dom.Text

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
    private lateinit var imgView1: ImageView
    private lateinit var imgView2: ImageView
    private lateinit var imgView3: ImageView
    private lateinit var imgView4: ImageView
    private lateinit var btn1: ImageButton

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
        imgView1 = view.findViewById(R.id.imageView1)
        imgView2 = view.findViewById(R.id.imageView2)
        imgView3 = view.findViewById(R.id.imageView3)
        imgView4 = view.findViewById(R.id.imageView4)
        btn1 = view.findViewById(R.id.button1)


        return view

    }

    fun displayReceivedData(name: String) {
        Log.d(name, "LLEGA AL METODO")
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
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

    private fun pickImageFromGallery(imageIndex: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000 + imageIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            imgView1.setImageURI(selectedImage)
            btn1.setImageDrawable(null)

            // haz algo con la imagen seleccionada, por ejemplo:
            // imageView.setImageURI(selectedImage)
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            //binding.imageView2.setImageURI(selectedImage)
            //binding.button2.setImageDrawable(null)

            // haz algo con la imagen seleccionada, por ejemplo:
            // imageView.setImageURI(selectedImage)
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            //binding.imageView3.setImageURI(selectedImage)
            //binding.button3.setImageDrawable(null)

            // haz algo con la imagen seleccionada, por ejemplo:
            // imageView.setImageURI(selectedImage)
        }
        if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            //binding.imageView4.setImageURI(selectedImage)
            //binding.button4.setImageDrawable(null)

            // haz algo con la imagen seleccionada, por ejemplo:
            // imageView.setImageURI(selectedImage)
        }
    }
}
