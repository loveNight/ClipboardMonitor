package king;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI extends JFrame implements ActionListener {
    private String[] option = {"资讯排版", "图片网址转html", "分析图片大小调整", "替换昨今去"}; //次序与Texts类中一致
    private SystemClipboardMonitor clipboardMonotor = new SystemClipboardMonitor(); //剪贴板监视器
    private JButton begin = new JButton("开始");
    private JButton stop = new JButton("停止");
    private JButton close = new JButton("关闭");
    private JRadioButton[] buttons = new JRadioButton[option.length];
    private JLabel prompt = new JLabel();
    
    private ImageIcon icon = new ImageIcon(GUI.class.getClass().getResource("/img/icon.png"));
    private TrayIcon trayIcon = new TrayIcon(icon.getImage(), "小金资讯处理工具");
    private SystemTray systemTray = SystemTray.getSystemTray(); //系统托盘
    private PopupMenu pop = new PopupMenu(); //托盘右键菜单
    private MenuItem show = new MenuItem("还原");
    private MenuItem exit = new MenuItem("退出");
    private MenuItem stopMenu = new MenuItem("停止");
    
    static{
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    
    public GUI(){
        super("小金资讯处理工具");
        //中间的多选项
        ButtonGroup bg = new ButtonGroup();
        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new GridLayout(2,2));
        for(int i = 0; i < option.length; i++){
            buttons[i] = new JRadioButton(option[i]);
            bg.add(buttons[i]);
            panel.add(buttons[i]);
            if (i == 0) buttons[i].setSelected(true);
        }
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "功能类型"));
        add(panel);
        //上方的提示文字
        prompt.setFont(new Font("微软雅黑", Font.BOLD, 20));
        prompt.setText("<html><p align=\"center\"><font color=\"blue\">" 
                + "本程序用于监听并改变剪贴板的值" + "</font><br>" 
                + "<font size=\"5\" color=\"red\">" + "由于功能冲突，使用前先关闭词典类软件！" 
                + "</font></p></html>");
        prompt.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(prompt, BorderLayout.NORTH);
        //底部按钮
        JPanel bottom = new JPanel();
        begin.addActionListener(this);
        stop.addActionListener(this);
        close.addActionListener(this);
        bottom.add(begin);
        bottom.add(stop);
        stop.setVisible(false);
        bottom.add(close);
        bottom.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(bottom, BorderLayout.SOUTH);
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setIconImage(icon.getImage());
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //窗口最小化事件
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowIconified(WindowEvent e){
                setVisible(false);
            }
        });
        //任务栏菜单
        show.addActionListener(this);
        exit.addActionListener(this);
        stopMenu.addActionListener(this);
        pop.add(stopMenu);
        pop.add(show);
        pop.add(exit);
        trayIcon.setPopupMenu(pop);
        trayIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){ // 鼠标双击事件
                if (e.getClickCount() == 2){
                    setVisible(true); 
                    setExtendedState(JFrame.NORMAL); //还原窗口 
                }
            }
        });
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new GUI().setVisible(true);
            }
        });
    }
    
    private void setButtonsEnabled(boolean b){
        for(JRadioButton button : buttons){
            button.setEnabled(b);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == begin){ //单击开始按钮
            begin.setVisible(false);
            stop.setVisible(true);
            setButtonsEnabled(false);
            for(int i = 0; i < buttons.length; i++){
                if (buttons[i].isSelected()){
                    Texts.setModel(i);
                    break;
                }
            }
            clipboardMonotor.begin();
        }else if (e.getSource() == close || e.getSource() == exit){
            System.exit(0);
        }else if (e.getSource() == stop || e.getSource() == stopMenu){
            begin.setVisible(true);
            stop.setVisible(false);
            setButtonsEnabled(true);
            clipboardMonotor.stop();
        }else if (e.getSource() == show){
            setVisible(true); 
            setExtendedState(JFrame.NORMAL); //还原窗口 
        }
    }    
}
