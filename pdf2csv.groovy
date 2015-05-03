@Grapes([
    @Grab(group='org.bouncycastle', module='bcprov-jdk16', version='1.46'),
    @Grab(group='org.apache.pdfbox', module='pdfbox', version='1.8.8'),
    @Grab(group='org.apache.poi', module='poi', version='3.11'),
])

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper
import groovy.transform.Field
import parsers.*
import poi.*

if( args.size() == 0 ) {
    println "You must specify which pdf file to process"
    return 1
}

def doc = PDDocument.load( args[0] )
if (doc.isEncrypted()) {
    doc.decrypt("");
    doc.setAllSecurityToBeRemoved(true);
}

def workbook = new Workbook();
def worksheet = workbook.createWorksheet();

def index = 1
new PDFTextStripper().getText(doc).eachLine { line ->
    def parser =  detectExpenseLine(line)
    if(!parser) return

    def expense = parser.parse(line)
    worksheet.set("A${index}", expense.date);
    worksheet.set("B${index}", expense.description);
    worksheet.set("C${index}", expense.currency);
    worksheet.set("D${index}", expense.amount);

    index++
}

doc.close()
workbook.write(System.out)

@Field lineParsers = [visa: new VisaLineParser(), galicia: new GaliciaLineParser()]
def detectExpenseLine( line ) {
     // VISA HOME BANKING PDF FORMAT
    if(line =~ / \d\d \d\d\d\d\d\d/)
        return lineParsers.visa

    if(line =~ /^[ ]+\d\d\.\d\d\.\d\d /)
        return lineParsers.galicia
}
