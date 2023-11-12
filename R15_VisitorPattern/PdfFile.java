/**
 * PDF文件的数据结构
 */
public class PdfFile extends ResourceFile{
    public PdfFile(String filepath) {
        super(filepath);
    }

    @Override
    public void accept(Extractor extractor) {
        extractor.extract2txt(this);
    }
}
