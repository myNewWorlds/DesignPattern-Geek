import java.util.ArrayList;
import java.util.List;

public class VisitorPatterDemo {
    public static void main(String[] args) {
        //创建提取器
        Extractor extractor = new Extractor();
        //创建压缩器
        Compressor compressor = new Compressor();
        List<ResourceFile> resourceFiles = listAllResourceFiles("directory");
        for (ResourceFile resourceFile : resourceFiles) {
            //表面看是resourceFile调用extractor
            //其实内部将resourceFile作为参数，extractor回调自己的方法
            resourceFile.accept(extractor);
            resourceFile.accept(compressor);
        }
    }

    //添加所有的文件
    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new PPTFile("b.ppt"));
        resourceFiles.add(new WordFile("c.word"));
        return resourceFiles;
    }
}
