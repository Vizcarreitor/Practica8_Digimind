package vizcarra.leobardo.mydigimind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

public class AdaptadorTareas : BaseAdapter {
    var tasks = ArrayList<Task>()
    var context: Context? = null

    constructor(contexto: Context, tasks: ArrayList<Task>) {
        this.context = contexto
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
        var inflador = LayoutInflater.from(this.context)
        var vista = inflador.inflate(R.layout.task_view, null)

        val titulo: TextView = vista.findViewById(R.id.title)
        val tiempo: TextView = vista.findViewById(R.id.time)
        val dias: TextView = vista.findViewById(R.id.days)

        titulo.setText(task.title)
        tiempo.setText(task.time)
        dias.setText(task.days.toString())

        return vista
    }
}