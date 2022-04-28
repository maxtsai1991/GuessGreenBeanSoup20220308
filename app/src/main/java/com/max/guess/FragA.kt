package com.max.guess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class FragA :Fragment() , View.OnClickListener{

    private lateinit var tvCount :TextView
    private lateinit var btnAdd :Button
    private val model:ShareViewModel by activityViewModels() // 在Fragment 要使用 by activityViewModels()這個 ,而在Activity 則使用 by viewModels()
    private var n = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View = LayoutInflater.from(requireContext()).inflate(R.layout.frag_a_layout,container,false)

        tvCount = view.findViewById(R.id.tv_count)
        btnAdd = view.findViewById(R.id.button)
        btnAdd.setOnClickListener(this)
        tvCount.text = model.count.value.toString()

        return view
    }

    override fun onClick(v: View?) {
        /**
         *  n 加 1 的時候, EX : n++
         *  去改變model.count的值, EX : model.count.value = n
         *  同時頁面上的TextView(tvCount)也去改變,並且轉成字串格式 EX : tvCount.text = n.toString()
         *  n 要放到全域變數, 不然都只會跳1(永遠都是1),不會往上疊加數字
         */
        n++
//        model.count.value = n
        model.add(n) // 等同 model.count.value = n 意思
        tvCount.text = n.toString()
    }

}