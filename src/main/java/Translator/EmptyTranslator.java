package Translator;

import java.util.ArrayList;
import java.util.List;

public class EmptyTranslator implements Translator {
    @Override
    public List<String> translate(String source) throws Exception {
        return new ArrayList<>();
    }
}
