package com.we_smart.lansharelib.shareconstant;

/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareConstant {

    //Share的时间
    public static final String SHARE_UTC = "share_utc";

    //Share的属性(可判断是否我们的应用协议)
    public static final String PROPERTY = "property";

    //分享的版本
    public static final String VERSION = "version";

    //分享者的手机品牌和手机型号
    public static final String SHARER_INFO = "info";

    //具体的数据
    public static final String MESH_DATA = "data";

    public class DataConstant {

        //mesh的信息
        public static final String MESH = "mesh";

        //群组的信息
        public static final String GROUP = "group";

        //设备的信息
        public static final String DEVICE = "device";

        //场景的信息
        public static final String SCENE = "scene";

        //自定义颜色
        public static final String FCOLOR = "favorite_color";

    }


    public class MeshConstant {

        //网络名称
        public static final String MESH_LOGIN_NAME = "login_name";

        //网络的显示名称
        public static final String MESH_SHOW_NAME = "show_name";

        //网路登陆密码
        public static final String MESH_LOGIN_PASSWORD = "login_psd";

        //网络编辑密码
        public static final String MESH_EDIT_PASSWORD = "edit_psd";

        //出厂的网络名称
        public static final String MESH_FACTORY_NAME = "factory_name";

        //出厂的网络密码
        public static final String MESH_FACTORY_PASSWORD = "factory_psd";

        //最后修改时间
        public static final String MESH_CHANGE_UTC = "mod_utc";

        //最后修改时间
        public static final String ALARM = "alarm";
    }

    public class DeviceConstant {
        //设备名称
        public static final String NAME = "name";

        //设备类型
        public static final String TYPE = "type";

        //设备meshId
        public static final String MESH_ID = "meshId";

        //设备的MacAddress
        public static final String MACADDRESS = "address";

        //通道一
        public static final String CHANNEL_ONE = "one";

        //通道二
        public static final String CHANNEL_TWO = "two";

        //通道三
        public static final String CHANNEL_THREE = "three";

        //通道四
        public static final String CHANNEL_FOUR = "four";

        //通道五
        public static final String CHANNEL_FIVE = "five";

        //设备的固件版本
        public static final String FIRMVERSION = "version";

        //
        public static final String ALARM = "alarm";

        //昼夜节律
        public static final String CIRADIAN = "circadian";

        //昼夜节律开关表示
        public static final String SYMBOLSTR = "symbol_str";
    }

    public class RoomConstant {

        //群组的名称
        public static final String NAME = "name";

        //群组的图片
        public static final String ICON = "icon";

        //群组的Id
        public static final String MESH_ID = "meshId";

        //设备数据
        public static final String DEV_ARRAY = "dev_array";

        //昼夜节律
        public static final String CIRADIAN = "circadian";
    }

    public class AlarmConstant {

        //定时的iD
        public static final String ID = "_id";

        //定时的时
        public static final String HOURS = "hours";

        //定时分
        public static final String MINUTE = "minute";

        //定时的时间戳
        public static final String UTC = "utc";

        //定时的周期
        public static final String WEEK = "week";

        //定时的事件
        public static final String EVENTS = "events";

        //定时的群组ID（设备时不会有）
        public static final String GROUPID = "group_id";

        public static final String STATUS = "status";
    }

    public class Scene {
        //场景的ID
        public static final String ID = "_id";

        //场景的名称
        public static final String NAME = "name";

        //场景的图标
        public static final String ICON = "icon";

        //场景模式
        public static final String MODE = "mode";

        //场景的条件现阶段是定时
        public static final String CONDITION = "condition";

        //场景的任务
        public static final String TASK = "task";

        //场景的条件类型
        public static final String CONDITION_TYPE = "type";
    }

    public class SceneColor {

        //场景的设备ID
        public static final String DEV_ID = "_id";

        //场景的开关
        public static final String MODE = "mode";

        //场景的红色
        public static final String RED = "R";

        //场景的绿色
        public static final String GREEN = "G";

        //场景的蓝色
        public static final String BLUE = "B";

        //场景的暖色
        public static final String WARM = "W";

        //场景的冷色
        public static final String COLD = "C";

        //场景的亮度
        public static final String BRIGHTNESS = "L";

    }

    /*
     @DBFiled
    public int cIndex;
    @DBFiled
    public String meshName;
    //自定义颜色与路数绑定
    @DBFiled
    public int parentChannel;
    @DBFiled
    public int red;
    @DBFiled
    public int green;
    @DBFiled
    public int blue;
    @DBFiled
    public int warm;
    @DBFiled
    public int cold;
    @DBFiled
    public int brightness;
     */

    public class FavoriteColor {
        public static final String C_INDEX = "c_index";

        public static final String MESH_NAME = "mesh_name";

        public static final String DEVICE_TYPE = "device_type";

        public static final String RED = "red";

        public static final String GREEN = "green";

        public static final String BLUE = "blue";

        public static final String WARM = "warm";

        public static final String COLD = "cold";

        public static final String BRIGHTNESS = "brightness";
    }

}
