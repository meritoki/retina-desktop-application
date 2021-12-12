package com.meritoki.app.desktop.retina.model.vendor.google;

import org.apache.logging.log4j.LogManager;

import com.meritoki.app.desktop.retina.model.vendor.Product;
import com.meritoki.app.desktop.retina.model.vendor.Vendor;

public class Google extends Vendor implements Runnable {
	
	static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Google.class.getName());
	private String product;
	
	public Google() {
		super("google");
		logger.info("Google()");
		this.productMap.put("Tesseract", new Tesseract()); 
	}
	
	public void setProduct(String name) {
		this.product = name;
	}
	
	public Product getProduct(String name) {
		return this.productMap.get(name);
	}
	
	@Override
	public void execute() throws Exception {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		if(this.product != null) {
			Product product = this.getProduct(this.product);
			product.setModel(this.model);
			product.setMainFrame(this.mainFrame);
			if(product != null) {
				try {
					product.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
