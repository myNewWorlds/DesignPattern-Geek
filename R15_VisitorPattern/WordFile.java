/**
 * Word文件的数据结构
 */
public class WordFile extends ResourceFile{
    public WordFile(String filepath) {
        super(filepath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
