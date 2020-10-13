/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.app.desktop.retina.controller.node;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.meritoki.app.desktop.retina.model.document.Document;

public class NodeController extends com.meritoki.library.controller.node.NodeController {

	private static Logger logger = LogManager.getLogger(NodeController.class.getName());

	public static String getSystemHome() {
		return getUserHome() + getSeperator() + ".retina";
	}

	public static String getDocumentCache() {
		return getSystemHome() + getSeperator() + "document";
	}

	public static String getDocumentCache(String uuid) {
		return getDocumentCache() + getSeperator() + uuid;
	}

	public static String getResourceCache() {
		return getSystemHome() + getSeperator() + "resource";
	}

	public static String getImageCache() {
		return getSystemHome() + getSeperator() + "image";
	}

	public static String getPanoptesHome() {
		return getUserHome() + getSeperator() + ".panoptes";
	}

	public static String getProviderHome() {
		return getSystemHome() + getSeperator() + "provider";
	}
	
	

	public static void saveDocument(String filePath, String fileName, Document document) {
		logger.info("saveDocument(" + filePath + ", " + fileName + ", " + document + ")");
		NodeController.saveJson(filePath, fileName, document);
	}

	public static void saveDocument(File file, Document document) {
		logger.info("saveDocument(" + file + ", " + document + ")");
		NodeController.saveJson(file, document);
	}

	public static Document openDocument(String filePath, String fileName) {
		Document document = (Document) NodeController.openJson(new java.io.File(filePath + "/" + fileName),
				Document.class);
		logger.info("openDocument(" + filePath + ", " + fileName + ") document=" + document);
		return document;
	}

	public static Document openDocument(File file) {
		Document document = (Document) NodeController.openJson(file, Document.class);
		logger.info("openDocument(" + file + ") document=" + document);
		return document;
	}

	public static File[] openPDF(String filePath, File pdf) {
		File[] fileArray = new File[0];
		try {
			String destinationDirectory = filePath;
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
				for (int i = 0; i < pdPageList.size(); i++) {
					PDPage page = pdPageList.get(i);
					File outputFile = new File(destinationDirectory + fileName + "_" + pageNumber + ".jpg");
					if (!outputFile.exists()) {
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