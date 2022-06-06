package tech.vinay.workingwithpermissions


import android.Manifest
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            isGranted ->
                if(isGranted){
                    Toast.makeText(this, "Permission granted for Camera.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Permission denied for Camera.", Toast.LENGTH_SHORT).show()
                }

        }

    private val cameraAndLocationResultLauncher:ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){permissions->
            /**
            Here it returns a Map of permission name as key with boolean as value
            We loop through the map to get the value we need which is the boolean
            value
             */
            Log.d("MainActivity","Permissions $permissions")
            permissions.entries.forEach {
                val permissionName = it.key
                //if it is granted then we show its granted
                val isGranted = it.value
                if (isGranted) {
                    //check the permission name and perform the specific operation
                    if ( permissionName == Manifest.permission.ACCESS_FINE_LOCATION || permissionName == Manifest.permission.ACCESS_COARSE_LOCATION ) {
                        Toast.makeText(
                            this,
                            "Permission granted for location",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    else{
                        //check the permission name and perform the specific operation
                        Toast.makeText(
                            this,
                            "Permission granted for Camera",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    if ( permissionName == Manifest.permission.ACCESS_FINE_LOCATION || permissionName == Manifest.permission.ACCESS_COARSE_LOCATION ) {
                        Toast.makeText(
                            this,
                            "Permission denied location",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    else{
                        Toast.makeText(
                            this,
                            "Permission denied for Camera",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

    // if android.permission.ACCESS_FINE_LOCATION=true then android.permission.ACCESS_COARSE_LOCATION is also set to true

    /**
     * Shows rationale dialog for displaying why the app needs permission
     * Only shown if the user has denied the permission request previously
     */
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPermission : Button = findViewById(R.id.btnPermission)

        btnPermission.setOnClickListener {
            /**
             * To ask for multiple permission like location and camera.
             * shoudShowRequestPermissionRationale is set to true if permission is denied.
             */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ){
                showRationaleDialog("WorkingWithPermissions requires camera access", "Camera cannot be used as access is denied.")
            }
            else{
                cameraAndLocationResultLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }



        /**
         * You can directly ask permission or use shoudShowRequestPermissionRationale()
         */
//        btnPermission.setOnClickListener {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ){
//                showRationaleDialog("WorkingWithPermissions requires camera access", "Camera cannot be used as access is denied.")
//            }
//            else{
//                cameraResultLauncher.launch(Manifest.permission.CAMERA)
//            }
//        }

//        btnPermission.setOnClickListener {
//            cameraResultLauncher.launch(Manifest.permission.CAMERA)
//        }
    }
}