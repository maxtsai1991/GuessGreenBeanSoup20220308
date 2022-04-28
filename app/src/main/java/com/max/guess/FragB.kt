package com.max.guess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class FragB : Fragment(){

    private val model:ShareViewModel by activityViewModels() // 在Fragment 要使用 by activityViewModels()這個 ,而在Activity 則使用 by viewModels()
    private lateinit var tvCount : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = LayoutInflater.from(requireContext()).inflate(R.layout.frag_b_layout,container,false)

        tvCount = view.findViewById(R.id.tv_count)

        /**
         * it 就是model.count的Valve值
         * 當model.count發生變化的時候 , FragB就能監聽的到 , FragB是觀察者
         */
        model.count.observe(viewLifecycleOwner){
            tvCount.text = it.toString() // 當FragA 的TextView(tvCount)發生變化的時候 , FragB 也會發生變化 , 因為FragB 觀察model.count , 這樣就可以達到共享的作用 , 也就實現FragA & FragB 共享ShareViewModel.kt 裡的count Value值
        }
        return view
    }

}