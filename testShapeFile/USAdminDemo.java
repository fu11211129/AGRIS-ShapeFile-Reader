package com.BIGRS_speed.testShapeFile;

import com.BIGRS_speed.files.dbf.DBF_File;
import com.BIGRS_speed.files.shp.SHP_File;
import com.BIGRS_speed.files.shp.ShpPolyLine;
import com.BIGRS_speed.files.shp.ShpPolygon;
import com.BIGRS_speed.files.shx.SHX_File;
import com.BIGRS_speed.shapeFile.ShapeFile;


public class USAdminDemo {

	public static void main(String[] args) throws Exception {
		DBF_File.LOG_INFO           = !false;
	    DBF_File.LOG_ONLOAD_HEADER  = false;
	    DBF_File.LOG_ONLOAD_CONTENT = false;
	    
	    SHX_File.LOG_INFO           = !false;
	    SHX_File.LOG_ONLOAD_HEADER  = false;
	    SHX_File.LOG_ONLOAD_CONTENT = false;
	    
	    SHP_File.LOG_INFO           = !false;
	    SHP_File.LOG_ONLOAD_HEADER  = false;
	    SHP_File.LOG_ONLOAD_CONTENT = false;
	   
	    String curDir = System.getProperty("user.dir");
	    String folder = "/data/Gis Steiermark 2010/Bezirke/BezirkeUTM33N/";
	      
	    // LOAD SHAPE FILE (.shp, .shx, .dbf)
	    ShapeFile shapefile = new ShapeFile("D:\\SPH RA\\USA_adm_shp\\", "USA_adm2").READ();
	      
	    ShpPolygon spl = shapefile.getSHP_shape(1194);
	    double[][] vertex = spl.getPoints();
	    System.out.println(vertex.length);
	    for(int i=0; i<1000; ++i) {
	    	System.out.println(vertex[i][1] + "," + vertex[i][0]);
	    }
	}


}
