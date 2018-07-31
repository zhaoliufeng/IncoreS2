package com.ws.mesh.incores2.utils;

import android.graphics.Color;

import com.telink.bluetooth.light.Opcode;
import com.ws.mesh.incores2.bean.SceneColor;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.service.WeSmartService;

import java.util.Calendar;

/**
 * 消息发送
 */

public class SendMsg {


    public static void sendCommonMsg(int meshAddress, byte opCode, byte[] params) {
        if (WeSmartService.Instance() != null && WeSmartService.Instance().isLogin()) {
            WeSmartService.Instance().sendCommandNoResponse(opCode, meshAddress, params);
        }
    }

    //获取设备类型
    public static void getDeviceType(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_EA.getValue(), new byte[]{0x10});
    }

    //获取设备当前所在的群组
    public static void getDevGroup(int meshAddress) {
        SendMsg.sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_DD.getValue(), new byte[]{0x08, 0x01});
    }


    //设备定位
    public static void locationDevice(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_D0.getValue(), new byte[]{0x03, 0x00, 0x00});
    }

    //全部设备
    public static void updateDeviceTime() {
        Calendar calendar = Calendar.getInstance();
        byte[] params = new byte[]{(byte) ((calendar.get(Calendar.YEAR) >> 0) & 0xFF),
                (byte) ((calendar.get(Calendar.YEAR) >> 8) & 0xFF), (byte) (calendar.get(Calendar.MONTH) + 1),
                (byte) calendar.get(Calendar.DAY_OF_MONTH), (byte) calendar.get(Calendar.HOUR_OF_DAY),
                (byte) calendar.get(Calendar.MINUTE), (byte) calendar.get(Calendar.SECOND)};
        sendCommonMsg(0xFFFF, Opcode.BLE_GATT_OP_CTRL_E4.getValue(), params);
    }

    //单个设备
    public static void updateDeviceTime(int meshAddress) {
        Calendar calendar = Calendar.getInstance();
        byte[] params = new byte[]{(byte) ((calendar.get(Calendar.YEAR) >> 0) & 0xFF),
                (byte) ((calendar.get(Calendar.YEAR) >> 8) & 0xFF), (byte) (calendar.get(Calendar.MONTH) + 1),
                (byte) calendar.get(Calendar.DAY_OF_MONTH), (byte) calendar.get(Calendar.HOUR_OF_DAY),
                (byte) calendar.get(Calendar.MINUTE), (byte) calendar.get(Calendar.SECOND)};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E4.getValue(), params);
    }

    //踢除设备
    public static void kickOut(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E3.getValue(), null);
    }

    /**
     * 五路设备控制
     */
    public static void setDevColor(int meshAddress, int color, int warm, int cold, boolean isChangeMode, byte validBit) {
        byte params[] = new byte[]{0x09, (byte) Color.red(color), (byte) Color.green(color), (byte) Color.blue(color),
                (byte) warm, (byte) cold, 0, (byte) (isChangeMode ? 1 : 0), validBit};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //四路设备的控制RGBW
    public static void setDevRGBW(int meshAddress, byte[] params) {
        params[8] = 0x15;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //四路设备的控制RGBC
    public static void setDevRGBC(int meshAddress, byte[] params) {
        params[8] = 0x0F;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //三路设备的控制RGBC
    public static void setDevRGB(int meshAddress, byte[] params) {
        params[8] = 0x07;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //两路路设备的控制RGBC
    public static void setDevWC(int meshAddress, byte[] params) {
        params[8] = 0x18;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //一路红
    public static void setDevRed(int meshAddress, byte redValues) {
        byte[] params = new byte[]{0x09, redValues, 0, 0,
                0, 0, 0, 0x01, 0x01};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //一路绿
    public static void setDevGreen(int meshAddress, byte greenValues) {
        byte[] params = new byte[]{0x09, 0, greenValues, 0,
                0, 0, 0, 0x01, 0x02};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //一路蓝
    public static void setDevBlue(int meshAddress, byte blueValues) {
        byte[] params = new byte[]{0x09, 0, 0, blueValues,
                0, 0, 0, 0x01, 0x04};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //一路暖色
    public static void setDevWarm(int meshAddress, byte warmValues) {
        byte[] params = new byte[]{0x09, 0, 0, 0,
                warmValues, 0, 0, 0x01, 0x08};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //一路冷色
    public static void setDevCool(int meshAddress, byte coolValues) {
        byte[] params = new byte[]{0x09, 0, 0, 0,
                coolValues, 0, 0, 0x01, 0x08};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    /**
     * 群组RGB测试
     */
    public static void setGroupColor(int meshAddress, int color) {
        byte[] params = {0x04, (byte) Color.red(color), (byte) Color.green(color), (byte) Color.blue(color), 0, 0};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }

    //群组WC测试
    public static void setGroupWC(int meshAddress, int warm) {
        int cold = 255 - warm;
        byte[] params = {0x08, (byte) warm, (byte) cold, 0, 1};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), params);
    }


    //开关灯
    public static void switchDevice(int meshAddress, boolean isOpen) {
        byte switchParams[] = new byte[]{(byte) (isOpen ? 0x01 : 0x00), 0x00, 0x00};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_D0.getValue(), switchParams);
    }

    public static void switchDevice(int meshAddress, int opVal) {
        byte switchParams[] = new byte[]{(byte) opVal, 0x00, 0x00};
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_D0.getValue(), switchParams);
    }

    //获取设备当前颜色
    public static void getDevColor(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_DA.getValue(), new byte[]{0x01});
    }

    /*
    * 亮度调控必须注意不能调到0.
    * */
    //亮度调节
    public static void setDevBrightness(int meshAddress, int brightness) {
        if (brightness <= 3) {
            switchDevice(meshAddress, false);
        } else {
            sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_D2.getValue(), new byte[]{(byte) brightness});
        }
    }

    /*
    * @params deviceAddress设备的mesh的地址取值范围为1-255;
    * @params groupAddress 群组的mesh的地址，取值范围为0x8001-0xFFFE
    * 
    * */
    public static void allocationGroup(int deviceAddress, int groupAddress) {
        byte[] params = new byte[]{0x01, (byte) (groupAddress & 0xFF),
                (byte) (groupAddress >> 8 & 0xFF)};
        sendCommonMsg(deviceAddress, Opcode.BLE_GATT_OP_CTRL_D7.getValue(), params);
    }

    /*
  * @params deviceAddress设备的mesh的地址取值范围为1-255;
  * @params groupAddress 群组的mesh的地址，取值范围为0x8001-0xFFFE
  * 
  * */
    public static void cancelAllocationGroup(int deviceAddress, int groupAddress) {
        byte[] params = new byte[]{0x00, (byte) (groupAddress & 0xFF),
                (byte) (groupAddress >> 8 & 0xFF)};
        sendCommonMsg(deviceAddress, Opcode.BLE_GATT_OP_CTRL_D7.getValue(), params);
    }


    /*
    * 删除群组
    * @param 需要删除群组的meshAddress;
    * */
    public static void deleteGroup(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_D7.getValue(), new byte[]{0x00, (byte) (meshAddress & 0xFF),
                (byte) (meshAddress >> 8 & 0xFF)});
    }

    /*
    * 昼节律为到达某个设定的时间，设备亮度值在某一段时间内由当前的亮度改变到最大。就像早上的太阳慢慢升起
    * 添加昼节律
    * @params startHours昼节律起始时间
     *@params startMinute昼节律起始分钟
     *@params durtime昼节律持续时间
    * 
    * */
    public static void addSunset(int startHours, int startMinute, int durtime, int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{(byte) (0x00),
                (byte) (15 & 0xFF), (byte) 0x92, (byte) 0x00, 0x7F, (byte) startHours, (byte) startMinute, 0,
                (byte) (15 & 0xFF), (byte) durtime});
    }

    /*
    * 删除昼节律
    * */
    public static void deleteSunset(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{(byte) (0x01), (byte) (15 & 0xFF), 0, 0,
                0, 0, 0, 0, 0});
    }


    /*
   * 夜节律为到达某个设定的时间，设备亮度值在某一段时间内由当前的亮度到最后关灯。就像的傍晚太阳慢慢下山一样
   * 添加夜节律
   * @params startHours夜节律起始时间
    *@params startMinute夜节律起始分钟
    *@params durtime夜节律持续时间
   * 
   * */
    public static void addSunrise(int startHours, int startMinute, int durtime, int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{(byte) (0x00),
                (byte) (16 & 0xFF), (byte) 0x92, (byte) 0x00, 0x7F, (byte) startHours, (byte) startMinute, 0,
                (byte) (16 & 0xFF), (byte) durtime});
    }

    /*
    * 删除夜节律
    * */
    public static void deleteSunrise(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{(byte) (0x01), (byte) (16 & 0xFF), 0, 0,
                0, 0, 0, 0, 0});
    }

//    public static void addAlarmScene(int meshAddress, Alarm alarmBean, int sceneId) {
//        if (null == alarmBean) return;
//        int byte13 = (2 + (alarmBean.mWeekNum == 0 ? 0 : 16) + 128);
//        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{0x02,
//                (byte) alarmBean.mAId, (byte) byte13, (byte) (alarmBean.mWeekNum == 0 ? alarmBean.mMonths : 0),
//                (byte) (alarmBean.mWeekNum == 0 ? alarmBean.mDate : alarmBean.mWeekNum), (byte) alarmBean.mHours,
//                (byte) alarmBean.mMins, (byte) 0, (byte) sceneId, 0
//        });
//    }
//
    public static void addAlarm(int meshAddress, Timing alarmBean) {
        if (null == alarmBean) return;
        int mode = alarmBean.mAlarmEvent > 1 ? 2 : alarmBean.mAlarmEvent;
        byte byte13;
        if (alarmBean.mWeekNum == 0) {
            if (mode == 0) {
                byte13 = AppConstant.DAY_OFF_ALARM;
            } else if (mode == 1) {
                byte13 = AppConstant.DAY_ON_ALARM;
            } else {
                byte13 = AppConstant.DAY_SCENE_ALARM;
            }
        } else {
            if (mode == 0) {
                byte13 = AppConstant.OFF_ALARM;
            } else if (mode == 1) {
                byte13 = AppConstant.ON_ALARM;
            } else {
                byte13 = AppConstant.SCENE_ALARM;
            }
        }
        int months = 0, day = 0;
        if (alarmBean.mWeekNum == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(alarmBean.mUtcTime);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            months = calendar.get(Calendar.MONTH) + 1;
        }
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{0x02,
                (byte) alarmBean.mAId, (byte) (byte13 & 0xFF), (byte) (alarmBean.mWeekNum == 0 ? months : 0),
                (byte) (alarmBean.mWeekNum == 0 ? day : alarmBean.mWeekNum), (byte) alarmBean.mHours,
                (byte) alarmBean.mMins, (byte) 0, (byte) (mode == 2 ? (0xF0 + alarmBean.mAlarmEvent - 1) : 0), 0
        });
    }

    /*
    * 删除定时
    * @params alarmId定时ID
    * @params meshAddress目的地址
    * */
    public static void deleteAlarm(int messAddress, int alarmId) {
        sendCommonMsg(messAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{0x01, (byte) alarmId, 0, 0, 0, 0, 0, 0});
    }

    /*
    * 关闭定时
    * @params alarmId定时ID
    * @params meshAddress目的地址
    * */
    public static void colseAlarm(int messAddress, int alarmId) {
        sendCommonMsg(messAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{0x04, (byte) alarmId, 0, 0, 0, 0, 0, 0});
    }


    /*
   * 打开定时
   * @params alarmId定时ID
   * @params meshAddress目的地址
   * */
    public static void openAlarm(int messAddress, int alarmId) {
        sendCommonMsg(messAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{0x03, (byte) alarmId, 0, 0, 0, 0, 0, 0});
    }


    /*
    * 读取定时
    * @params meshAddress读取的设备地址。
    * @params readMode阅读模式，当输入的是0的时候表示读取所有定时的详情。0xFF为读取所有定时的ID（目前只能支持到10个，最多只能返回10个ID）。
    * 0x01-0x07f表示读取对应的ID的定时数据数据。如果当前读取的ID为空，返回的所有数据为0
    * */
    public static void readAlarm(int meshAddress, int readMode) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E6.getValue(), new byte[]{0x10, (byte) readMode});
    }

    //    /*
//    *添加场景
//    * @params SceneColor场景的各种参数的实体类 (id不能大于13)
//    * */
    public static void addNewScene(int meshAddress, int sceneId, SceneColor sceneColor) {
        if (sceneId > 13) return;
        int brightness = sceneColor.mSceneBrightness;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_EE.getValue(), new byte[]{0x01, (byte) sceneId, (
                byte) brightness, (byte) sceneColor.mSceneRed,
                (byte) sceneColor.mSceneGreen, (byte) sceneColor.mSceneBlue,
                (byte) sceneColor.mSceneWarm, (byte) sceneColor.mSceneCold});
    }


    /*
   * 删除场景
   * @params 设备的目的地址meshAddress
   * @params 场景的ID.
   * */
    public static void deleteScene(int meshAddress, int sceneId) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_EE.getValue(), new byte[]{(byte) (0x00), (byte) (sceneId & 0xFF), 0, 0,
                0, 0, 0, 0, 0});
    }

    /*
    * 加载场景
    * @params 设备的目的地址meshAddress
    * @params 场景的ID.
    * 
    * */
    public static void loadScene(int meshAddress, int sceneId) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_EF.getValue(), new byte[]{(byte) sceneId});
    }

    /*
    * 获取场景
    * @params meshAddress设备地址
    * @params readMode阅读模式，当输入的是0的时候表示读取所有场景的详情。0xFF为读取所有场景的ID（目前只能支持到10个，最多只能返回10个ID）。
    * 0x01-0x07f表示读取对应的场景ID的场景数据。如果当前读取的ID为空，返回的所有数据为0
    * */
    public static void getScene(int meshAddress, int readMode) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_C0.getValue(), new byte[]{0x10, (byte) readMode});
    }

    public static int[] breathIndex = {
            0,//停止呼吸  
            1,//彩虹呼吸 
            2,//心跳呼吸 
            3,//漫绿呼吸 
            4,//漫蓝呼吸 
            5,//警报灯
            6,//闪烁
            7,//漫彩呼吸
            8,//绿色心情
            9,//夕阳美景
    };

    /*
    * 加载某种呼吸模式
    * */
    public static void loadBreath(int meshAddress, int index) {
        if (index < 0 || index > 9) return;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(), new byte[]{0x0a, (byte) breathIndex[index]});
    }


//    /*
//    * 读取某个设备的固件版本。
//    * */
//    public static void getFirmware(int meshAddress) {
//        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_C7.getValue(), new byte[]{ 0x10, 0x00 });
//    }

    /*
    *   MeshOTA 比较直连设备的版本号 判断是否升级。
    * */
    public static void startMeshOta() {
        byte opcode = (byte) 0xC6;
        int address = 0x0000;
        byte[] params = new byte[]{(byte) 0xFF, (byte) 0xFF};
        sendCommonMsg(address, opcode, params);
    }


    /*
    * 关闭定时
    * meshAddress 设备的MeshId
    * alarmId闹钟Id(1~14)
    * */
    public static void closeAlarm(int meshAddress, int alarmId) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E5.getValue(), new byte[]{0x04,
                (byte) alarmId});
    }


    /*
    * 获取群组中设备
    * groupAddress 获取对应Id群组中的设备。
    * 
    * */
    public static void getDevOfGroup(int groupAddress) {
        sendCommonMsg(groupAddress, Opcode.BLE_GATT_OP_CTRL_DA.getValue(), new byte[]{0x10});
    }

    public static void reStartDevice(int meshAddress) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_FF.getValue(), new byte[]{(byte) 0xFF});
    }

    public static void sendMusicCommand(int meshAddress, int color) {
        byte red = (byte) Color.red(color);
        byte green = (byte) Color.green(color);
        byte blue = (byte) Color.blue(color);
        byte warm = (byte) (0);
        byte cold = (byte) (0);
        byte brightness = (byte) (0);
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_E2.getValue(),
                new byte[]{0x09, red, green, blue, warm, cold, brightness, (byte) 0x00, 0x07});
    }

    //修改勿扰亮度
    public static void changeDNDBrightness(int meshAddress, int brightness) {
        //勿扰模式的场景Id是 14
        int dndId = 14;
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_EE.getValue(), new byte[]{0x01, (byte) dndId, (
                byte) brightness, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0xFF, (byte) 0});
    }

    //修改勿扰时间
    public static void changeDNDTime(int meshAddress, boolean isOpen, int startTime, int endTime) {
        sendCommonMsg(meshAddress, Opcode.BLE_GATT_OP_CTRL_FF.getValue(), new byte[]{
                0x09, (byte) (isOpen ? 0xA5 : 0xA4), (byte) startTime, 0, (byte) endTime, 0
        });
    }

    public static void getFirmware(int meshAddress) {
        WeSmartService.Instance().sendCommandNoResponse((byte) 0xC7, meshAddress, new byte[]{0x10, 0x00});
    }

    /**
     * 2018 5/9解决音乐模式上报的问题 修改最后一位 判断是否需要状态上报
     */
    public static void sendColorCommonMsg(int mDeviceAddress, byte[] colorData) {
        if (WeSmartService.Instance() != null && WeSmartService.Instance().isLogin()) {
            WeSmartService.Instance().sendCommandNoResponse(Opcode.BLE_GATT_OP_CTRL_E2.getValue(), mDeviceAddress, colorData);
        }
    }
}


