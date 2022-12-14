package yanghgri.executor;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;
import com.sun.jna.platform.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author YangHgRi
 */
public class YTDLPDeleteExecutor extends Executor {
    private static final String FIXED_SUFFIX = "\\.(?:jpg|png|webp)";

    @Override
    public String start(File workDir, List<String> regexList) throws IOException {
        List<Pattern> patternList = super.toPatternList(regexList);

        Set<File> fileSet = ergodicByRegex(workDir, patternList);
        fileSet.forEach(file -> {
            try {
                FileUtils.getInstance().moveToTrash(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return "已删除 " + fileSet.size() + " 个文件！";
    }

    public Set<File> ergodicByRegex(File workPath, List<Pattern> patternList) throws IOException {
        Set<File> fileSet = new HashSet<>();
        Set<Pattern> patternSetForPic = new HashSet<>();

        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                String fileName = path.getFileName().toString();

                patternList.forEach(pattern -> {
                    Matcher matcher = pattern.matcher(fileName);
                    if (matcher.matches()) {
                        fileSet.add(path.toFile());
                        patternSetForPic.add(Pattern.compile(matcher.group("name").replace("[", "\\[").replace("]", "\\]") + FIXED_SUFFIX));
                    }
                });
                return super.visitFile(path, attrs);
            }
        };

        SimpleFileVisitor<Path> finderForPic = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                String fileName = path.getFileName().toString();

                patternSetForPic.forEach(pattern -> {
                    Matcher matcher = pattern.matcher(fileName);
                    if (matcher.matches()) {
                        fileSet.add(path.toFile());
                    }
                });
                return super.visitFile(path, attrs);
            }
        };

        Files.walkFileTree(workPath.toPath(), finder);
        Files.walkFileTree(workPath.toPath(), finderForPic);
        return fileSet;
    }
}