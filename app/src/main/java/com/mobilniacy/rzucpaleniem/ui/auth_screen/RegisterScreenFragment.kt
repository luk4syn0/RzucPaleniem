package com.mobilniacy.rzucpaleniem.ui.auth_screen


//import com.example.rzucpaleniem.AuthHelper

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import apacheFileHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.mobilniacy.rzucpaleniem.MainActivity
import com.mobilniacy.rzucpaleniem.R
import com.mobilniacy.rzucpaleniem.databinding.FragmentRegisterScreenBinding
import java.text.SimpleDateFormat
import java.util.*


class RegisterScreenFragment : Fragment() {

    private var _binding: FragmentRegisterScreenBinding? = null

    //test
    lateinit var aph : apacheFileHelper
    //test

    private val binding get() = _binding!!

    lateinit var btnDatePicker: FloatingActionButton

    //Kredensy
    private var email: String = ""
    private var uname: String = ""
    private var password: String = ""
    private var birthdate: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val registerScreenViewModel =
//            ViewModelProvider(this)[RegisterScreenViewModel::class.java]

        _binding = FragmentRegisterScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //test
        // TODO: firebase
        aph = apacheFileHelper()
        //test

        // PO CO TO JEST?
//        val textView: TextView = binding.textView2
//        registerScreenViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnDatePicker = requireView().findViewById(R.id.floatingActionButton)

        // when floating action button is clicked
        btnDatePicker.setOnClickListener {
            // Initiate date picker with
            // MaterialDatePicker.Builder.datePicker()
            // and build it using build()
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                Toast.makeText(context, "$date is selected", Toast.LENGTH_LONG).show()

                val outlinedTextBirthDay =
                    activity?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterBirthDate)

                outlinedTextBirthDay?.editText?.setText(dateFormatter.format(Date(it)))
            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(
                    context,
                    "${datePicker.headerText} is cancelled",
                    Toast.LENGTH_LONG
                ).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(context, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }
        // Disabling manual input in TextInputEditText
        val outlinedTextBirthDay =
            activity?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterBirthDate)

        outlinedTextBirthDay?.editText?.setOnClickListener {
            // Show date picker dialog when TextInputEditText is clicked
            btnDatePicker.performClick()
//            Toast.makeText(context, "Klik klik", Toast.LENGTH_LONG).show()
        }

        // ClickListener dla przycisku confirm dla rejestracji
        // Korzysta z akcji zdefiniowanej w navigation mobile
        val confirmTextView = view.findViewById<TextView>(R.id.textViewConfirm)
        confirmTextView?.setOnClickListener() {


            /*TODO: ogarnąć sprawdzanie warunków dla każdego pola rejestracji i w razie braku lub nieprawidłowości zaznaczyć odpowienie pole
            *  /ustawić fokus na dany element który się nie zgadza oraz wyświetlić komunikat w formie Toast.makeText albo na stałe w okienku pod
            *  nieprawidłowym czymś VVV*/

            checkCredentials()

            //NAZWA UŻYTKOWNIKA -


            //Jeżeli wszystkie warunki spełnione i porozumienie z Firebase zostało ustanowione i potwierdzone
            //Przenieść użytkownika do aplikacji


//            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_authScreen_to_titleScreen)
//            Toast.makeText(context, "Text kliknięty", Toast.LENGTH_LONG).show()
        }

        //TODO: Dodać listenery dla przycisków dotyczących prywatności oraz TOS aby coś wyświetlały
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Ukrycie dolnego nav bar
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.visibility = View.GONE

        // Dla paska narzędzi (Toolbar)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.hide()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Funkce pomocnicze dla sprawdzania pól rejestracji
    private fun checkCredentials(){
        if (emptyEMail()) {
            return
        } else if (emptyUname()) {
            return
        } else if (emptyPswd()){
            return
        } else if (emptyPswd2()){
            return
        } else if (emptyBirthDate()){
            return
        } else if (emptyAgreements()){
            return
        } else if (email.isNotEmpty() and password.isNotEmpty()) {
        //Ogarniamy dodawanie do bazy danych użytkowników
            (activity as MainActivity).auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((activity as MainActivity)) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
//                        val user = (activity as MainActivity).auth.currentUser
//                        updateUI(user)
                        goToTitle()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            task.exception?.message ?: "createUserWithEmail:failure",
                            Toast.LENGTH_LONG,
                        ).show()
//                        updateUI(null)

                    }
                }


        } else {
            Toast.makeText(
                context,
                "|"+password+"|oraz|"+email+"|",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }


    }

    private fun goToTitle(){
        if (checkIfSignedIn()){
        requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            .navigate(R.id.action_registerScreen_to_titleScreen)
        } else {
            Toast.makeText(
                context,
                "Wystąpił problem z sesją, powrót do okna wyboru",
                Toast.LENGTH_SHORT,
            ).show()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
                .navigate(R.id.action_registerScreen_to_authScreen)
        }
    }

    private fun checkIfSignedIn(): Boolean {
        if ((activity as MainActivity).auth.currentUser != null) {
            return true
        }else{
            return false
        }
    }

    private fun emptyUname(verbose: Boolean = true): Boolean {
        //Verbose - tryb gadatliwy - toast na ekranie dobre dla informowania, ale niedobre dla
        //sprawdzania w tle
        val tekst = view?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterUname)?.editText?.text.toString()
        if (tekst.isNotEmpty()){
            uname = tekst
            return false
        } else {
            if(verbose){
                Toast.makeText(context, "Pole login puste", Toast.LENGTH_LONG).show()
            }
            return true
        }
    }

    private fun emptyPswd(verbose: Boolean = true): Boolean {
        //Verbose - tryb gadatliwy - toast na ekranie dobre dla informowania, ale niedobre dla
        //sprawdzania w tle
        val tekst = view?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterPassword)?.editText?.text.toString()
        if (tekst.isNotEmpty()
        ) {
            password = tekst
            return false
        } else {
            if (verbose) {
                Toast.makeText(context, "Pole hasło puste", Toast.LENGTH_LONG).show()
            }
            return true
        }
    }
    private fun emptyPswd2(verbose: Boolean = true): Boolean {
        //Verbose - tryb gadatliwy - toast na ekranie dobre dla informowania, ale niedobre dla
        //sprawdzania w tle
        if (view?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterPasswordConfirm)?.editText?.text.toString()
                .isNotEmpty()
        ) {
            return false
        } else {
            if (verbose) {
                Toast.makeText(context, "Pole potwierdź hasło puste", Toast.LENGTH_LONG).show()
            }
            return true
        }
    }

    private fun emptyBirthDate(verbose: Boolean = true): Boolean {
        //Verbose - tryb gadatliwy - toast na ekranie dobre dla informowania, ale niedobre dla
        //sprawdzania w tle
        if (view?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterBirthDate)?.editText?.text.toString()
                .isNotEmpty()
        ) {
            return false
        } else {
            if (verbose) {
                Toast.makeText(context, "Pole data urodzenia puste", Toast.LENGTH_LONG).show()
            }
            return true
        }
    }

    private fun emptyAgreements(verbose: Boolean = true): Boolean {
        //Verbose - tryb gadatliwy - toast na ekranie dobre dla informowania, ale niedobre dla
        //sprawdzania w tle

        val checkBoxPrivacy = view?.findViewById<CheckBox>(R.id.checkBoxPrivacy)
        val checkBoxTOS = view?.findViewById<CheckBox>(R.id.checkBoxTOS)
        if (checkBoxPrivacy != null) {
            if (checkBoxTOS != null) {
                return if (checkBoxPrivacy.isChecked and checkBoxTOS.isChecked) {
                    false
                } else {
                    if (verbose) {
                        Toast.makeText(
                            context,
                            "Wyraź zgodę na TOS oraz politykę prywatności",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    true
                }
            }
        }; return true
    }

    private fun emptyEMail(verbose: Boolean = true): Boolean {
        //Verbose - tryb gadatliwy - toast na ekranie dobre dla informowania, ale niedobre dla
        //sprawdzania w tle
        val tekst = view?.findViewById<TextInputLayout>(R.id.outlinedTextFieldRegisterMail)?.editText?.text.toString()
        if (tekst.isNotEmpty()
        ) {
            email = tekst
            return false
        } else {
            if (verbose) {
                Toast.makeText(context, "Pole E-Mail puste", Toast.LENGTH_LONG).show()
            }
            return true
        }
    }

}