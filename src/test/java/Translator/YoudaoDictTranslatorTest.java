package Translator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YoudaoDictTranslatorTest {

    @Test
    void translate() throws Exception {
        YoudaoDictTranslator translator = new YoudaoDictTranslator();
        List<String> results = translator.translate("hello", "en", "zh-CHS");

        System.out.println(results);
    }
}