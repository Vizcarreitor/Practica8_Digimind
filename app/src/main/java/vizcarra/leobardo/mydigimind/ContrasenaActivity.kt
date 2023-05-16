package vizcarra.leobardo.mydigimind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ContrasenaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrasena)

        var auth = Firebase.auth

        val btn_restablecer: Button = findViewById(R.id.btn_restablecer)

        btn_restablecer.setOnClickListener {

            val et_correo: EditText = findViewById(R.id.et_correo_cont)
            var correo: String = et_correo.text.toString()

            if (!correo.isNullOrBlank()) {

                auth.sendPasswordResetEmail(correo).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Se envió un correo a $correo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al enviar correo", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "Ingresar correo", Toast.LENGTH_SHORT).show()
            }
        }

    }
}