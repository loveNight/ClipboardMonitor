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
    public static final int INFO_COMPOSE = 0; //资讯排版
    public static final int IMAGE_TO_HTML = 1; //图片网址转HTML
    public static final int IMAGE_TO_ANALYSIS = 2; //分析图片网址大小调整
    public static final int EXPLICIT_DATE = 3; //替换昨今去
    private static int model = -1; 
    private static final String XML_PATH = "./Data/xml/Regular.xml";
    private static final String SEPARATE = "※★※";
    private static Properties propXML = new Properties(); //将XML文件读取到集合，key为正则式，value为替换值+说明，以SEPARATE隔开
    private static Map<String, String> regulars = new HashMap<String, String>(); //用于保存正则表达式
    
    static{
        // 设置时区，否则时间不一致
        // 必须先设置时区，再获取calendar
        TimeZone tz =TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
    }
    private static Calendar calendar = Calendar.getInstance();
    
    
    //***************************
    //* 加载此工具类时进行初始化 *
    //***************************
    static {
        loadMapFromXML();
    }
    
    /****************************************************
     * 从XML读取文件到Properties集合，再提取被替换值到Map *
     * **************************************************/
    private static void loadMapFromXML(){
        File xml = new File(XML_PATH);
        //如果文件不存在则执行init()
        if (!xml.exists()){
            init();
            JOptionPane.showMessageDialog(null, "Regular.xml文件不存在!\n已使用程序内置正则式在同目录下生成Regular.xml" +
                    "\n如有需要，请手动设置xml文件"
                    , "XML文件不存在", JOptionPane.ERROR_MESSAGE);
        }
        // 读取XML到Properties
        try(FileInputStream input = new FileInputStream(XML_PATH)){
            propXML.loadFromXML(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 提取value中的value中的被替换值到Map集合
        for(String key : propXML.stringPropertyNames()){
            String value = propXML.getProperty(key);
            String replacement = value.substring(0, value.indexOf(SEPARATE));
            regulars.put(key, replacement);
        }
    }
    
    /***************
     * 设置监控模式 *
     ***************/
    public static void setModel(int model){
        Texts.model = model;
    }
    
    /*****************
     * 处理传入的文本 
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
                return infoCompose(explicitDate(text)); //替换昨今去，顺便处理一下资讯格式
        }
        return null;
    }
    
    /*****************************************
     **替换掉新闻中的昨天、今天、明天、去等字眼**
     *****************************************
     * @param text
     * @return
     */
    private static String explicitDate(String text){   
        // 确保是当天的时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        //今天的年数（可能正值跨年，下同）
        int todayYear = calendar.get(Calendar.YEAR);
        System.out.println("今年1:" + todayYear);
        //月份从0开始算
        int todayMonth = calendar.get(Calendar.MONTH) + 1;
        int todayDate = calendar.get(Calendar.DATE);
        // 明天
        calendar.add(Calendar.DATE, 1);
        int tomorrowYear = calendar.get(Calendar.YEAR);
        int tomorrowMonth = calendar.get(Calendar.MONTH) + 1;
        int tomorrowDate = calendar.get(Calendar.DATE);
        // 昨天
        calendar.add(Calendar.DATE, -2);
        int yesterdayYear = calendar.get(Calendar.YEAR);
        int yesterdayMonth = calendar.get(Calendar.MONTH) + 1;
        int yesterdayDate = calendar.get(Calendar.DATE);
        // 去年
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.YEAR, -1);
        int lastYear = calendar.get(Calendar.YEAR);
        // 明年
        calendar.add(Calendar.YEAR, 2);
        int nextYear = calendar.get(Calendar.YEAR);
        //开始替换
        text = text.replaceAll("(昨天)|(昨日)", yesterdayMonth + "月" + yesterdayDate + "日");
        text = text.replaceAll("(昨夜)|(昨晚)", yesterdayMonth + "月" + yesterdayDate + "日晚");
        text = text.replaceAll("(今夜)|(今晚)", todayMonth + "月" + todayDate + "日晚");
        text = text.replaceAll("(今天)|(今日)", todayMonth + "月" + todayDate + "日");
        text = text.replaceAll("(明天)|(明日)", tomorrowMonth + "月" + tomorrowDate + "日");
        text = text.replaceAll("去年", lastYear + "年");
        text = text.replaceAll("明年", nextYear + "年");
        text = text.replaceAll("今年", todayYear + "年");
        return text;
    }
    
    /*******************
     **分析图片网址处理**
     *******************
     *将图片大小设置成400*230
     * @param text 处理前网址
     * @return 处理后网址
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
        String s = "今-天：今天，昨-天：昨天，明-天：明天，今-年：今年，明-年：明年";
        System.out.println(explicitDate(s));
//        String imgUrl = "http://i0.sinaimg.cn/cj/chanjing/gsnews/20150126/U7646P31T1D21394238F46DT20150126040816.jpg";
//        String regex = "(?<=(\\s)|(^))http://\\S*?(?=(\\s)|($))";
//        System.out.println(imgUrl.matches(regex));
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(calendar.get(Calendar.YEAR));
    }
    
    /********************************
     * 把内置的正则表达式存入本地文件 *
     ********************************/
    public static void init(){
        //添加内置的正则表达式
        propXML.setProperty("[\\(\\[][(博客)(,微博)(微博)(,博客)]{1,}[\\)\\]]", SEPARATE + "eg:(微博)[微博](博客,微博)");
        propXML.setProperty("/(?=\n)", SEPARATE + "★小标题后的/");
        propXML.setProperty("\\([\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.%]{1,}\\)", SEPARATE + "eg:(11.630, 1.06, 10.03%)");
        propXML.setProperty("\\[[\\d\\-\\.%]{1,}\\]", SEPARATE + "eg:[10.03%]");
        propXML.setProperty("\\[[\\d\\-\\.%]{1,}\\s资金\\s研报\\]", SEPARATE + "eg:[35.8% 资金 研报]");
        propXML.setProperty("\\(\\d{6},[(股吧)(基金)]{1,}\\)", SEPARATE + "eg:(600648,股吧)");
        propXML.setProperty("\n{1,}", "\n\n" + SEPARATE + "一个或多个回车符替换成二个");
        propXML.setProperty("\\([\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.]{1,}[,\\s]{1,}[\\d\\-\\.%]{1,},\\s实时行情\\)", SEPARATE + "(6.56, 0.08, 1.24%, 实时行情)");
        propXML.setProperty("(?<=(\\s)|(^))http://\\S*?(?=(\\s)|($))", SEPARATE + "复制图片时出现的网址");
        // 如果文件夹不存在，则创建
        File directory = new File(XML_PATH).getParentFile();
        if (!directory.exists()) directory.mkdirs();
        // 存入文件
        try(FileOutputStream output = new FileOutputStream(XML_PATH)){
            propXML.storeToXML(output, "key为Java正则表达式，value为替换值和示例，用" + SEPARATE + "隔开");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /************
     * 资讯排版 *
     ************/
    public static String infoCompose(String text){
        for(String regular : regulars.keySet()){
            text = text.replaceAll(regular, regulars.get(regular));
        }
        return text;
    }

}
