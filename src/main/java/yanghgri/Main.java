package yanghgri;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import yanghgri.executor.Executor;
import yanghgri.executor.YTDLPDeleteExecutor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YangHgRi
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("无参数异常！");
            System.exit(1);
        }

        String mode = args[0].toLowerCase();

        if (StringUtils.isEmpty(mode)) {
            System.out.println("工作模式未指定！");
            System.exit(0);
        }

        // 获取工作目录
        String workPath = args[1];

        if (StringUtils.isEmpty(workPath)) {
            System.out.println("工作目录未指定！");
            System.exit(1);
        }

        List<String> regexList = new ArrayList<>();

        // 从数组第一位元素开始收集正则
        for (int i = 2; i < args.length; i++) {
            if (StringUtils.isNotEmpty(args[i])) {
                regexList.add(args[i]);
            }
        }

        if (CollectionUtils.isEmpty(regexList)) {
            System.out.println("未提供正则！");
            System.exit(2);
        }

        Executor executor = null;

        switch (mode) {
            case ("delete-file"): {
                executor = new YTDLPDeleteExecutor();

                break;
            }
            default: {
                System.out.println("工作模无法识别！");
                System.exit(0);
            }
        }

        String result = executor.start(new File(workPath), regexList);
        System.out.println(result);
    }
}