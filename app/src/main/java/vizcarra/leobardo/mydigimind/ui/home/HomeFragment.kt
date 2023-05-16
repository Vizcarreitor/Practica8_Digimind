package vizcarra.leobardo.mydigimind.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import vizcarra.leobardo.mydigimind.R
import vizcarra.leobardo.mydigimind.Task

class HomeFragment : Fragment() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    private var adaptador: AdaptadorTareas? = null
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        var tasks = ArrayList<Task>()
        var first = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        if (first) {
            fillTasks()
            first = false
        }

        adaptador = AdaptadorTareas(root.context, tasks)
        val gridView: GridView = root.findViewById(R.id.reminders)

        gridView.adapter = adaptador

        var txtEmail: TextView = root.findViewById(R.id.tv_correo)
        txtEmail.text = usuario.currentUser?.email.toString()

        var txtDireccion: EditText = root.findViewById(R.id.et_direccion)
        var txtTel: EditText = root.findViewById(R.id.et_telefono)


        var save: Button = root.findViewById(R.id.btn_save)
        save.setOnClickListener {
            storage.collection("usuarios").document(txtEmail.text.toString())
                .set(
                    hashMapOf(
                        "email" to txtEmail.text.toString(),
                        "direccion" to txtDireccion.text.toString(),
                        "telefono" to txtTel.text.toString()
                    )
                ).addOnSuccessListener {
                    Toast.makeText(root.context, "Guardado exitoso", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        root.context,
                        "Guardado fallido" + it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        var delete: Button = root.findViewById(R.id.btn_delete)
        delete.setOnClickListener {
            storage.collection("usuarios").document(txtEmail.text.toString()).delete()
                .addOnSuccessListener {
                    Toast.makeText(root.context, "Eliminado", Toast.LENGTH_SHORT).show()
                }
        }

        val docRef = storage.collection("usuarios").document(txtEmail.text.toString())
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    txtDireccion.setText(document.getString("direccion"))
                    txtTel.setText(document.getString("telefono"))
                } else {
                    Toast.makeText(root.context, "No se encontró", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(root.context, "Falló el get", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    fun fillTasks() {
        //tasks.clear()
        storage.collection("actividades")
            .whereEqualTo("email", usuario.currentUser?.email)
            .get()
            .addOnSuccessListener {
                it.forEach {
                    var dias = ArrayList<String>()
                    if (it.getBoolean("lu") == true) {
                        dias.add("Monday")
                    }
                    if (it.getBoolean("ma") == true) {
                        dias.add("Tuesday")
                    }
                    if (it.getBoolean("mi") == true) {
                        dias.add("Wednesday")
                    }
                    if (it.getBoolean("ju") == true) {
                        dias.add("Thursday")
                    }
                    if (it.getBoolean("vi") == true) {
                        dias.add("Friday")
                    }
                    if (it.getBoolean("sa") == true) {
                        dias.add("Saturday")
                    }
                    if (it.getBoolean("do") == true) {
                        dias.add("Sunday")
                    }

                    var titulo = it.getString("actividad")
                    var tiempo = it.getString("tiempo")

                    var act = Task(titulo!!, dias, tiempo!!)

                    tasks.add(act)
                    //Toast.makeText(context, act.toString(), Toast.LENGTH_SHORT).show()

                }

            }

            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private class AdaptadorTareas : BaseAdapter {

        var tasks = ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>) {
            this.contexto = contexto
            this.tasks = tasks
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(p0: Int): Any {
            return tasks[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var task = tasks[p0]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.task_view, null)

            var title: TextView = vista.findViewById(R.id.title)
            var time: TextView = vista.findViewById(R.id.time)
            var days: TextView = vista.findViewById(R.id.days)

            title.setText(task.title)
            time.setText(task.time)
            days.setText(task.days.toString())

            return vista
        }
    }
}