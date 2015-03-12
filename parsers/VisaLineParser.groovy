package parsers

class VisaLineParser implements LineParser {
    def lastDate = new Date()

    def transformMonth(month) {
        def months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septie.", "Octubre", "Noviem.", "Diciem."]
        months.indexOf(month)
    }
    
    HashMap parse(String line) {
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
}