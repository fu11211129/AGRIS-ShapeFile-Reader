package com.BIGRS_speed.testShapeFile;

import com.BIGRS_speed.files.dbf.DBF_File;
import com.BIGRS_speed.files.shp.SHP_File;
import com.BIGRS_speed.files.shp.ShpPolyLine;
import com.BIGRS_speed.files.shp.ShpPolygon;
import com.BIGRS_speed.files.shp.ShpShape;
import com.BIGRS_speed.files.shx.SHX_File;
import com.BIGRS_speed.shapeFile.ShapeFile;

public class ShapeFileDemo {

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
	   
	    ShapeFile shapefile = new ShapeFile("D:\\SPH RA\\streetcl2\\", "street").READ();
	    ShpShape.Type shape_type = shapefile.getSHP_shapeType();
	    System.out.println(shape_type);
	    
	    int no_shape_files = shapefile.getSHP_shapeCount();
	    int e = 0;
	    
	    for(int i=0; i<no_shape_files; ++i) {
	    	ShpPolyLine spl = shapefile.getSHP_shape(i);
	    	double[][] vertex = spl.getPoints();
	    	
	    	if(vertex.length > 2) e += 1;
	    	
//	    	e += vertex.length;
//	    	for(int j=0; j<vertex.length; ++j) {
//	    		System.out.print(vertex[j][1] + "," + vertex[j][0] + "\n");
//	    	} System.out.println();
	    }
	    
	    System.out.println(e);
	}

}
