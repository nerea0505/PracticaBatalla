package com.nerea.practica4

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MatchGameAdapter(
    private val gameList: List<MatchGame>,
    private val context: Context  // Recibimos el contexto en el adaptador
) : RecyclerView.Adapter<MatchGameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvAtaque: TextView = view.findViewById(R.id.tvAtaque)
        val tvVida: TextView = view.findViewById(R.id.tvVida)
        val tvNacimiento: TextView = view.findViewById(R.id.tvF_nacimiento)
        val imgPersonaje: ImageView = view.findViewById(R.id.imgPersonaje)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_match_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val match = gameList[position]
        holder.tvNombre.text = match.nombre
        holder.tvAtaque.text = match.ataque.toString()
        holder.tvVida.text = match.vida.toString()
        holder.tvNacimiento.text = match.date
        holder.imgPersonaje.setImageResource(R.drawable.ic_launcher_background)  // Asegúrate de cambiar esto a tu imagen

        // Acción al hacer click en un personaje
        holder.itemView.setOnClickListener {
            // Enlazar la acción para abrir los detalles del personaje
            val intent = Intent(context, DetallesPersonajeActivity::class.java)
            intent.putExtra("nombre_personaje", match.nombre)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = gameList.size
}
