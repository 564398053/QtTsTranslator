package DictCache;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictCache {
    private Map<String, List<String>> valueMap = new HashMap<>(); // <source, translations>
    private String lang; // the current language

    public DictCache(String lang) {
        this.lang = lang;
    }

    public void load() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        valueMap = mapper.readValue(new File(getDictPath()), Map.class);
    }

    public void save() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File dir = new File(getDictDir());
        dir.mkdirs();
        File file = new File(getDictPath());
        mapper.writeValue(file, valueMap);
    }

    public void put(String key, List<String> value) {
        valueMap.put(key, value);
    }

    public List<String> get(String key) {
        return valueMap.getOrDefault(key, new ArrayList<>());
    }

    private String getDictPath() {
        return getDictDir() + lang + ".json";
    }

    private String getDictDir() {
        return "dict/";
    }
}
