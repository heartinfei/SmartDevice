package com.apeman.sdk.bean

/**
 * 当前的状态标示位
 * @author 王强 on 2019/3/25 249346528@qq.com
 */
enum class BleDeviceModel{
    DEVICE_POWER_OFF,
    DEVICE_STANDBY,
    DEVICE_INIT_BTN,
    DEVICE_OFFLINE,
    DEVICE_ACTIVE, //报点模式
    DEVICE_LOW_POWER_ACTIVE,
    DEVICE_OTA_MODE,//06
    DEVICE_OTA_WAIT_SWITCH,
    DEVICE_TRYING_POWER_OFF,
    DEVICE_FINISHED_PRODUCT_TEST,
    DEVICE_SYNC_MODE,
    DEVICE_SEMI_FINISHED_PRODUCT_TEST
}