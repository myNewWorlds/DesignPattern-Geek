/**
 * 将业务操作类
 * （与文件的数据结构解耦）
 */
public class Extractor {

    //提取ppt中的文本
    public void extract2txt(PPTFile pptFile) {
        System.out.println("extract PPT");
    }

    //提取PDF文件中的文本
    public void extract2txt(PdfFile pdfFile) {
        System.out.println("extract PDF");
    }

    //提取word文件中的文本
    public void extract2txt(WordFile wordFile) {
        System.out.println("extract WORD");
    }
}
