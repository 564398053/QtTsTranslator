package DictCache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DictCacheTest {

    @Test
    void load() throws Exception {
        DictCache dictCache = new DictCache("zh-CN");
        dictCache.load();
        System.out.println(dictCache.get("hello"));
        System.out.println(dictCache.get("world"));
    }

    @Test
    void save() throws Exception {
        DictCache dictCache = new DictCache("zh-CN");
        List<String> valueHello = new ArrayList<>();
        valueHello.add("你好");
        dictCache.put("hello", valueHello);
        List<String> valueWorld = new ArrayList<>();
        valueWorld.add("世界");
        dictCache.put("world", valueWorld);
        dictCache.save();
    }
}