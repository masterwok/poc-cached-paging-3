package com.example.pagingpoc.features.accountsettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pagingpoc.databinding.FragmentAccountSettingsBinding


class AccountSettingsFragment : Fragment() {

    private val binding get() = _binding!!

    private var _binding: FragmentAccountSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }
}