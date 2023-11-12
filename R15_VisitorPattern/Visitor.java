/**
 * 所有访问者（要访问PDFFile等数据结构类的）实现的总接口
 */
public interface Visitor {
    void visit(PPTFile pptFile);

    void visit(WordFile wordFile);

    void visit(PdfFile pdfFile);
}
