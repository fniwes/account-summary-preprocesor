package poi
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFCellStyle

class Worksheet {
	Workbook workbook = null
	HSSFSheet poiSheet = null
	HSSFCellStyle poiCellStyle = null


	Worksheet(Workbook workbook) {
		this.workbook = workbook
		poiSheet = workbook.poiWorkbook.createSheet()

		def createHelper = workbook.poiWorkbook.getCreationHelper()
		poiCellStyle = workbook.poiWorkbook.createCellStyle()
		poiCellStyle.dataFormat = createHelper.createDataFormat().getFormat("m/d/yy")
	}

	void set(String cell, value) {
		char column = cell[0]
		int row = cell[1..-1] as int

		def poiRow = poiSheet.getRow(row-1)
		if(!poiRow) poiRow = poiSheet.createRow(row-1)

		def poiCell = poiRow.getCell(columnToIndex(column))
		if(!poiCell) poiCell = poiRow.createCell(columnToIndex(column))

		poiCell.setCellValue(value)
		if(value instanceof Date) poiCell.cellStyle = poiCellStyle
	}

	private int columnToIndex(char column) {
		def index = column - 65
		return index
	}
}