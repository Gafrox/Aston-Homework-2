package ru.gustavo.astonhomework2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import ru.gustavo.astonhomework2.databinding.ActivityMainBinding
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            resetButton.setOnClickListener {
                customView.clear()
                seekBar.progress = 50
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    val size = dp(this@MainActivity, (p1 + 200).toFloat())
                    customView.setSize(size, size)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
        }
    }

    private fun dp(context: Context, dp: Float): Int {
        return if (dp == 0F) 0 else ceil(
            context.resources.displayMetrics.density * dp
        ).toInt()
    }
}