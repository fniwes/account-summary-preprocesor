@Grapes([
    @Grab(group='org.apache.pdfbox', module='pdfbox', version='1.8.8'),
    @Grab(group='org.bouncycastle', module='bcprov-jdk16', version='1.46')
])

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper
import groovy.transform.Field
import parsers.*

if( args.size() == 0 ) {
    println "You must specify which pdf file to process"
    return 1
}

def doc = PDDocument.load( args[0] )
if (doc.isEncrypted()) {
    doc.decrypt("");
    doc.setAllSecurityToBeRemoved(true);
}

new PDFTextStripper().getText(doc).eachLine { line ->
    def parser =  detectExpenseLine(line)
    if(!parser) return

    def expense = parser.parse(line)
    println "${expense.date.format('yyyy/MM/dd')};${expense.description};${expense.currency};${expense.amount}"
}

doc.close()

@Field lineParsers = [visa: new VisaLineParser()]
def detectExpenseLine( line ) {
     // VISA HOME BANKING PDF FORMAT
    if(line =~ / \d\d \d\d\d\d\d\d/)
        return lineParsers.visa

}
