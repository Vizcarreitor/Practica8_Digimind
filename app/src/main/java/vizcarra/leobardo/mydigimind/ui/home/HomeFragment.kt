package vizcarra.leobardo.mydigimind.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vizcarra.leobardo.mydigimind.R
import vizcarra.leobardo.mydigimind.databinding.FragmentHomeBinding
import vizcarra.leobardo.mydigimind.ui.Task

class HomeFragment : Fragment() {
    private var adaptador: AdaptadorTareas? = null
    private lateinit var homeViewModel: HomeViewModel

    companion object{
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

        if (first){
            fillTasks()
            first = false
        }

        adaptador = AdaptadorTareas(root.context, tasks)
        val gridView: GridView = root.findViewById(R.id.reminders)

        gridView.adapter = adaptador

        return root
    }

    fun fillTasks(){
        tasks.add(Task("Practice 1", arrayListOf("Monday", "Sunday"), "17:30"))
        tasks.add(Task("Practice 2", arrayListOf("Tuesday"), "12:30"))
        tasks.add(Task("Practice 3", arrayListOf("Thursday", "Monday"), "12:00"))
        tasks.add(Task("Practice 4", arrayListOf("Friday"), "20:00"))
        tasks.add(Task("Practice 5", arrayListOf("Thursday", "Friday"), "6:30"))
        tasks.add(Task("Practice 6", arrayListOf("Sunday"), "11:45"))
        tasks.add(Task("Practice 7", arrayListOf("Saturday"), "15:30"))
    }

    private class AdaptadorTareas: BaseAdapter {

        var tasks = ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>){
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