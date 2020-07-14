package com.meritoki.app.desktop.retina.controller.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.meritoki.app.desktop.retina.controller.node.NodeController;

public class PDFController {

	public static void main(String[] args) {
		PDFController.openPDF(new File("./data/image/1905.pdf"));
	}
	
	public static File[] openPDF(File pdf) {
		File[] fileArray = new File[0];
		try {
			String destinationDirectory = NodeController.getImageCache()+NodeController.getSeperator();
			File destinationFile = new File(destinationDirectory);
			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
			}
			if (pdf.exists()) {
				System.out.println("Images copied to Folder: " + destinationFile.getName());
				PDDocument pdfDocument = PDDocument.load(pdf);
				List<PDPage> pdPageList = pdfDocument.getDocumentCatalog().getAllPages();
				fileArray = new File[pdPageList.size()];
				String fileName = pdf.getName().replace(".pdf", "");
				int pageNumber = 1;
				for (int i=0;i<pdPageList.size();i++) {
					PDPage page = pdPageList.get(i);
					File outputFile = new File(destinationDirectory + fileName + "_" + pageNumber + ".jpg");
					if(!outputFile.exists()) {
						BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
						System.out.println("Image Created -> " + outputFile.getName());
						ImageIO.write(image, "jpg", outputFile);
					}
					pageNumber++;
					fileArray[i] = outputFile;
				}
				pdfDocument.close();
				System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
			} else {
				System.err.println(pdf.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileArray;
	}
}
