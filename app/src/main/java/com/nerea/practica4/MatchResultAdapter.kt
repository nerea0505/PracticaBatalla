package com.nerea.practica4

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

class MatchResultAdapter(private val matchResults: List<MatchResult>) :
    RecyclerView.Adapter<MatchResultAdapter.MatchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match_result, parent, false)
        return MatchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchResultViewHolder, position: Int) {
        val matchResult = matchResults[position]
        holder.bind(matchResult)
    }

    override fun getItemCount(): Int {
        return matchResults.size
    }

    class MatchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgResult: ImageView = itemView.findViewById(R.id.imgResult)
        private val txtResult: TextView = itemView.findViewById(R.id.txtResult)
        private val txtCharacters: TextView = itemView.findViewById(R.id.txtCharacters)
        private val txtDate: TextView = itemView.findViewById(R.id.txtDate)

        fun bind(matchResult: MatchResult) {
            imgResult.setImageResource(matchResult.resultImage)
            txtResult.text = matchResult.result
            txtCharacters.text = matchResult.characters
            txtDate.text = "Fecha: ${matchResult.date}"
        }
    }
}
