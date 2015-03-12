package parsers

class GaliciaLineParser implements LineParser {
    HashMap parse(String line) {
        def description = line[31..80].trim()
        def amount = line[-10..-1].trim()
        def currency = description.contains("USD") ? "USD" : "ARS"
        
        def date = line[7..8].trim()
        def month = line[10..11].trim()
        def year = line[13..14].trim()
        def lastDate = new Date(year.toInteger() + 100, month.toInteger() - 1, date.toInteger())

        def result = [description: description, amount: amount, currency: currency, date: lastDate.clone()]
        return result;
    }
}