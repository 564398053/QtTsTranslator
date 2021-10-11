package com.amoy.QtTsTranslator;

import Translator.Translator;
import Translator.YoudaoDictTranslator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

// TS file structure
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
    Translator mTranslator = new YoudaoDictTranslator(); // default use YoudaoDict

    public TsParser(String file) {
        mFileName = file;
    }

    public void setTranslator(Translator translator) {
        mTranslator = translator;
    }

    void doParse() throws Exception {
        SAXReader saxReader = new SAXReader();
        mDocument = saxReader.read(mFileName);
        Element elRoot = mDocument.getRootElement();

        String language = elRoot.attributeValue("language");
        System.out.println("File Language: " + language);

        List<Element> elContextList = elRoot.elements("context");
        for (Element elContext : elContextList) {

            List<Element> elMessageList = elContext.elements("message");
            for (Element elMessage : elMessageList) {
                String source = elMessage.elementText("source"); // the string to translate

                Element elTranslation = elMessage.element("translation");
                String translationType = elTranslation.attributeValue("type", "");

                List<String> translationList = mTranslator.translate(source, "en", language);
                // Only translate the unfinished items.
                boolean bUnfinished = translationType.equals("unfinished");
                if (bUnfinished) {
                    if (translationList.isEmpty() || translationList.size() == 0) {
                        System.out.println("Translate failed: " + source);
                        continue;
                    }

                    elTranslation.remove(new DOMAttribute(new QName("type"))); // mark as finished
                    elMessage.setText(translationList.get(0));
                }
            }
        }
    }

    void save() throws Exception {
        XMLWriter writer = new XMLWriter(new FileWriter(mFileName));
        writer.write(mDocument);
        writer.close();
    }
}
