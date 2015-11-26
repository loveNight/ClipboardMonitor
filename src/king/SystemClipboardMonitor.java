package king;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * 剪贴板监控器
 * 负责对剪贴板的监控和操作
 * 由于监控需要一个对象作为ClipboardOwner，故不能用静态类
 *
 */
public class SystemClipboardMonitor implements ClipboardOwner{
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private boolean going; //控制开关
    
    /*****************
     * 开始监视剪贴板 *
     * ***************/
    public void begin(){
        going = true;
        handleClipboard();
    }
    
    /*****************
     * 停止监视剪贴板 *
     * ***************/
    public void stop(){
        going = false;
    }

    
    /************
     * 测试代码 *
     * **********/
    public static void main(String[] args) {
        SystemClipboardMonitor tmp = new SystemClipboardMonitor();
        tmp.begin(); //开始监视
        new JFrame().setVisible(true);// 软件窗口
    }
    
    /*******************************************
     * 如果剪贴板的内容改变，则系统自动调用此方法 *
     *******************************************/
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        if (going){ //如果是进行中状态，则操作
            // 如果不暂停一下，经常会抛出IllegalStateException
            // 猜测是操作系统正在使用系统剪切板，故暂时无法访问
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handleClipboard(); //处理之
        }
    }
    
    /*************************
     **处理当前剪贴板中的内容**
     *************************/
    private void handleClipboard(){
        // 取出文本并进行一次文本处理            
        // 如果剪贴板中有文本:
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
            try {
                String text = (String)clipboard.getData(DataFlavor.stringFlavor);
                String clearedText = Texts.handle(text);
                // 存入剪贴板，并注册自己为所有者
                // 这样下次剪贴板内容改变时，仍然
                StringSelection tmp = new StringSelection(clearedText);
                clipboard.setContents(tmp, this);
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { 
            //将剪贴板中内容的ClipboardOwner设置为自己
            //这样当其中内容变化时，就会触发lostOwnership事件
            clipboard.setContents(clipboard.getContents(null), this);
        }        
    }

}
