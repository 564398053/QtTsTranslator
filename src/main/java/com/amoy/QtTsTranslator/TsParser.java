package com.amoy.QtTsTranslator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

//<?xml version="1.0" encoding="utf-8"?>
//<!DOCTYPE TS>
//<TS version="2.1" language="fr_FR">
//  <context>
//      <name>QPushButton</name>
//      <message>
//          <location filename="main.cpp" line="73"/>
//          <source>Hello world!</source>
//          <translation type="unfinished"></translation>
//      </message>
//  </context>
//</TS>

public class TsParser {
    String mFileName;
    Document mDocument;
    public TsParser(String file) {
        mFileName = file;
    }

    void doParse() throws Exception {
        SAXReader saxReader = new SAXReader();
        mDocument = saxReader.read(mFileName);
        Element rootElement = mDocument.getRootElement();

        String language = rootElement.attributeValue("language");
        System.out.println("File Language: " + language);
        List<Element> servletElements = rootElement.elements("servlet");
        // 5. 遍历, 并获取该标签下的子标签数据内容, 使用父标签对象调用elementText方法, 传入子标签名称获取数据
        for (Element servlet : servletElements) {
            String name = servlet.elementText("servlet-name");
            String cls = servlet.elementText("servlet-class");
            System.out.println(name + " : " + cls);
        }
        // 使用 rootElement 根标签对象调用 elements 方法, 传入 servlet-mapping, 获取servlet-mapping标签对象
        List<Element> mappingElements = rootElement.elements("servlet-mapping");
        for (Element mapping : mappingElements) {
            String name = mapping.elementText("servlet-name");
            String url = mapping.elementText("url-pattern");
            System.out.println(name + " = " + url);
        }
    }

}
