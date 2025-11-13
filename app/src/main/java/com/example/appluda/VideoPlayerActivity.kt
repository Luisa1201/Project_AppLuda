package com.example.appluda

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var progressBar: ProgressBar
    private lateinit var mediaController: MediaController

    companion object {
        private const val TAG = "VideoPlayerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Habilitar botón de retroceso
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtener datos del Intent
        val videoResId = intent.getIntExtra("VIDEO_RES_ID", -1)  // ID del recurso local
        val videoUrl = intent.getStringExtra("VIDEO_URL")        // URL externa (opcional)
        val videoTitle = intent.getStringExtra("VIDEO_TITLE")

        // Configurar título
        supportActionBar?.title = videoTitle ?: "Video"

        Log.d(TAG, "=================================")
        Log.d(TAG, "Video Res ID: $videoResId")
        Log.d(TAG, "Video URL: $videoUrl")
        Log.d(TAG, "Video Title: $videoTitle")
        Log.d(TAG, "=================================")

        initializeViews()

        // Reproducir video LOCAL o EXTERNO
        when {
            videoResId != -1 -> {
                // Video LOCAL desde res/raw/
                Log.d(TAG, "📱 Reproduciendo video LOCAL")
                setupLocalVideoPlayer(videoResId)
            }
            !videoUrl.isNullOrEmpty() -> {
                // Video EXTERNO desde URL
                Log.d(TAG, "🌐 Reproduciendo video desde URL")
                setupOnlineVideoPlayer(videoUrl)
            }
            else -> {
                Log.e(TAG, "❌ ERROR: No se proporcionó ni ID ni URL de video")
                Toast.makeText(this, "Error: No se especificó un video", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun initializeViews() {
        videoView = findViewById(R.id.videoView)
        progressBar = findViewById(R.id.progressBar)

        // Configurar controles de video
        mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
    }

    /**
     * Configurar y reproducir video LOCAL desde res/raw/
     */
    private fun setupLocalVideoPlayer(videoResId: Int) {
        try {
            progressBar.visibility = View.VISIBLE

            // Crear URI desde el recurso local
            val uri = Uri.parse("android.resource://$packageName/$videoResId")
            Log.d(TAG, "URI local creado: $uri")

            videoView.setVideoURI(uri)

            // Cuando el video esté preparado
            videoView.setOnPreparedListener { mp ->
                Log.d(TAG, "✅ Video LOCAL preparado")
                progressBar.visibility = View.GONE

                mp.setOnVideoSizeChangedListener { _, _, _ ->
                    mediaController.setAnchorView(videoView)
                }

                videoView.start()
                Toast.makeText(this, "▶️ Reproduciendo video", Toast.LENGTH_SHORT).show()
            }

            // Si hay error
            videoView.setOnErrorListener { mp, what, extra ->
                progressBar.visibility = View.GONE

                Log.e(TAG, "❌ Error reproduciendo video local - What: $what, Extra: $extra")

                Toast.makeText(
                    this,
                    "❌ Error al reproducir video local.\nVerifica que el archivo esté en res/raw/",
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            // Cuando termine el video
            videoView.setOnCompletionListener {
                Log.d(TAG, "✅ Video finalizado")
                Toast.makeText(this, "✅ Video finalizado", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            Log.e(TAG, "❌ Exception al configurar video local", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    /**
     * Configurar y reproducir video ONLINE desde URL
     */
    private fun setupOnlineVideoPlayer(videoUrl: String) {
        try {
            progressBar.visibility = View.VISIBLE

            Log.d(TAG, "Iniciando carga de video desde: $videoUrl")

            val uri = Uri.parse(videoUrl)
            videoView.setVideoURI(uri)

            // Cuando el video esté preparado
            videoView.setOnPreparedListener { mp ->
                Log.d(TAG, "✅ Video online preparado")
                progressBar.visibility = View.GONE

                mp.setOnVideoSizeChangedListener { _, _, _ ->
                    mediaController.setAnchorView(videoView)
                }

                videoView.start()
                Toast.makeText(this, "▶️ Reproduciendo video", Toast.LENGTH_SHORT).show()
            }

            // Si hay error
            videoView.setOnErrorListener { mp, what, extra ->
                progressBar.visibility = View.GONE

                val errorMsg = when (what) {
                    -1004 -> "Error de I/O al cargar el video"
                    -1007 -> "Video malformado"
                    -110 -> "Timeout de conexión"
                    1 -> "Error desconocido"
                    100 -> "Error del servidor"
                    else -> "Error código: $what"
                }

                Log.e(TAG, "❌ Error reproduciendo video online - What: $what, Extra: $extra")

                Toast.makeText(
                    this,
                    "❌ $errorMsg\n\nVerifica tu conexión a internet.",
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            // Cuando termine el video
            videoView.setOnCompletionListener {
                Log.d(TAG, "✅ Video finalizado")
                Toast.makeText(this, "✅ Video finalizado", Toast.LENGTH_SHORT).show()
            }

            // Información de buffering
            videoView.setOnInfoListener { mp, what, extra ->
                when (what) {
                    701 -> {
                        Log.d(TAG, "🔄 Buffering...")
                        progressBar.visibility = View.VISIBLE
                    }
                    702 -> {
                        Log.d(TAG, "✅ Buffering completado")
                        progressBar.visibility = View.GONE
                    }
                }
                false
            }

        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            Log.e(TAG, "❌ Exception al configurar video online", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause()
            Log.d(TAG, "⏸️ Video pausado")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
        Log.d(TAG, "🛑 VideoView destruido")
    }
}