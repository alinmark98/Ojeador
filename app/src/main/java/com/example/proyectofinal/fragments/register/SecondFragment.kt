package com.example.proyectofinal.fragments.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import com.example.proyectofinal.R

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
    private var sendDataFromFragment: SendDataFromFragment2? = null

    private lateinit var spinGender: Spinner
    private lateinit var spinPosition: Spinner
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var etDescription: AppCompatEditText


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
        spinGender = view.findViewById(R.id.genderSpinner)
        spinPosition = view.findViewById(R.id.positionSpinner)

        etHeight = view.findViewById(R.id.etHeight)
        etWeight = view.findViewById(R.id.etWeight)
        etDescription = view.findViewById(R.id.etDescription)

        return view
    }

    interface SendDataFromFragment2 : FirstFragment.SendDataFromFragment1 {
        fun sendDataSecondFragment(
            gender: String?, position: String?,
            height: Double?, weight: Double?,
            description: String?, secondFragConfirmed: Boolean)
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
            sendDataFromFragment?.sendDataSecondFragment(spinGender.selectedItem.toString(),
                spinPosition.selectedItem.toString(),etHeight.text.toString().toDoubleOrNull(),
                etHeight.text.toString().toDoubleOrNull(),etDescription.text.toString(),true)
            Log.d("DENTRO", "DENTRO")
        }else{
            Log.d("FUERA", "FUERA")
            Toast.makeText(context, "Check fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun emptyCheck(): Boolean {
        return etDescription.text.toString().isEmpty()
                || etWeight.text.toString().isEmpty()
                || etHeight.text.toString().isEmpty()
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