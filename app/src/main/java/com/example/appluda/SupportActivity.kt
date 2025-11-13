package com.example.appluda

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SupportActivity : AppCompatActivity() {

    // MediaPlayer para reproducir música
    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingIcon: ImageView? = null

    // Breathing Exercise Icons
    private lateinit var iconBreathing: ImageView
    private lateinit var iconMusic: ImageView
    private lateinit var iconRelax: ImageView

    // Sports Icons
    private lateinit var iconRun: ImageView
    private lateinit var iconYoga: ImageView
    private lateinit var iconBike: ImageView

    // Sleep Icons
    private lateinit var iconSleep1: ImageView
    private lateinit var iconSleep2: ImageView
    private lateinit var iconSleep3: ImageView

    // Virtual Companions Icons
    private lateinit var iconVirtual1: ImageView
    private lateinit var iconVirtual2: ImageView
    private lateinit var iconVirtual3: ImageView

    // URLs de música
    private val musicUrls = mapOf(
        "instrumental" to "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        "emotional" to "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
        "relax" to "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
    )

    // IDs de recursos de videos locales (en res/raw/)
    // Los nombres coinciden EXACTAMENTE con tus archivos
    private val videoResources = mapOf(
        "sport_mental" to R.raw.video_mental,  // archivo: video_mental.mp4 ✅
        "yoga" to R.raw.video_yoga,            // archivo: video_yoga.mp4 ✅
        "cardio" to R.raw.video_cardio         // archivo: video_cardio.mp4 ✅
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        // Habilitar botón de retroceso
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        // Breathing Exercise
        iconBreathing = findViewById(R.id.iconBreathing)
        iconMusic = findViewById(R.id.iconMusic)
        iconRelax = findViewById(R.id.iconRelax)

        // Sports
        iconRun = findViewById(R.id.iconRun)
        iconYoga = findViewById(R.id.iconYoga)
        iconBike = findViewById(R.id.iconBike)

        // Sleep
        iconSleep1 = findViewById(R.id.iconSleep1)
        iconSleep2 = findViewById(R.id.iconSleep2)
        iconSleep3 = findViewById(R.id.iconSleep3)

        // Virtual Companions
        iconVirtual1 = findViewById(R.id.iconVirtual1)
        iconVirtual2 = findViewById(R.id.iconVirtual2)
        iconVirtual3 = findViewById(R.id.iconVirtual3)
    }

    private fun setupListeners() {
        // Breathing Exercise listeners (CON MÚSICA)
        iconBreathing.setOnClickListener {
            playMusic(it as ImageView, "instrumental", "Música Instrumental para Dormir")
        }

        iconMusic.setOnClickListener {
            playMusic(it as ImageView, "emotional", "Música Emocional")
        }

        iconRelax.setOnClickListener {
            playMusic(it as ImageView, "relax", "Música Relajante")
        }

        // Sports listeners (CON VIDEOS LOCALES)
        iconRun.setOnClickListener {
            playLocalVideo("sport_mental", "Ejercicio y Salud Mental")
        }

        iconYoga.setOnClickListener {
            playLocalVideo("yoga", "Yoga y Meditación")
        }

        iconBike.setOnClickListener {
            playLocalVideo("cardio", "Cardio en Casa")
        }

        // Sleep listeners
        iconSleep1.setOnClickListener {
            Toast.makeText(this, "Ejercicios para dormir", Toast.LENGTH_SHORT).show()
        }

        iconSleep2.setOnClickListener {
            Toast.makeText(this, "Gestión de expectativas", Toast.LENGTH_SHORT).show()
        }

        iconSleep3.setOnClickListener {
            Toast.makeText(this, "Calidad del sueño", Toast.LENGTH_SHORT).show()
        }

        // Virtual Companions listeners
        iconVirtual1.setOnClickListener {
            Toast.makeText(this, "Compañero virtual", Toast.LENGTH_SHORT).show()
        }

        iconVirtual2.setOnClickListener {
            Toast.makeText(this, "Conectar con amigos", Toast.LENGTH_SHORT).show()
        }

        iconVirtual3.setOnClickListener {
            Toast.makeText(this, "Chat y recordatorios", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playMusic(icon: ImageView, musicType: String, musicName: String) {
        // Si ya hay música sonando en este mismo ícono, detenerla
        if (currentPlayingIcon == icon && mediaPlayer?.isPlaying == true) {
            stopMusic()
            Toast.makeText(this, "Música detenida", Toast.LENGTH_SHORT).show()
            return
        }

        // Detener cualquier música que esté sonando
        stopMusic()

        try {
            // Mostrar diálogo de carga
            val loadingDialog = AlertDialog.Builder(this)
                .setMessage("Cargando música...")
                .setCancelable(false)
                .create()
            loadingDialog.show()

            // Crear nuevo MediaPlayer
            mediaPlayer = MediaPlayer().apply {
                setDataSource(musicUrls[musicType])

                // Cuando la música esté preparada
                setOnPreparedListener { mp ->
                    loadingDialog.dismiss()
                    mp.start()
                    currentPlayingIcon = icon

                    // Cambiar apariencia del ícono (opacidad)
                    icon.alpha = 0.7f

                    Toast.makeText(
                        this@SupportActivity,
                        "🎵 Reproduciendo: $musicName\n\nToca de nuevo para detener",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // Cuando termine la música
                setOnCompletionListener {
                    stopMusic()
                    Toast.makeText(this@SupportActivity, "Música finalizada", Toast.LENGTH_SHORT).show()
                }

                // Si hay error
                setOnErrorListener { _, what, extra ->
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@SupportActivity,
                        "Error al reproducir música.\nVerifica tu conexión a internet.",
                        Toast.LENGTH_LONG
                    ).show()
                    true
                }

                // Preparar de forma asíncrona
                prepareAsync()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * Reproduce un video LOCAL desde res/raw/
     */
    private fun playLocalVideo(videoType: String, videoName: String) {
        // Detener música si está sonando
        stopMusic()

        try {
            // Obtener el ID del recurso de video
            val videoResId = videoResources[videoType]

            if (videoResId == null) {
                Toast.makeText(this, "Video no encontrado", Toast.LENGTH_SHORT).show()
                return
            }

            Toast.makeText(this, "Cargando video: $videoName", Toast.LENGTH_SHORT).show()

            // Abrir VideoPlayerActivity con el ID del recurso
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("VIDEO_RES_ID", videoResId)  // Enviamos el ID del recurso
            intent.putExtra("VIDEO_TITLE", videoName)
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir video: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun stopMusic() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null

        // Restaurar apariencia del ícono
        currentPlayingIcon?.alpha = 1.0f
        currentPlayingIcon = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
        // Detener música si la app pasa a segundo plano
        stopMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos del MediaPlayer
        stopMusic()
    }
}