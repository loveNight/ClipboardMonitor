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
    private String[] option = {"��Ѷ�Ű�", "ͼƬ��ַתhtml", "����ͼƬ��С����", "�滻���ȥ"}; //������Texts����һ��
    private SystemClipboardMonitor clipboardMonotor = new SystemClipboardMonitor(); //�����������
    private JButton begin = new JButton("��ʼ");
    private JButton stop = new JButton("ֹͣ");
    private JButton close = new JButton("�ر�");
    private JRadioButton[] buttons = new JRadioButton[option.length];
    private JLabel prompt = new JLabel();
    
    private ImageIcon icon = new ImageIcon(GUI.class.getClass().getResource("/img/icon.png"));
    private TrayIcon trayIcon = new TrayIcon(icon.getImage(), "С����Ѷ������");
    private SystemTray systemTray = SystemTray.getSystemTray(); //ϵͳ����
    private PopupMenu pop = new PopupMenu(); //�����Ҽ��˵�
    private MenuItem show = new MenuItem("��ԭ");
    private MenuItem exit = new MenuItem("�˳�");
    private MenuItem stopMenu = new MenuItem("ֹͣ");
    
    static{
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    
    public GUI(){
        super("С����Ѷ������");
        //�м�Ķ�ѡ��
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
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "��������"));
        add(panel);
        //�Ϸ�����ʾ����
        prompt.setFont(new Font("΢���ź�", Font.BOLD, 20));
        prompt.setText("<html><p align=\"center\"><font color=\"blue\">" 
                + "���������ڼ������ı�������ֵ" + "</font><br>" 
                + "<font size=\"5\" color=\"red\">" + "���ڹ��ܳ�ͻ��ʹ��ǰ�ȹرմʵ��������" 
                + "</font></p></html>");
        prompt.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(prompt, BorderLayout.NORTH);
        //�ײ���ť
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
        //������С���¼�
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowIconified(WindowEvent e){
                setVisible(false);
            }
        });
        //�������˵�
        show.addActionListener(this);
        exit.addActionListener(this);
        stopMenu.addActionListener(this);
        pop.add(stopMenu);
        pop.add(show);
        pop.add(exit);
        trayIcon.setPopupMenu(pop);
        trayIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){ // ���˫���¼�
                if (e.getClickCount() == 2){
                    setVisible(true); 
                    setExtendedState(JFrame.NORMAL); //��ԭ���� 
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
        if (e.getSource() == begin){ //������ʼ��ť
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
            setExtendedState(JFrame.NORMAL); //��ԭ���� 
        }
    }    
}
