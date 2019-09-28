package bwki.deepblossom.blumenidentifikator.resultscreen

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bwki.deepblossom.blumenidentifikator.R
import bwki.deepblossom.blumenidentifikator.Result
import bwki.deepblossom.blumenidentifikator.res


/**
 * RecyclerView Klasse ist für die Anzeige der Ergebnisse verantwortlich.
 */

class ResultScreenAdapter : RecyclerView.Adapter<ResultScreenAdapter.ViewHolder>() {
    var data = listOf<Result>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ResultScreenAdapter", "onBindViewHolder Called")
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("ResultScreenAdapter", "onCreateViewHolder Called")
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Hier werden den IDs den Beispielbildern zugeordnet
        private val imagePreview = mapOf(
            0 to R.drawable.alpineseaholly,
            1 to R.drawable.anthurium,
            2 to R.drawable.artichoke,
            3 to R.drawable.azalea, // sind verschiedene Srten und Sorten der Gattung Rhododendron
            4 to R.drawable.ballmoss, // funktioniert nicht! keine richtige klassifizierung! Mooskugel Algenart
            5 to R.drawable.balloonflower, //anscheinend mehrere Farben
            6 to R.drawable.barbertondaisie, // nur die gelbe funktioniert!
            7 to R.drawable.beardediris, // Pflanzengattung
            8 to R.drawable.beebalm,
            9 to R.drawable.birdofparadise,
            10 to R.drawable.bishopofllandaff,
            11 to R.drawable.blackeyedsusan,
            12 to R.drawable.blackberrylily,
            13 to R.drawable.blanketflower,
            14 to R.drawable.bolerodeepblue,
            15 to R.drawable.bougainviella,
            16 to R.drawable.bromelia,
            17 to R.drawable.buttercup,
            18 to R.drawable.californianpoppy,
            19 to R.drawable.camellia, // etwas ungenau
            20 to R.drawable.cannalily,
            21 to R.drawable.canterburybells,
            22 to R.drawable.image_02063, // Dummy
            // 22 to capeflower // finde ich nicht
            23 to R.drawable.carnation, // Gattung
            24 to R.drawable.cautleyaspicata, // keine richtige klassifizierung
            25 to R.drawable.clematis, // funktioniert nur mit der lilalen // Gattung
            26 to R.drawable.coltsfoot,
            27 to R.drawable.columbine, // etwas unzuverlässig // Gattung
            28 to R.drawable.commondandelion,
            29 to R.drawable.cornpoppy,
            30 to R.drawable.cyclamen, // unzuverlässig // Gattung
            31 to R.drawable.daffodil, // Gattung
            32 to R.drawable.desertrose, // Gattung
            33 to R.drawable.englishmarigold, // funktioniert nur mit der orangenen gut
            34 to R.drawable.firelily, // unzuverlässig
            35 to R.drawable.foxglove, // etwas unzuverlässig // Gattung
            36 to R.drawable.frangipani,
            37 to R.drawable.fritillary, // Gattung
            38 to R.drawable.gardenphlox,
            39 to R.drawable.gaura,
            40 to R.drawable.gazania, // Eigentlich Gazania
            41 to R.drawable.geranium, // unzuverlässig // Gattung
            42 to R.drawable.giantwhitearumlily, //??
            43 to R.drawable.globeflower, // Gattung // unzuverlässig
            44 to R.drawable.globethistle,
            45 to R.drawable.grapehyacinth, // Gattung
            46 to R.drawable.greatmasterwort,
            47 to R.drawable.hardleavedpocketorchid, // etwas unzuverlässig
            48 to R.drawable.hibiscus, // Gattung
            49 to R.drawable.hippeastrum, // nur die pinke wird erkannt. es gibt verschiedene hippeastrum // Gattung
            50 to R.drawable.japaneseanemone, // nur die pinke funktioniert
            51 to R.drawable.kingprotea,
            52 to R.drawable.lentenrose, // unzuverlässig
            53 to R.drawable.lotus, // etwas unzuverlässig // Gattung
            54 to R.drawable.lotusinthemist,
            55 to R.drawable.magnolia, // funktioniert nicht // Gattung
            56 to R.drawable.mallow, // funktioniert nicht
            57 to R.drawable.marigold, // nur gelbe funktionieren
            58 to R.drawable.mexican_aster,
            59 to R.drawable.mexican_petunia,
            60 to R.drawable.monkshood, // etwas unzuverlässig // Gattung
            61 to R.drawable.moon_orchid,
            62 to R.drawable.morning_glory, // GROßE GATTUNG!!
            63 to R.drawable.image_02063, // Dummy
            // 63 to orange dahlia find ich net
            64 to R.drawable.osteospermum, // es erkennt nur daisybush,eine Art von osteospermum // Gattung
            65 to R.drawable.oxeye_daisy,
            66 to R.drawable.passionflower, // Gattung
            67 to R.drawable.pelargonia, // Gattung
            68 to R.drawable.image_02063, // Dummy
            // 68 to R.drawable.pervuvian lily, // Gattung
            69 to R.drawable.petunia, // Gattung
            70 to R.drawable.pincushionflower,
            71 to R.drawable.image_02063, // Dummy
            // 71 to pinkyellowdahlia
            72 to R.drawable.pinkprimrose,
            73 to R.drawable.poinsettia,
            74 to R.drawable.primula, // Gattung
            75 to R.drawable.princeofwalesfeathres,
            76 to R.drawable.purpleconeflower,
            77 to R.drawable.alpiniapurupurata,
            78 to R.drawable.rose, // Gattung
            79 to R.drawable.rubylippedcattleya, // unzuverlässig
            80 to R.drawable.siamtulip,
            81 to R.drawable.silverbush,
            82 to R.drawable.snapdragon, // unzuverlässig Gattung
            83 to R.drawable.spearthistle,
            84 to R.drawable.springcrocus, // unzuverlässig
            85 to R.drawable.stemlessgentian,
            86 to R.drawable.sunflower,
            87 to R.drawable.sweetpea,
            88 to R.drawable.sweetwilliam,
            89 to R.drawable.image_02063, // Dummy
            // 89 to R.drawable.swordlily // Gattung
            90 to R.drawable.thornapple, // Nur Blütenform funktioniert
            91 to R.drawable.tigerlily, // unzuverlässig
            92 to R.drawable.toadlily,
            93 to R.drawable.treemallow, // unzuverlässig
            94 to R.drawable.treepoppy,
            95 to R.drawable.trumpetcreeper,
            96 to R.drawable.wallflower, // unzuverlässig // Gattung
            97 to R.drawable.waterlily, //Familie -> Probleme
            98 to R.drawable.watercress, // funktioniert nicht
            99 to R.drawable.wildpansy,
            100 to R.drawable.windflower,
            101 to R.drawable.yellowiris
        )

        private val resultName: TextView = itemView.findViewById(R.id.result_name)
        private val resultWissName: TextView = itemView.findViewById(R.id.result_wissName)
        private val resultConf: TextView = itemView.findViewById(R.id.result_confidence)
        private val resultProgress: ProgressBar = itemView.findViewById(R.id.result_progress)
        private val resultPicture: ImageView = itemView.findViewById(R.id.result_picture)

        fun bind(
            item: Result
        ) {
            Log.d("ResultScreenAdapter", "ViewHolder Binder called")
            resultName.text =
                String.format(res.getString(R.string.name), item.name).replace("'", "")
            resultWissName.text = String.format(
                res.getString(
                    R.string.wissName
                ), item.wissName
            ).replace("'", "")
            Log.d("ResultScreenAdapter", item.confidence.toString())
            val confidence: String = (item.confidence * 100).toInt().toString() + "%"
            resultConf.text = String.format(res.getString(R.string.confidence), confidence)
            resultProgress.progress = (item.confidence * 100).toInt()

            resultPicture.setImageResource(imagePreview.getOrElse(item.id.toInt()) {
                Log.e(
                    "ResultScreenAdapter",
                    "Failed picture"
                )
            }.toInt())
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_results, parent, false)
                return ViewHolder(
                    view
                )
            }
        }
    }
}
