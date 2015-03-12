@Grapes([
    @Grab(group='org.apache.pdfbox', module='pdfbox', version='1.8.8'),
    @Grab(group='org.bouncycastle', module='bcprov-jdk16', version='1.46')
])

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper
import groovy.transform.Field

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
    if(!isExpenseLine(line)) return
    def expense = parseLine(line)
    
    println "${expense.date.format('yyyy/MM/dd')};${expense.description};${expense.currency};${expense.amount}"
}

doc.close()

def isExpenseLine( line ) {
    line =~ / \d\d \d\d\d\d\d\d/
}

@Field lastDate = new Date()
def parseLine( line ) {
    def description = line[24..58].trim()
    def amount = line[-10..-1].trim()
    def currency = description.contains("USD") ? "USD" : "ARS"
    
    def date = line[11..12].trim()
    def month = line[3..9].trim()
    def year = line[0..1].trim()

    if(month) lastDate.setMonth( transformMonth(month) )
    if(year) lastDate.setYear(year.toInteger() + 100)
    lastDate.setDate(date.toInteger())
    
    [description: description, amount: amount, currency: currency, date: lastDate.clone()]
}

def transformMonth(month) {
    def months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septie.", "Octubre", "Noviem.", "Diciem."]
    months.indexOf(month)
}