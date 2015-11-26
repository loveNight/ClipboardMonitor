package king;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/*
 ************************************************
 **���ڰ�ͼƬ��ַ������������̨��Ѷ�õ�Html��ʽ**
 ************************************************
 * ���ǵ�ͨ���ԣ�����������Զ���ѡ��
 * ��ͼƬURLת���������е�html���룬��Ҫ�������У�
 * λ�ã����С��ң�����ָ�������ߣ����ݿ�ȼ�ԭͼ����������ã�
 * �����ǹ����࣬�쳣һ���׳���˭����˭����
 */
public class ImageURLs {
    public static final String CENTER = "center";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final int DEFAULT_WIDTH = 500;
    
    /**
     * ����ָ����������ͼƬ��html�༭����ʹ�õĵ�ַ
     * Ĭ��ͼƬ���С����500
     * @param imageUrl ͼƬ��ַ
     * @return html����
     * @throws IOException
     */
    public static String transform(String imageUrl) 
            throws IOException{
        return transform(imageUrl, DEFAULT_WIDTH, CENTER);
    }
    
    /**
     * ����ָ����������ͼƬ��html�༭����ʹ�õĵ�ַ
     * �����Զ���
     * @param imageUrl ͼƬ��ַ
     * @param assignWidth ָ�����
     * @param align λ��
     * @return html����
     * @throws IOException
     */
    public static String transform(String imageUrl, int assignWidth, String align)
            throws IOException{
        // ʾ�� String htmlStr = "<p align=\"center\"><img src=\"http://..." ></p>";
        URL url = new URL(imageUrl);
        Dimension originalImageSize = getImageSize(url); 
        Dimension assignImageSize = getAssignImageSize(originalImageSize, assignWidth);
        String htmlStr ="<p align=\"" + align + "\"><img width=\"" 
                + assignImageSize.width + "\" height=\"" + assignImageSize.height + "\" src=\"" 
                + imageUrl + "\" ></p>";
        return htmlStr;
    }

    /**
     * ������ַ��ȡͼƬ��С
     * @param url ͼƬ��ַ
     * @return ͼƬ��С�� Dimension����
     * @throws IOException 
     */
    public static Dimension getImageSize(URL url) 
            throws IOException{
        BufferedImage image = ImageIO.read(url);
        Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
        return imageSize;
    }
    
    /**
     * ���ذ�������С��ĸ߶�
     * @param originalImageSize ͼƬԭʼ��С
     * @param assignWidth ָ�����
     * @return Ŀ��ͼƬ��С
     */
    public static Dimension getAssignImageSize(Dimension originalImageSize, int assignWidth){
        Dimension assignImageSize = null;
        if (originalImageSize.width <= 500){ //С��500�������Ŵ���
            assignImageSize = originalImageSize;
        }else{
            // ������������Ƶ����ִ�У������һ��Ϊ0������󷵻�ֵҲ��0
            assignImageSize = new Dimension(assignWidth, assignWidth * originalImageSize.height / originalImageSize.width);
        }
        return assignImageSize;
    }
    
    /**
     * ������
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        System.out.println(transform("http://www.stats.gov.cn/tjsj/zxfb/201501/W020150120311577730935_r75.gif"));
    }

}
