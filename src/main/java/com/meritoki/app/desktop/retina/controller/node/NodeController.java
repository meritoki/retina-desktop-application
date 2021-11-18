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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

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

	public static File zip(List<File> files, String filename) {
		File zipfile = new File(filename);
		try (OutputStream fo = Files.newOutputStream(Paths.get(zipfile.getAbsolutePath()));
				ArchiveOutputStream o = new ZipArchiveOutputStream(fo)) {
			for (File f : files) {
				logger.info("zip("+files.size()+","+filename+") f="+f);
				// maybe skip directories for formats like AR that don't store directories
				ArchiveEntry entry = o.createArchiveEntry(f, f.getName());
				// potentially add more flags to entry
				o.putArchiveEntry(entry);
				if (f.isFile()) {
					try (InputStream i = Files.newInputStream(f.toPath())) {
						IOUtils.copy(i, o);
					}
				}
				o.closeArchiveEntry();
			}
			o.finish();
			o.close();
			return zipfile;
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		return null;
	}
//	public static File zip(List<File> files, String filename) {
//	    File zipfile = new File(filename);
//	    // Create a buffer for reading the files
//	    byte[] buf = new byte[4096];
//	    try {
//	        // create the ZIP file
//	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
//	        // compress the files
//	        for(int i=0; i<files.size(); i++) {
//	        	logger.info("zip("+files.size()+","+filename+") files.get("+i+")="+files.get(i));
//	            FileInputStream in = new FileInputStream(files.get(i).getCanonicalPath());
//	            // add ZIP entry to output stream
//	            out.putNextEntry(new ZipEntry(files.get(i).getName()));
//	            // transfer bytes from the file to the ZIP file
//	            int len;
//	            while((len = in.read(buf)) > 0) {
//	                out.write(buf, 0, len);
//	            }
//	            // complete the entry
//	            out.finish();
//	            out.flush();
//	            out.closeEntry();
//	            in.close();
//	        }
//	        // complete the ZIP file
//	        out.finish();
//	        out.close();
//	        return zipfile;
//	    } catch (IOException ex) {
//	    	ex.printStackTrace();
//	        System.err.println(ex.getMessage());
//	    }
//	    return null;
//	}

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
				PDDocument pdfDocument =  PDDocument.load(pdf);//Loader.loadPDF(pdf);
				PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
				PDPageTree pdPageList = pdfDocument.getPages();
				fileArray = new File[pdPageList.getCount()];
				String fileName = pdf.getName().replace(".pdf", "");
				int pageNumber = 0;
				for (int i = 0; i < pdPageList.getCount(); i++) {
					PDPage page = pdPageList.get(i);
					File outputFile = new File(
							destinationDirectory + File.separatorChar + fileName + "_" + pageNumber + ".jpg");
					if (!outputFile.exists()) {
						BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber, 300, ImageType.RGB);
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