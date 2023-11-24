package com.example.playlistmaker.root.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.search.ui.fragments.SearchFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Привязываем вёрстку к экрану
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Добавляем фрагмент в контейнер
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, SearchFragment())
            }
        }
    }

}