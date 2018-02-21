/**
 * Xadditus Android Project.
 * com.linoagli.android.xadditus.activities.input.fragments
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.linoagli.android.xadditus.R
import com.linoagli.android.xadditus.helpers.RequestFactory
import com.linoagli.android.xadditus.SendRequestEvent
import kotlinx.android.synthetic.main.fragment_operating_system_controls.*
import org.greenrobot.eventbus.EventBus

class OperatingSystemControlFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.fragment_operating_system_controls, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonVolumeUp.setOnClickListener { doVolumeUp() }
        buttonVolumeDown.setOnClickListener { doVolumeDown() }
    }

    private fun doVolumeUp() {
        val request = RequestFactory.createSystemVolumeUpRequest()
        EventBus.getDefault().post(SendRequestEvent(request))
    }

    private fun doVolumeDown() {
        val request = RequestFactory.createSystemVolumeDownRequest()
        EventBus.getDefault().post(SendRequestEvent(request))
    }
}