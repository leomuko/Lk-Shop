package com.example.shoppingapp.activities.UI.Home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.Login.LoginActivity
import com.example.shoppingapp.activities.Register.RegisterActivity
import com.example.shoppingapp.helpers.sliderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    val imageSliderAdapter = sliderAdapter(context)
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView : View= inflater.inflate(R.layout.fragment_home, container, false)

        val sliderView :SliderView = rootView.findViewById(R.id.imageSlider)


        sliderView.setSliderAdapter(imageSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = R.color.colorPrimary
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :

        if(mAuth.currentUser != null){
            rootView.login_button.visibility = View.GONE
            rootView.register_button.visibility = View.GONE
            rootView.tap_order_text.visibility = View.GONE
        }
        rootView.login_button.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        })
        rootView.register_button.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        })

        sliderView.startAutoCycle()
        for (i in 1..5){
            imageSliderAdapter.addItem("https://images.unsplash.com/photo-1461151304267-38535e780c79?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1190&q=80")
        }


        return rootView;
    }


}