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
 * ����������
 * ����Լ�����ļ�غͲ���
 * ���ڼ����Ҫһ��������ΪClipboardOwner���ʲ����þ�̬��
 *
 */
public class SystemClipboardMonitor implements ClipboardOwner{
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private boolean going; //���ƿ���
    
    /*****************
     * ��ʼ���Ӽ����� *
     * ***************/
    public void begin(){
        going = true;
        handleClipboard();
    }
    
    /*****************
     * ֹͣ���Ӽ����� *
     * ***************/
    public void stop(){
        going = false;
    }

    
    /************
     * ���Դ��� *
     * **********/
    public static void main(String[] args) {
        SystemClipboardMonitor tmp = new SystemClipboardMonitor();
        tmp.begin(); //��ʼ����
        new JFrame().setVisible(true);// �������
    }
    
    /*******************************************
     * �������������ݸı䣬��ϵͳ�Զ����ô˷��� *
     *******************************************/
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        if (going){ //����ǽ�����״̬�������
            // �������ͣһ�£��������׳�IllegalStateException
            // �²��ǲ���ϵͳ����ʹ��ϵͳ���а壬����ʱ�޷�����
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handleClipboard(); //����֮
        }
    }
    
    /*************************
     **����ǰ�������е�����**
     *************************/
    private void handleClipboard(){
        // ȡ���ı�������һ���ı�����            
        // ��������������ı�:
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
            try {
                String text = (String)clipboard.getData(DataFlavor.stringFlavor);
                String clearedText = Texts.handle(text);
                // ��������壬��ע���Լ�Ϊ������
                // �����´μ��������ݸı�ʱ����Ȼ
                StringSelection tmp = new StringSelection(clearedText);
                clipboard.setContents(tmp, this);
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { 
            //�������������ݵ�ClipboardOwner����Ϊ�Լ�
            //�������������ݱ仯ʱ���ͻᴥ��lostOwnership�¼�
            clipboard.setContents(clipboard.getContents(null), this);
        }        
    }

}
