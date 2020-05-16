package com.harper.carnet.ui.support.perms

interface OnPermissionsListener {
    fun onGrantSuccess(permissions: List<Permission>)

    fun onGrantFail(permissions: List<Permission>)
}
