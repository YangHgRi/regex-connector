package yanghgri.executor;

import com.google.code.regexp.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YangHgRi
 */
public abstract class Executor {
    /**
     * 开始
     *
     * @param workDir   工作目录
     * @param regexList 正则表达式列表
     * @return {@link String}
     */
    public abstract String start(File workDir, List<String> regexList) throws IOException;

    Map<String, String> resolveBackreference(List<Pattern> patternList) {
        Map<String, String> backreferenceMap = new HashMap<>(patternList.size());

        patternList.forEach(pattern -> {
            pattern.groupNames().forEach(name -> {
                if (backreferenceMap.containsKey(name)) {
                    System.out.println("捕获组命名重复！");
                    System.exit(3);
                } else {
                    backreferenceMap.put(name, null);
                }
            });
        });
        return backreferenceMap;
    }
    
    List<Pattern> toPatternList(List<String> regexList) {
        return regexList.stream().map(Pattern::compile).collect(Collectors.toList());
    }
}