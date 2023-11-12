/**
 * PDF文件的数据结构
 */
public class PdfFile extends ResourceFile{
    public PdfFile(String filepath) {
        super(filepath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
