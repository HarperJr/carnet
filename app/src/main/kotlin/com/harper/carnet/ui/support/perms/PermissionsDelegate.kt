package com.harper.carnet.ui.support.perms

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionsDelegate(private val fragment: Fragment, private vararg val permissions: Permission) {
    var onPermissionsListener: OnPermissionsListener? = null

    fun requestPermissions() {
        val grantedPerms = getGrantedPerms(permissions)
        val allPermissionsGranted = grantedPerms.size == permissions.size
        if (allPermissionsGranted) {
            onPermissionsListener?.onGrantSuccess(grantedPerms)
        } else requestPermissions(permissions)
    }


    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQ_CODE) {
            val grantedPermissions = mutableListOf<Permission>()
            val ungrantedPermissions = mutableListOf<Permission>()

            for ((index, value) in grantResults.withIndex()) {
                val permission = Permission.of(permissions[index])
                if (value == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permission)
                } else ungrantedPermissions.add(permission)
            }

            onPermissionsListener?.onGrantSuccess(grantedPermissions)
            onPermissionsListener?.onGrantFail(ungrantedPermissions)
        }
    }

    private fun getGrantedPerms(permissions: Array<out Permission>): List<Permission> {
        val grantedPerms = mutableListOf<Permission>()
        for (perm in permissions) {
            val permissionsCheckState =
                ActivityCompat.checkSelfPermission(fragment.requireActivity(), perm.manifestString)
            if (permissionsCheckState == PackageManager.PERMISSION_GRANTED) {
                grantedPerms.add(perm)
            }
        }

        return grantedPerms
    }

    private fun requestPermissions(permissions: Array<out Permission>) {
        fragment.requestPermissions(permissions.map { it.manifestString }.toTypedArray(), REQ_CODE)
    }

    companion object {
        private const val REQ_CODE = 1024
    }
}
