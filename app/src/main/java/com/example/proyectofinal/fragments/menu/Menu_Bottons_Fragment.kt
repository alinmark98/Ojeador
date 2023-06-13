package com.example.proyectofinal.fragments.menu

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.proyectofinal.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var buttonClickListener: Menu_Bottons_Fragment.OnButtonClickListener? = null

/**
 * A simple [Fragment] subclass.
 * Use the [Menu_Bottons_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Menu_Bottons_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    interface OnButtonClickListener {
        fun onButtonClick(buttonId: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu__bottons_, container, false)
        val button1 = view.findViewById<Button>(R.id.btn1)
        val button2 = view.findViewById<Button>(R.id.btn2)
        val button3 = view.findViewById<Button>(R.id.btn3)
        val button4 = view.findViewById<Button>(R.id.btn4)

        button1.setOnClickListener {
            buttonClickListener?.onButtonClick(R.id.button1)
        }

        button2.setOnClickListener {
            buttonClickListener?.onButtonClick(R.id.button2)
        }

        button3.setOnClickListener {
            buttonClickListener?.onButtonClick(R.id.button3)
        }

        button4.setOnClickListener {
            buttonClickListener?.onButtonClick(R.id.button4)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClickListener) {
            buttonClickListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        buttonClickListener = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Menu_Bottons_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Menu_Bottons_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}