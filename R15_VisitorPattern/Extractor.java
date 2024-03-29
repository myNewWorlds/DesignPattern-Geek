/**
 * 将业务操作类
 * （与文件的数据结构解耦）
 */
public class Extractor implements Visitor {

    //提取ppt中的文本
    @Override
    public void visit(PPTFile pptFile) {
        System.out.println("extract PPT");
    }

    //提取PDF文件中的文本
    @Override
    public void visit(PdfFile wordFile) {
        System.out.println("extract PDF");
    }

    //提取word文件中的文本
    @Override
    public void visit(WordFile pdfFile) {
        System.out.println("extract WORD");
    }
}
