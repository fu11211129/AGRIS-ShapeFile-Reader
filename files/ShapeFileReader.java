package com.BIGRS_speed.files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.BIGRS_speed.shapeFile.*;

public abstract class ShapeFileReader {
	
	protected ShapeFile parent_shapefile;
	protected File file;
	protected ByteBuffer bb;
		
	public ShapeFileReader(ShapeFile parent_shapefile, File file) throws IOException{
		this.parent_shapefile = parent_shapefile;
		this.file = file;
		this.bb = ShapeFileReader.loadFile(file);
	}
	  
	public abstract void read() throws Exception;
	  
	public abstract void printHeader();
	public abstract void printContent();
	
	public static ByteBuffer loadFile(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(is);
		byte data[] = new byte[bis.available()];
		bis.read(data);
		bis.close();
		is.close();
		return ByteBuffer.wrap(data);
	}
	  
	public ShapeFile getShapeFile(){
		return parent_shapefile;
	}
		
	public File getFile(){
		return file;
	}
	  
}
