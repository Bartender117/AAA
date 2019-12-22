package come.test;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;

import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
 
/**
 * javaʵ������
 */
public class SpiderDemo1 {
 
    //��վ����ҳ����
    private final static String theURL = "http://www.jyyishu.cn";
    //�������ڣ����ڱ����־
    private final static String theTIME = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    //��ҳ�����ļ�·��
    private final static String theFILE = "D:/html/"  + "/URL.txt";
    //��ҳԴ��·��
    private final static String thePATH = "D:/html/" + "/code";
    //������ʽ�������ж��Ƿ���һ����ַ
    private final static String theREGEX= "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";
 
    /**
     * ������
     * @param args
     */
    public static void main(String[] args) {
        found();
        System.out.println("��վ��ȡ���");
    }
 
 
    public static void found() {
        PrintWriter pw = null;
        try{
            //�����ļ�Ŀ¼
            File fileDir = new File(thePATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
 
            //������վ��ҳ�����ļ�
            pw = new PrintWriter(new FileWriter(theFILE),true);
 
            //ʹ�õݹ������վ��ÿ����ҳ
            spiderURL(theURL, pw);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(pw != null) {
                    pw.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
 
 
    /**
     * ��ȡ����ҳԴ�����ҳ������
     * @param url ����ҳ��ַ
     * @param tpw ����վ��ҳ��ַ�ļ����ӵ�io��
     */
    public static void spiderURL(String url, PrintWriter tpw){
        URL realURL=null;
        URLConnection connection=null;
        BufferedReader br=null;
        PrintWriter pw=null;
        PrintWriter pw1=null;
 
        Pattern pattern=Pattern.compile(theREGEX);
        try{
            realURL=new URL(url);
            connection=realURL.openConnection();
 
            //�����ļ���
            String src = thePATH + url.substring(theURL.length());
            File fileDir = new File(src);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
 
            //����Դ�����ļ�
            pw = new PrintWriter(new FileWriter(src + "/Test.txt"),true);
            pw1 = tpw;
 
            //��ȡ��ҳ�ļ�
            br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line=null;
            while((line=br.readLine())!=null){
                //����ȡ��Դ��д���ļ�
                pw.println(line);
                System.out.println("��ȡ��ҳ" + url + "�ɹ�");
                Matcher matcher=pattern.matcher(line);
                //�ж��Ƿ���һ����ַ
                while(matcher.find()){
                    //�ж���ַ�Ƿ�����վ����ַΪǰ׺����ֹ����������վ,���ж��Ƿ��ԭ����ַ�ظ�
                    if(matcher.group().startsWith(theURL) && examine(matcher.group())) {
                        //����ȡ����ַд���ļ�
                        pw1.println(matcher.group());
                        spiderURL(matcher.group(), pw1);
                    }
                }
                System.out.println("��ҳ" + url + "��������ȡ���");
            }
    }catch(Exception e){
        e.printStackTrace();
    }finally {
            try {
                if(br != null) {
                    br.close();
                }
                if(pw != null) {
                    pw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
 
    /**
     * �ж��Ƿ���Դ�����ַ��ͬ
     * @param str ���жϵ���ַ
     * @return �Ƿ��ظ�
     */
    public static boolean examine(String str) {
        BufferedReader br = null;
        String str1;
        try {
            br = new BufferedReader(new FileReader(theFILE));
 
//            //��Ը���վ������ҳ������
//            if(str.startsWith("http://www.jyyishu.cn/artnews/")) {
//                return false;
//            }
            
            //ѭ���ļ���ÿһ�е���ַ���ж��Ƿ��ظ����ظ����˳�
            while((str1 = br.readLine()) != null) {
                if(str.equals(str1)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
        }
    }
    
 
    

