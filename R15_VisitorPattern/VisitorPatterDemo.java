import java.util.ArrayList;
import java.util.List;

public class VisitorPatterDemo {
    public static void main(String[] args) {
        //创建提取器
        Extractor extractor = new Extractor();
        List<ResourceFile> resourceFiles = listAllResourceFiles("directory");
        for (ResourceFile resourceFile : resourceFiles) {
            //resourceFile是多态实现，extract2txt是函数的重载
            //多态是动态绑定，运行时获取对象的实际类型
            //函数重载是静态绑定，编译时根据参数的声明类型执行对应的方法，不能获取对象的实际类型
            //所以下面执行报错
            extractor.extract2txt(resourceFile);
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
