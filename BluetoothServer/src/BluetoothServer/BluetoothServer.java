package BluetoothServer;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;  
import java.io.InputStream;  
import java.nio.charset.Charset;
  
import javax.microedition.io.Connector;  
import javax.microedition.io.StreamConnection;  
import javax.microedition.io.StreamConnectionNotifier;  
  
public class BluetoothServer implements Runnable {  
  
    // 流连接通知 用于创建流连接  
    private StreamConnectionNotifier myPCConnNotifier = null;  
    // 流连接  
    private StreamConnection streamConn = null;  
    // 接受数据字节流  
    private byte[] acceptedByteArray;  
    // 读取（输入）流  
    private InputStream inputStream = null;  
  
    /** 
     * 主线程 
     *   
     * @param args 
     */  
    public static void main(String[] args) {  
        new BluetoothServer();  
    }  
  
    /** 
     * 构造方法 
     */  
    public BluetoothServer() {  
        try {  
            // 得到流连接通知，下面的UUID必须和手机客户端的UUID相一致。  
            myPCConnNotifier = (StreamConnectionNotifier) Connector.open("btspp://localhost:0000110100001000800000805F9B34FB");  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        // 打开连接通道并读取流线程  
        new Thread(this).start();  
    }  
  
    @Override  
    public void run() {  
        try {  
            String inSTR = null;  
            streamConn = myPCConnNotifier.acceptAndOpen();  
            inputStream = streamConn.openInputStream();
            acceptedByteArray = new byte[1024];
            int len = -1;
            String movex_str;
            String movey_str;
            int movex;
            int movey;
            int speedx=0;
            int speedy=0;
            int nowx;
            int nowy;
            
            while (true) {  
            	if ((len=inputStream.read(acceptedByteArray))!=-1) {  
                    inSTR = new String(acceptedByteArray, 0, len, Charset.forName("UTF-8"));  
                    movex_str=inSTR.substring(inSTR.indexOf("<")+1, inSTR.indexOf(","));//截取字符串，获得x方向的移动距离
                    movey_str=inSTR.substring(inSTR.indexOf(",")+1, inSTR.indexOf(">"));//y方向
                    movex=Integer.parseInt(movex_str);
                    movey=Integer.parseInt(movey_str);
                    movex=0-movex;
                    speedx+=movex;
                    speedy+=movey;
                    Robot robot=new Robot();
                    Point point = MouseInfo.getPointerInfo().getLocation();//获得当前鼠标位置
                    nowx=point.x;
                    nowy=point.y;
                    System.out.println(nowx+" "+nowy);
                    robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
}  
