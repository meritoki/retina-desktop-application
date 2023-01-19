package com.meritoki.app.desktop.retina.controller.tool;


import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ClassificationCounter {
	
	public static String format = "yyyy-MM-dd HH:mm:ss";

	public static void main(String[] args) {
		String fileName = "i20221202-meteororum-ad-extremum-terrae-classifications.csv";
		String path = "C:\\Users\\Joaquin Rodriguez\\Downloads\\";
		
		System.out.println(importZooniverse(path+fileName));
	}
	
	@JsonIgnore
	public static int importZooniverse(String fileName) {
//		List<String[]> stringArrayList = NodeController.openCsv(fileName);
		FileInputStream inputStream = null;
		Scanner sc = null;
		int count = 0;
		try {
		    inputStream = new FileInputStream(fileName);
		    sc = new Scanner(inputStream, "UTF-8");
		    sc.nextLine();
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        String[] stringArray = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				String dateString = stringArray[7];
				dateString.replace("UTC","");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				try {
					Date referenceDate = sdf.parse("05/10/2022");
					Date date =new SimpleDateFormat(format).parse(dateString);
					if(date.after(referenceDate)) {
						count++;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if (inputStream != null) {
		        try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		return count;
	}

}
