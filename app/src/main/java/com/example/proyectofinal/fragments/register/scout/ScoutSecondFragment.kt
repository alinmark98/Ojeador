package com.example.proyectofinal.fragments.register.scout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.RegisterViewModel
import com.example.proyectofinal.viewmodels.TeamsViewModel
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [perInfo.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutSecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var sendDataFromFragment: ScoutSendDataFromF2? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var spinRegion: Spinner
    private lateinit var etLocation: EditText
    private lateinit var spinCity: Spinner
    private lateinit var spinTeam: Spinner
    private lateinit var teamsViewModel: TeamsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        teamsViewModel = ViewModelProvider(this)[TeamsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scout_second, container, false)
        spinRegion = view.findViewById(R.id.regionSpinner)
        spinCity = view.findViewById(R.id.citySpinner)
        spinTeam = view.findViewById(R.id.teamSpinner)
        etLocation = view.findViewById(R.id.etLocation)

        val regionsObserver = Observer<List<String>> { regions ->
            // Configurar el primer spinner con las regiones obtenidas
            val regionsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regions)
            spinRegion.adapter = regionsAdapter
        }

        val citiesObserver = Observer<List<String>> { cities ->
            // Configurar el segundo spinner con las ciudades obtenidas
            val citiesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
            spinCity.adapter = citiesAdapter
        }

        val teamsObserver = Observer<List<String>> { teams ->
            // Configurar el tercer spinner con los equipos obtenidos
            val teamsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, teams)
            spinTeam.adapter = teamsAdapter
        }

        teamsViewModel.getRegions().observe(viewLifecycleOwner, regionsObserver)

        spinRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRegion = parent.getItemAtPosition(position) as String
                teamsViewModel.getCities(selectedRegion).observe(viewLifecycleOwner, citiesObserver)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar la selección vacía del primer spinner
            }
        }
        spinCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRegion = spinRegion.selectedItem as String
                val selectedCity = parent.getItemAtPosition(position) as String
                teamsViewModel.getTeams(selectedRegion, selectedCity).observe(viewLifecycleOwner, teamsObserver)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar la selección vacía del segundo spinner
            }
        }


        etLocation.setOnClickListener {
            val fields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        return view
    }


    interface ScoutSendDataFromF2 : ScoutFirstFragment.ScoutSendDataFromF1 {
        fun scoutSendDataSecondFragment(
            team: String?, location: String?,
            secondFragConfirmed: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            sendDataFromFragment = activity as ScoutSendDataFromF2?
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }
    }

    override fun onPause() {
        super.onPause()
        if(!isLocationEmpty()){
            sendDataFromFragment?.scoutSendDataSecondFragment(spinTeam.selectedItem.toString(),etLocation.text.toString(), true)
            Log.d("SC-FRAG2", "LOCATION NOT EMPTY")
        }else{
            Log.d("SC-FRAG2", "LOCATION EMPTY")
            Toast.makeText(context, "Check fields", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isLocationEmpty(): Boolean{
        return etLocation.text.toString().isEmpty()
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
         * @return A new instance of fragment perInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScoutSecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}