package com.ws.mesh.incores2.constant;

/**
 * 下一页传值的常量
 */

public class IntentConstant {
    //判断是否已经显示新手引导界面
    public static final String SHOW_GUIDE = "show_guide";

    //界面类型
    public static final String PAGE_TYPE = "page_type";

    //传递下一步需要的ID（场景、定时、设备、群组）
    public static final String NEED_ID = "need_id";

    public static final String ENTER_SCAN_VIEW = "enter_scan_view";
    //meshAddress
    public static final String MESH_ADDRESS = "mesh_address";

    public static final String INTENT_STR = "str_intent";
    //iconId
    public static final String ICON_ID = "icon_id";

    //Name
    public static final String NAME = "name";

    //选择的是场景还是设备
    public static final String IS_DEVICE = "is_device";

    //是群组还是设备
    public static final String IS_GROUP = "is_group";
    //重复
    public static final String WEEK_NUM = "week_num";
    //闹钟id
    public static final String ALARM_ID = "alarm_id";
    //闹钟的小时
    public static final String ALARM_HOUR = "alarm_hour";
    //闹钟的分钟
    public static final String ALARM_MINS = "alarm_min";
    //插排 开关选择的路数
    public static final String SELECTED_CHANNEL = "selected_channel";

    //场景ID
    public static final String SCENE_ID = "scene_id";

    //房间设备管理跳转类型
    public static final String ROOM_TURN_TYPE = "room_turn_type";

    //设备操作 判断是不是常用
    public static final String DEVICE_CONTROL_IS_COMMON = "device_control_is_common";

    //判断继续添加是 是不是已经添加到了最后一个设备 如果是最后一个设备则不能再继续添加设备
    public static final String IS_THE_LAST_ONE_DEVICE = "is_the_last_one_device";

    public static final String EXTRA = "extra";
    
    public static final String INTENT_OBJ = "intent_obj";
    public static final String RESULT = "result";
    public static final int REQUEST_CODE = 1;
}
