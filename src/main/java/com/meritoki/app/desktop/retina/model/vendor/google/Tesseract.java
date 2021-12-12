package com.meritoki.app.desktop.retina.model.vendor.google;

import java.awt.image.BufferedImage;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.Text;
import com.meritoki.app.desktop.retina.model.vendor.Product;

public class Tesseract extends Product {
	
	private net.sourceforge.tess4j.Tesseract tesseract;// = new net.sourceforge.tess4j.Tesseract();

	public Tesseract() {
		tesseract = new net.sourceforge.tess4j.Tesseract();
		tesseract.setDatapath("./lib/");
		tesseract.setLanguage("eng");
		tesseract.setTessVariable("tessedit_char_whitelist", ".,0123456789");
		tesseract.setTessVariable("tessedit_char_blacklist", "!?@#$%&*()<>_-+=/:;'\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
	}
	
	@Override
	public void execute() throws Exception {
		if(this.model != null) {
			Document document = this.model.document;
			for(Page p:document.getPageList()) {
				for(Shape s:p.getGridShapeList()) {
					BufferedImage image = document.getShapeBufferedImage(p.getScaledBufferedImage(this.model), s);
					if(image != null) {
						Text t = new Text();
						t.setValue(this.ocr(image));
						s.addText(t);
						this.mainFrame.init();
					} 
				}
			}
		} else {
			throw new Exception("this.model = null");
		}
	}
	
	public String ocr(BufferedImage bufferedImage) throws Exception {
		return tesseract.doOCR(bufferedImage);
	}
}
