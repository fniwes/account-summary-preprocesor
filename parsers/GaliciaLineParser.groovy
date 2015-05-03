package parsers

class GaliciaLineParser implements LineParser {
    HashMap parse(String line) {
        def description = line[31..80].trim()
        def amount = parseAmount(line[-10..-1])
        def currency = description.contains("USD") ? "USD" : "ARS"
        
        def date = line[7..8].trim()
        def month = line[10..11].trim()
        def year = line[13..14].trim()
        def lastDate = new Date(year.toInteger() + 100, month.toInteger() - 1, date.toInteger())

        def result = [description: description, amount: amount, currency: currency, date: lastDate.clone()]
        return result;
    }

    float parseAmount( String amount ) {
        String strAmount = amount.trim()
        def isNegative = strAmount[-1..-1] == "-"

        if(isNegative) strAmount = strAmount[0..-2]
        strAmount = strAmount.replace(".", "")
        strAmount = strAmount.replace(",", ".")

        float floatAmount = 0
        try {
            floatAmount = strAmount.toFloat()
        }
        catch(Exception) {
            System.err.println("An amount is not a number. Value: ${amount}")
        }
        if(isNegative) floatAmount *= -1

        return floatAmount.round(2)
    }
}