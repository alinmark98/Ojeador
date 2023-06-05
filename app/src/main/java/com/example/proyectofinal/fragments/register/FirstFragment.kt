package com.example.proyectofinal.fragments.register

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.RegisterViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [accInfo.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var registerViewModel: RegisterViewModel
    private var sendDataFromFragment: SendDataFromFragment1? = null
    private lateinit var etLocation: EditText
    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordCheck: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        Places.initialize(requireContext(), "[GOOGLE API KEY]")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        val icon_check: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
        val icon_error: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)

        etLocation = view.findViewById(R.id.etLocation)
        etName = view.findViewById(R.id.etName)
        etSurname = view.findViewById(R.id.etSurname)
        etBirthdate = view.findViewById(R.id.etBirthdate)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etPasswordCheck = view.findViewById(R.id.etPasswordCheck)

        registerViewModel.showDatePicker.observe(viewLifecycleOwner) { showDatePicker ->
            if (showDatePicker) {
                showDatePickerDialog()
            }
        }

        etLocation.setOnClickListener {
            val fields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        etEmail.addTextChangedListener { email ->
            val emailString = email.toString().trim()
            registerViewModel.checkEmailExists(emailString) { emailExists ->
                if (emailExists) {
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_error, null)
                } else {
                    if(emailCheckPattern()){
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_check, null)
                    }else{
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_error, null)
                    }
                }
            }
        }


        etBirthdate.setOnClickListener {
            registerViewModel.onBirthdayEditTextClicked()
        }

        return view
    }

    interface SendDataFromFragment1 {
        fun sendDataFirstFragment(name: String?, surname: String?,
                                  birthday: String?, email: String?,
                                  password: String?, location: String?,
                                  firstFragConfirmed: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            sendDataFromFragment = activity as SendDataFromFragment1?
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${monthOfYear + 1}/${year}"
                etBirthdate.setText(selectedDate)
                registerViewModel.onDatePickerDismissed()
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.setOnDismissListener {
            registerViewModel.onDatePickerDismissed()
        }

        datePickerDialog.show()
    }

    override fun onPause() {
        super.onPause()
        Log.d("PAUSA", "PAUSA")
        //!emailCheck() && passCheck()
        if(!emptyCheck()){
            if(emailCheckPattern()) {
                if (passCheck()) {
                    sendDataFromFragment?.sendDataFirstFragment(etName.text.toString(),etSurname.text.toString(),
                        etBirthdate.text.toString(),etEmail.text.toString(),etPassword.text.toString(),
                        etLocation.text.toString(), true)
                }else{
                    Toast.makeText(context, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "El correo no es válido", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }

        /*
        Toast.makeText(context, "Check fields", Toast.LENGTH_SHORT).show()
        Log.d("FIRST1FRAGMENT", "CAMPOS VACIO")*/
    }

    private fun emptyCheck(): Boolean{
        return (etEmail.text.isEmpty() || etPassword.text.isEmpty() || etPasswordCheck.text.isEmpty() ||
                etBirthdate.text.isEmpty() || etName.text.isEmpty() || etSurname.text.isEmpty() ||
                etLocation.text.isEmpty())
    }

    private fun passCheck(): Boolean{
        return etPassword.text.toString().trim() == etPasswordCheck.text.toString().trim()
                && etPassword.text.toString().trim().length >= 8
    }

    private fun emailCheckPattern(): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            etLocation.setText(place.address)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment accInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
