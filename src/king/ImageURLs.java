package king;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/*
 ************************************************
 **用于把图片网址处理成生意社后台资讯用的Html格式**
 ************************************************
 * 考虑到通用性，增加了许多自定义选项
 * 纯图片URL转化成文章中的html代码，需要的属性有：
 * 位置（左、中、右）、宽（指定）、高（根据宽度及原图比例计算而得）
 * 由于是工具类，异常一律抛出，谁调用谁处理
 */
public class ImageURLs {
    public static final String CENTER = "center";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final int DEFAULT_WIDTH = 500;
    
    /**
     * 根据指定参数返回图片在html编辑器中使用的地址
     * 默认图片居中、宽度500
     * @param imageUrl 图片网址
     * @return html代码
     * @throws IOException
     */
    public static String transform(String imageUrl) 
            throws IOException{
        return transform(imageUrl, DEFAULT_WIDTH, CENTER);
    }
    
    /**
     * 根据指定参数返回图片在html编辑器中使用的地址
     * 用于自定义
     * @param imageUrl 图片网址
     * @param assignWidth 指定宽度
     * @param align 位置
     * @return html代码
     * @throws IOException
     */
    public static String transform(String imageUrl, int assignWidth, String align)
            throws IOException{
        // 示例 String htmlStr = "<p align=\"center\"><img src=\"http://..." ></p>";
        URL url = new URL(imageUrl);
        Dimension originalImageSize = getImageSize(url); 
        Dimension assignImageSize = getAssignImageSize(originalImageSize, assignWidth);
        String htmlStr ="<p align=\"" + align + "\"><img width=\"" 
                + assignImageSize.width + "\" height=\"" + assignImageSize.height + "\" src=\"" 
                + imageUrl + "\" ></p>";
        return htmlStr;
    }

    /**
     * 根据网址获取图片大小
     * @param url 图片网址
     * @return 图片大小， Dimension对象
     * @throws IOException 
     */
    public static Dimension getImageSize(URL url) 
            throws IOException{
        BufferedImage image = ImageIO.read(url);
        Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
        return imageSize;
    }
    
    /**
     * 返回按比例缩小后的高度
     * @param originalImageSize 图片原始大小
     * @param assignWidth 指定宽度
     * @return 目标图片大小
     */
    public static Dimension getAssignImageSize(Dimension originalImageSize, int assignWidth){
        Dimension assignImageSize = null;
        if (originalImageSize.width <= 500){ //小于500则不作缩放处理
            assignImageSize = originalImageSize;
        }else{
            // 下面除法必须移到最后执行，否则第一步为0，则最后返回值也是0
            assignImageSize = new Dimension(assignWidth, assignWidth * originalImageSize.height / originalImageSize.width);
        }
        return assignImageSize;
    }
    
    /**
     * 测试用
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        System.out.println(transform("http://www.stats.gov.cn/tjsj/zxfb/201501/W020150120311577730935_r75.gif"));
    }

}
