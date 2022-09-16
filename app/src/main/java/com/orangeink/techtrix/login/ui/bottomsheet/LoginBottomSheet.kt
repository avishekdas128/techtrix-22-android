package com.orangeink.techtrix.login.ui.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.IntentSender
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orangeink.design.RoundedBottomSheet
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.BottomsheetLoginBinding
import com.orangeink.techtrix.login.viewmodel.LoginViewModel
import com.orangeink.common.preferences.Prefs
import com.orangeink.utils.hideKeyboard
import com.orangeink.utils.isValidEmail
import com.orangeink.utils.pxToDp
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginBottomSheet : RoundedBottomSheet() {

    private lateinit var binding: BottomsheetLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var loginInterface: LoginInterface

    interface LoginInterface {
        fun profileSetupNeeded(user: FirebaseUser)

        fun onLoginCompleted()
    }

    fun setData(loginInterface: LoginInterface) {
        this.loginInterface = loginInterface
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { mDialog: DialogInterface ->
            val d = mDialog as BottomSheetDialog
            val containerLayout =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.container)
            val parent = binding.rlBottom.parent as ViewGroup
            parent.removeView(binding.rlBottom)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                requireContext().pxToDp(48f)
            )
            layoutParams.gravity = Gravity.BOTTOM
            containerLayout?.addView(binding.rlBottom, containerLayout.childCount)
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isDraggable = false
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        setListeners()
        setupGoogleLogin()
        subscribeToLiveData()
    }

    private fun setListeners() {
        binding.btnGoogle.setOnClickListener {
            binding.progressLogin.visibility = View.VISIBLE
            oneTapClient.beginSignIn(signInRequest).addOnSuccessListener {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
                    startForResult.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Timber.e("Couldn't start One Tap UI: ${e.localizedMessage}")
                    binding.progressLogin.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "No google accounts found!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Timber.e(it)
                binding.progressLogin.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.server_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.etPassword.setOnEditorActionListener { _, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN) {
                login()
            }
            false
        }
        binding.btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        hideKeyboard(requireActivity())
        if (binding.etEmail.text.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Email cannot be empty!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (binding.etPassword.text.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Password cannot be empty!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!binding.etEmail.text.toString().isValidEmail()) {
            Toast.makeText(
                requireContext(),
                "Invalid email!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (binding.etPassword.text.length < 6) {
            Toast.makeText(
                requireContext(),
                "Password must be at least 6 characters long!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        binding.progressLogin.visibility = View.VISIBLE
        viewModel.loginWithEmail(
            auth,
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
    }

    private fun subscribeToLiveData() {
        viewModel.user.observe(viewLifecycleOwner) {
            binding.progressLogin.visibility = View.GONE
            loginInterface.profileSetupNeeded(it)
            dismiss()
        }
        viewModel.participant.observe(viewLifecycleOwner) {
            binding.progressLogin.visibility = View.GONE
            Prefs(requireContext()).user = it
            loginInterface.onLoginCompleted()
            dismiss()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressLogin.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupGoogleLogin() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.google_sign_in_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val googleCredential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = googleCredential.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        val firebaseCredential =
                            GoogleAuthProvider.getCredential(idToken, null)
                        viewModel.loginWithGoogle(auth, firebaseCredential)
                    }
                    else -> binding.progressLogin.visibility = View.GONE
                }
            } else binding.progressLogin.visibility = View.GONE
        }
}