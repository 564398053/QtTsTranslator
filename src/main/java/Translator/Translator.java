package Translator;

import java.util.List;

public interface Translator {
    List<String> translate(String source, String from, String to) throws Exception;
}
