package com.nguyen.wechat.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@Slf4j
public class DtProcessHelper {
    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
    private static final String OAUTH20 = "oauth/20";

    public static JSONObject xml2Json(String xml){
        JSONObject json = new JSONObject();
        try {
            DocumentBuilder builder = FACTORY.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            if (document.hasChildNodes()){
                NodeList nodes = document.getChildNodes().item(0).getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++){
                    Node node = nodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE){
                        json.put(node.getNodeName(), StringUtils.defaultString(node.getTextContent()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse xml: {}, error: {}", xml, e);
        }
        return json;
    }

    public static Map<String, String> parseXml(InputStream inputStream) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 读取输入流
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList){
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        return map;
    }

    public static String httpServer(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName();
    }

    public static String oauth20(String server) {
        return StringUtils.endsWith(server,"/") ? server + OAUTH20 : server + "/" + OAUTH20;
    }
}
