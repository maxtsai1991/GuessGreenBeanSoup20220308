package com.max.guess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.max.guess.databinding.FragOneBinding

class FragOne : Fragment() {

    private lateinit var binding : FragOneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view :View = LayoutInflater.from(requireContext()).inflate(R.layout.frag_one,container,false)
        /**
         * 使用ViewBinding的綁定畫面寫法
         */
        binding = FragOneBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvName1.text = "使用viewbinding設定文字"           // 使用viewbinding設定文字
        binding.imgHead.setImageResource(R.drawable.image_coder)//  使用viewbinding設定圖片
    }
}