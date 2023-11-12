public class Compressor implements Visitor{
    @Override
    public void visit(PPTFile pptFile) {
        System.out.println("compress PPT");
    }

    @Override
    public void visit(WordFile wordFile) {
        System.out.println("compress Word");
    }

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("compress Pdf");
    }
}
