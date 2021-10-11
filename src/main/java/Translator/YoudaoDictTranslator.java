package Translator;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class Translation {
    public String query;
    public List<String> translation;
    public String errorCode;
    public String l;
    public String tSpeakUrl;
    public List<String> returnPhrase;
    public Object web;
    public Object requestId;
    public Object dict;
    public Object webdict;
    public Object basic;
    public Object isWord;
    public Object speakUrl;
}

public class YoudaoDictTranslator implements Translator {
    // 翻译平台的语音格式可能和翻译文件的不一致，需要转换下
    private static String convertLanguage(String language) {
        // API文档
        // https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
        final Map<String, String> lanMap = new HashMap<>();
        lanMap.put("zh-CN", "zh-CHS");
        return lanMap.getOrDefault(language, language);
    }

    @Override
    public List<String> translate(String source, String from, String to) throws Exception {
        Map<String,String> params = new HashMap<String,String>();
        String q = source;
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", from);
        params.put("to", convertLanguage(to));
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        params.put("vocabId","您的用户词表ID");
        /** 处理结果 */
        List<String> result = requestForHttp(YOUDAO_URL, params);
        return result;
    }

    private static Logger logger = LoggerFactory.getLogger(YoudaoDictTranslator.class);

    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";

    private static final String APP_KEY = "2baf0424f27f4486";
    private static final String APP_SECRET = "VT4ZBELhAnk6ZXHeZqwVSP1WiJ2egeGf";

    private static List<String> requestForHttp(String url, Map<String,String> params) throws IOException {
        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key,value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try{
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            // logger.info("Content-Type:" + contentType[0].getValue());
            /** 响应不是音频流，直接显示结果 */
            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity,"UTF-8");
            EntityUtils.consume(httpEntity);

            ObjectMapper mapper = new ObjectMapper();
            Translation translation = mapper.readValue(json, Translation.class);
            // logger.info(mapper.writeValueAsString(translation));

            return translation.translation;
        } finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }
            }catch(IOException e){
                logger.info("## release resouce error ##" + e);
            }
        }
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     *
     * @param result 音频字节流
     * @param file 存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        }catch (Exception e){
            logger.info(e.toString());
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
