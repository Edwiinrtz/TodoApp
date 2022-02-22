package com.example.todoapp

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTodoBinding


class ToDoAdapter(private val todos: MutableList<ToDo>) :
    RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {

    fun addTodo(todo: ToDo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDone() {
        todos.removeAll { todo -> todo.isChecked }
        notifyDataSetChanged()
    }

    fun getTodos(): MutableList<ToDo> {
        return todos
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val viewHolderInflated = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_todo,
                parent,
                false
            )
        return TodoViewHolder(viewHolderInflated)
    }

    private fun subrayar(tvItem: TextView, isChecked: Boolean) {
        when (isChecked) {
            true -> tvItem.paintFlags = tvItem.paintFlags or STRIKE_THRU_TEXT_FLAG
            else -> tvItem.paintFlags = tvItem.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val actualItem = todos[position]
        holder.itemView.apply {
            holder.binding.tvItem.text = actualItem.title
            holder.binding.cbDone.isChecked = actualItem.isChecked

            subrayar(holder.binding.tvItem, actualItem.isChecked)
            holder.binding.cbDone.setOnCheckedChangeListener {_,isChecked ->
                subrayar(holder.binding.tvItem, isChecked)
                actualItem.isChecked = !actualItem.isChecked
            }

        }

    }

    override fun getItemCount(): Int {
        return todos.size
    }


    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = ItemTodoBinding.bind(view)
    }

}

