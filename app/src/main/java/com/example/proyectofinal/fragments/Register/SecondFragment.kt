package com.example.proyectofinal.fragments.Register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentFirstBinding
import com.example.proyectofinal.databinding.FragmentSecondBinding
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [perInfo.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var sendDataFromFragment: SecondFragment.SendDataFromFragment2? = null

    private lateinit var spinGender: String
    private lateinit var spinPosition: String
    private var etHeight by Delegates.notNull<Int>()
    private var etWeight by Delegates.notNull<Int>()
    private lateinit var etDescription: String

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
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        spinGender = view.findViewById<Spinner>(R.id.genderSpinner).selectedItem as String
        spinPosition = view.findViewById<Spinner>(R.id.positionSpinner).selectedItem as String
        etHeight = view.findViewById<EditText>(R.id.etHeight).text.toString().toIntOrNull() ?: 0
        etWeight = view.findViewById<EditText>(R.id.etWeight).text.toString().toIntOrNull() ?: 0
        etDescription = view.findViewById<EditText>(R.id.etDescription).text.toString()

        return view
    }

    interface SendDataFromFragment2 : FirstFragment.SendDataFromFragment1 {
        fun sendDataSecondFragment(gender: String?, position: String?, height: Int?,
                                   weight: Int?, description: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            sendDataFromFragment = activity as SendDataFromFragment2?
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }
    }

    override fun onPause() {
        super.onPause()
        if(!emptyCheck()){
            Toast.makeText(context, "Check fields", Toast.LENGTH_SHORT).show()
        }else{
            sendDataFromFragment?.sendDataSecondFragment(spinGender,spinPosition,etHeight,etWeight,etDescription)
        }
    }

    private fun emptyCheck(): Boolean{
        return (etWeight.toString().isEmpty() || etHeight.toString().isEmpty() || etDescription.isEmpty())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment perInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}