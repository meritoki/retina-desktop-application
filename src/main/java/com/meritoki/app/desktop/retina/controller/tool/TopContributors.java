package com.meritoki.app.desktop.retina.controller.tool;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TopContributors {
	
	public static Map<String,Integer> userNameMap = new HashMap<String,Integer>();
	

	public static void main(String[] args) {
		String fileName = "20220913-meteororum-ad-extremum-terrae-classifications.csv";
		String path = "/home/jorodriguez/Documents/UTN/File/Project/ACRE/Workspace/utn-acre/zooniverse/";
		Object[] a = importZooniverse(path+fileName);
		for (Object e : a) {
		    System.out.println(((Map.Entry<String, Integer>) e).getKey() + " : "
		            + ((Map.Entry<String, Integer>) e).getValue());
		}
	}
	
	@JsonIgnore
	public static Object[] importZooniverse(String fileName) {
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
				String userName = stringArray[1];
				Integer integer = userNameMap.get(userName);
				if(integer == null) {
					integer = 0;
				} 
				integer += 1;
				userNameMap.put(userName,integer);
				
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
		Object[] a = userNameMap.entrySet().toArray();
		Arrays.sort(a, new Comparator() {
		    public int compare(Object o1, Object o2) {
		        return ((Map.Entry<String, Integer>) o2).getValue()
		                   .compareTo(((Map.Entry<String, Integer>) o1).getValue());
		    }
		});
		return a;
	}

}