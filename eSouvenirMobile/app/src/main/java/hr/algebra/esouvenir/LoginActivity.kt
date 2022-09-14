package hr.algebra.esouvenir

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.esouvenir.api.LoginFetcher
import hr.algebra.esouvenir.api.RegisterFetcher
import hr.algebra.esouvenir.databinding.ActivityLoginBinding
import hr.algebra.esouvenir.framework.callDelayed
import hr.algebra.esouvenir.framework.setIntPreference
import hr.algebra.esouvenir.framework.setStringPreference
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DELAY = 4500L
const val USER_ID = "hr.algebra.esouvenir.userId"
const val TOKEN = "hr.algebra.esouvenir.token"
const val TOKEN_VALID_TIME = "hr.algebra.esouvenir.tokenValidTime"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var ll: LinearLayout
    private lateinit var tvRegister: TextView

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title=""

        setStub()
        tvRegister = findViewById<TextView>(R.id.tvRedirectRegister)
        ll = findViewById<LinearLayout>(R.id.ll)

        setLoginLayout()

        //setRegisterLayout()



    }

    private fun setLoginLayout() {


        val btnLogin = findViewById<Button>(R.id.btnLogin)

        tvRegister.setOnClickListener {
            ll.removeAllViews()
            ll.addView(LayoutInflater.from(this).inflate(R.layout.layout_register, ll, false))
            setRegisterLayout()
        }
        btnLogin.setOnClickListener {
            username = findViewById<EditText>(R.id.usernameLogin)
            password = findViewById<EditText>(R.id.passwordLogin)
            if (username.text.isEmpty() || password.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Complete all fields in order to login!",
                    Toast.LENGTH_SHORT
                ).show()
                when {
                    username.text.isEmpty() -> username.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.required,
                        0
                    )
                    password.text.isEmpty() -> password.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.required,
                        0
                    )
                }
                if (username.text.isNotEmpty())
                    username.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                else if (password.text.isNotEmpty())
                    password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            } else {
                val user: User = User(
                    null,
                    username.text.toString(),
                    null,
                    password.text.toString(),
                    false,
                    null
                )
                loginUser(user)
            }
        }
    }

    private fun setRegisterLayout() {
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        tvLogin.setOnClickListener {
            Toast.makeText(applicationContext, "Na loginu sam!", Toast.LENGTH_SHORT).show()
            ll.removeAllViews()
            ll.addView(LayoutInflater.from(this).inflate(R.layout.layout_login, ll, false))
        }


            val btnRegister = findViewById<Button>(R.id.btnRegister)
            btnRegister.setOnClickListener {
                etUsername = findViewById<EditText>(R.id.etUsername)
                etEmail = findViewById<EditText>(R.id.etEmail)
                etPass = findViewById<EditText>(R.id.etPass)
                if (etUsername.text.isEmpty() || etEmail.text.isEmpty()|| etPass.text.isEmpty()) {
                    Toast.makeText(applicationContext, "Complete all fields in order to register!", Toast.LENGTH_SHORT).show()
                    when {
                        etUsername.text.isEmpty() -> etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.required, 0)
                        etEmail.text.isEmpty() -> etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.required, 0)
                        etPass.text.isEmpty() -> etPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.required, 0)
                    }
                } else {
                    val user: User = User(null, etUsername.text.toString(),etEmail.text.toString(), etPass.text.toString(), false, null)
                    registerUser(user)
                }



            //redirectLogin()

        }
    }

    private fun registerUser(user: User) {


        RegisterFetcher(this).insertUser(user) {
            if (it!= null && it >= 0) {
                val id = it

                Toast.makeText(applicationContext, "Registered successfully!", Toast.LENGTH_SHORT).show()
                //Toast.makeText(applicationContext, "Logging in...", Toast.LENGTH_LONG).show()

                callDelayed(DELAY) {startActivity<LoginActivity>()}
                ll.addView(LayoutInflater.from(this).inflate(R.layout.layout_login, ll, false))

            } else {
                if (it == -1) {
                    Log.d(javaClass.name, "Error registering new user")
                    Toast.makeText(applicationContext, "Username is already taken!", Toast.LENGTH_SHORT).show()
                    etUsername.background.setTint(getColor(R.color.redColor))

                }
                else if (it == -2) {
                    Log.d(javaClass.name, "Error registering new user")
                    Toast.makeText(applicationContext, "User with this email already exists!", Toast.LENGTH_SHORT).show()
                    etEmail.setBackgroundColor(Color.RED)
                }

            }
        }

    }


    private fun setStub() {

            if (binding.stub.parent != null) {
                binding.stub.inflate()
            } else {
                binding.stub.visibility = View.VISIBLE
            }
    }



    private fun loginUser(user: User) {
        LoginFetcher(this).loginUser(user) {
            if(it != null) {
                if (it.iDUser == -1){
                    Toast.makeText(applicationContext, "Username or password is incorrect!", Toast.LENGTH_SHORT).show()
                    username.background.setTint(getColor(R.color.redColor))
                    password.background.setTint(getColor(R.color.redColor))

                    return@loginUser
                }
                Toast.makeText(applicationContext, "Logging in...", Toast.LENGTH_LONG).show()
                val userLogged : User = User(it.iDUser, it.username, it.email, it.pass, it.isAdmin, it.token)
                if (userLogged != null) {
                    setIntPreference(USER_ID, userLogged._id!!)
                    setStringPreference(TOKEN, userLogged.token!!)

                    val current = LocalDateTime.now().plusDays(1)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    val formatted = current.format(formatter)

                    setStringPreference(TOKEN_VALID_TIME, formatted)

                }

                startActivity<QRScannerActivity>()
            }

        }
    }


}