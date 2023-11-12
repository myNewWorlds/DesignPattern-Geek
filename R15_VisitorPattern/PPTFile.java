/**
 * PPT文件的数据结构
 */
public class PPTFile extends ResourceFile{
    public PPTFile(String filepath) {
        super(filepath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
