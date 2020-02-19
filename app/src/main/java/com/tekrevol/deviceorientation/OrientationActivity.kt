package com.tekrevol.deviceorientation

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class OrientationActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mLastAccelerometerSet = false
    private var isLastPortrait = false
    var alert: AlertDialog? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        mLastAccelerometerSet = false
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int
    ) {
    }

    override fun onSensorChanged(event: SensorEvent) {
         if (event.sensor == mAccelerometer) {

             // If alert box showing then return
             alert?.let {
                 if (it.isShowing) return
             }

             // If alert box not showing then check logic
            if(Math.abs(event.values[1]) > Math.abs(event.values[0])) {
                //Mainly portrait


                // If last position was portrait and again portrait detected then return
                if (isLastPortrait) {
                    return
                } else {
                    isLastPortrait = true
                }

                showAlert()

                if (event.values[1] > 1) {
                    Log.d("ORIENTATION TESTING ","Portrait")
                } else if (event.values[1] < -1) {
                    Log.d("ORIENTATION TESTING ","Inverse portrait")
                }
            }else{
                //Mainly landscape


                // If last position was landscape and again landscape detected then return
                if (isLastPortrait) {
                    isLastPortrait = false
                } else {
                    return
                }

                if (event.values[0] > 1) {
                    Log.d("ORIENTATION TESTING ","Landscape - right side up")
                } else if (event.values[0] < -1) {
                    Log.d("ORIENTATION TESTING ","Landscape - left side up")
                }
            }
        }
    }


    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage("You can not use this screen in Portrait mode")
            .setCancelable(false)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        builder.setTitle("Alert")

        alert = builder.create()
        alert?.show()

    }
}
