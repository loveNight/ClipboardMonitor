package king;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.swing.JOptionPane;

public class Texts {
    public static final int INFO_COMPOSE = 0; //��Ѷ�Ű�
    public static final int IMAGE_TO_HTML = 1; //ͼƬ��ַתHTML
    public static final int IMAGE_TO_ANALYSIS = 2; //����ͼƬ��ַ��С����
    public static final int EXPLICIT_DATE = 3; //�滻���ȥ
    private static int model = -1; 
    private static final String XML_PATH = "./Data/xml/Regular.xml";
    private static final String SEPARATE = "�����";
    private static Properties propXML = new Properties(); //��XML�ļ���ȡ�����ϣ�keyΪ����ʽ��valueΪ�滻ֵ+˵������SEPARATE����
    private static Map<String, String> regulars = new HashMap<String, String>(); //���ڱ���������ʽ
    
    static{
        // ����ʱ��������ʱ�䲻һ��
        // ����������ʱ�����ٻ�ȡcalendar
        TimeZone tz =TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
    }
    private static Calendar calendar = Calendar.getInstance();
    
    
    //***************************
    //* ���ش˹�����ʱ���г�ʼ�� *
    //***************************
    static {
        loadMapFromXML();
    }
    
    /****************************************************
     * ��XML��ȡ�ļ���Properties���ϣ�����ȡ���滻ֵ��Map *
     * **************************************************/
    private static void loadMapFromXML(){
        File xml = new File(XML_PATH);
        //����ļ���������ִ��init()
        if (!xml.exists()){
            init();
            JOptionPane.showMessageDialog(null, "Regular.xml�ļ�������!\n��ʹ�ó�����������ʽ��ͬĿ¼������Regular.xml" +
                    "\n������Ҫ�����ֶ�����xml�ļ�"
                    , "XML�ļ�������", JOptionPane.ERROR_MESSAGE);
        }
        // ��ȡXML��Properties
        try(FileInputStream input = new FileInputStream(XML_PATH)){
            propXML.loadFromXML(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ��ȡvalue�е�value�еı��滻ֵ��Map����
        for(String key : propXML.stringPropertyNames()){
            String value = propXML.getProperty(key);
            String replacement = value.substring(0, value.indexOf(SEPARATE));
            regulars.put(key, replacement);
        }
    }
    
    /***************
     * ���ü��ģʽ *
     ***************/
    public static void setModel(int model){
        Texts.model = model;
    }
    
    /*****************
     * ��������ı� 
     * @throws IOException *
     *****************/
    public static String handle(String text) throws IOException{
        switch (model){
            case INFO_COMPOSE:
                return infoCompose(text);
            case IMAGE_TO_HTML:
                return ImageURLs.transform(text);
            case IMAGE_TO_ANALYSIS:
                return analysisImageURLCompose(text);
            case EXPLICIT_DATE:
                return infoCompose(explicitDate(text)); //�滻���ȥ��˳�㴦��һ����Ѷ��ʽ
        }
        return null;
    }
    
    /*****************************************
     **�滻�������е����졢���졢���졢ȥ������**
     *****************************************
     * @param text
     * @return
     */
    private static String explicitDate(String text){   
        // ȷ���ǵ����ʱ��
        calendar.setTimeInMillis(System.currentTimeMillis());
        //�����������������ֵ���꣬��ͬ��
        int todayYear = calendar.get(Calendar.YEAR);
        System.out.println("����1:" + todayYear);
        //�·ݴ�0��ʼ��
        int todayMonth = calendar.get(Calendar.MONTH) + 1;
        int todayDate = calendar.get(Calendar.DATE);
        // ����
        calendar.add(Calendar.DATE, 1);
        int tomorrowYear = calendar.get(Calendar.YEAR);
        int tomorrowMonth = calendar.get(Calendar.MONTH) + 1;
        int tomorrowDate = calendar.get(Calendar.DATE);
        // ����
        calendar.add(Calendar.DATE, -2);
        int yesterdayYear = calendar.get(Calendar.YEAR);
        int yesterdayMonth = calendar.get(Calendar.MONTH) + 1;
        int yesterdayDate = calendar.get(Calendar.DATE);
        // ȥ��
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.YEAR, -1);
        int lastYear = calendar.get(Calendar.YEAR);
        // ����
        calendar.add(Calendar.YEAR, 2);
        int nextYear = calendar.get(Calendar.YEAR);
        //��ʼ�滻
        text = text.replaceAll("(����)|(����)", yesterdayMonth + "��" + yesterdayDate + "��");
        text = text.replaceAll("(��ҹ)|(����)", yesterdayMonth + "��" + yesterdayDate + "����");
        text = text.replaceAll("(��ҹ)|(����)", todayMonth + "��" + todayDate + "����");
        text = text.replaceAll("(����)|(����)", todayMonth + "��" + todayDate + "��");
        text = text.replaceAll("(����)|(����)", tomorrowMonth + "��" + tomorrowDate + "��");
        text = text.replaceAll("ȥ��", lastYear + "��");
        text = text.replaceAll("����", nextYear + "��");
        text = text.replaceAll("����", todayYear + "��");
        return text;
    }
    
    /*******************
     **����ͼƬ��ַ����**
     *******************
     *��ͼƬ��С���ó�400*230
     * @param text ����ǰ��ַ
     * @return �������ַ
     */
    private static String analysisImageURLCompose(String text){
        text = text.replaceFirst("(?<=&w=)\\d+(?=&h)", "400");
        text = text.replaceFirst("(?<=&h=)\\d+(?=&bg)", "230");
        return text;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String s = "��-�죺���죬��-�죺���죬��-�죺���죬��-�꣺���꣬��-�꣺����";
        System.out.println(explicitDate(s));
//        String imgUrl = "http://i0.sinaimg.cn/cj/chanjing/gsnews/20150126/U7646P31T1D21394238F46DT20150126040816.jpg";
//        String regex = "(?<=(\\s)|(^))http://\\S*?(?=(\\s)|($))";
//        System.out.println(imgUrl.matches(regex));
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(calendar.get(Calendar.YEAR));
    }
    
    /********************************
     * �����õ�������ʽ���뱾���ļ� *
     ********************************/
    public static void init(){
        //������õ�������ʽ
        propXML.setProperty("[\\(\\[][(����)(,΢��)(΢��)(,����)]{1,}[\\)\\]]", SEPARATE + "eg:(΢��)[΢��](����,΢��)");
        propXML.setProperty("/(?=\n)", SEPARATE + "��С������/");
        propXML.setProperty("\\([\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.%]{1,}\\)", SEPARATE + "eg:(11.630, 1.06, 10.03%)");
        propXML.setProperty("\\[[\\d\\-\\.%]{1,}\\]", SEPARATE + "eg:[10.03%]");
        propXML.setProperty("\\[[\\d\\-\\.%]{1,}\\s�ʽ�\\s�б�\\]", SEPARATE + "eg:[35.8% �ʽ� �б�]");
        propXML.setProperty("\\(\\d{6},[(�ɰ�)(����)]{1,}\\)", SEPARATE + "eg:(600648,�ɰ�)");
        propXML.setProperty("\n{1,}", "\n\n" + SEPARATE + "һ�������س����滻�ɶ���");
        propXML.setProperty("\\([\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.%]{1,},\\sʵʱ����\\)", SEPARATE + "(6.56, 0.08, 1.24%, ʵʱ����)");
        propXML.setProperty("(?<=(\\s)|(^))http://\\S*?(?=(\\s)|($))", SEPARATE + "����ͼƬʱ���ֵ���ַ");
        // ����ļ��в����ڣ��򴴽�
        File directory = new File(XML_PATH).getParentFile();
        if (!directory.exists()) directory.mkdirs();
        // �����ļ�
        try(FileOutputStream output = new FileOutputStream(XML_PATH)){
            propXML.storeToXML(output, "keyΪJava������ʽ��valueΪ�滻ֵ��ʾ������" + SEPARATE + "����");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /************
     * ��Ѷ�Ű� *
     ************/
    public static String infoCompose(String text){
        for(String regular : regulars.keySet()){
            text = text.replaceAll(regular, regulars.get(regular));
        }
        return text;
    }

}
