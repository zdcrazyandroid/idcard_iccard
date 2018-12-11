package twoinonecard.com.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.byid.android.IDCard;
import com.byid.android.ReadPort;

import android_serialport_api.SerialPortFinder;

/**
 * Created by Administrator on 2018/8/1.
 */

public class ReadUtil {
    private ReadPort readPort;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    @SuppressLint("WrongConstant")
    public ReadUtil(Context context, int arg){
        sp=context.getSharedPreferences("setting", Context.MODE_APPEND);
        ed=sp.edit();
        if(arg==0) {
            readPort = new ReadPort(context, sp.getString("port","/dev/ttyS0"),115200);
            //readPort = new ReadPort(context, "/dev/ttyMT3",115200);
        }else if(arg==1){
//            while (true){
//                readPort=new ReadPort(context);
//                if(readPort.isPort()){
//                    break;
//                }
//            }
            readPort=new ReadPort(context);

        }
    }

    //通用判断是否连接
    public boolean isPort(){return readPort.isPort();}
    public boolean startProtocol(){return readPort.getStartProtocol();}
    //读取身份证UID
    public String idUid(){
        return readPort.readCardUid();
    }
    //通用读取身份证
    public IDCard readCard(){
        return readPort.readCard();
    }

    //IC卡类型：TYPEA TYPEB 参数： 0或1
    public void icType(int type){ readPort.icType(type);}
    //读取IC卡ID
    public String icID(){
        return readPort.icID();
    }
    //验证扇区密码 参数： 扇区ID和密码
    public boolean icPassWord(int sector,String str){
        return readPort.icPassWord(sector,str);
    }
    // 读取IC卡块数据 参数： 块ID
    public String icReadData(int sector){
        return readPort.icReadData(sector);
    }
    //写入IC卡块数据 参数：块ID 数据
    public boolean icWriteData(int rng,String rnx){
        return readPort.icWriteData(rng,rnx);
    }
    //写入IC卡块密码 参数：块ID 数据
    public boolean icWritePassword(int rng,String rnx){
        return readPort.icWritePassword(rng,rnx);
    }

    //关闭USB连接
    public void closeUSBDevice(){
        if(readPort!=null)
            readPort.closeUsbDevice();
    }
    //关闭串口连接
    public void closePort(){
        if(readPort!=null)
            readPort.closePort();
    }
}
