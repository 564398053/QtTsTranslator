package com.amoy.QtTsTranslator;

import Translator.Translator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
    Translator mTranslator;

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
                String translationType = elTranslation.attributeValue("type");

                List<String> translationList = mTranslator.translate(source, "en", language);
                // Only translate the unfinished items.
                boolean bUnfinished = translationType.equals("unfinished");
                if (bUnfinished) {
                    System.out.println("Unfinished item: " + source);
                }
            }
        }
    }

}
