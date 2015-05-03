package poi
import org.apache.poi.hssf.usermodel.HSSFWorkbook

class Workbook {
	HSSFWorkbook poiWorkbook = new HSSFWorkbook();

	Worksheet createWorksheet() {
		return new Worksheet(this);
	}

	void write(OutputStream out) {
		poiWorkbook.write(out)
	}
}