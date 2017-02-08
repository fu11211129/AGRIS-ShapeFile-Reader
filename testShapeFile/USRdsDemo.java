package com.BIGRS_speed.testShapeFile;

import java.util.ArrayList;

import com.BIGRS_speed.files.dbf.DBF_File;
import com.BIGRS_speed.files.shp.SHP_File;
import com.BIGRS_speed.files.shp.ShpPolyLine;
import com.BIGRS_speed.files.shx.SHX_File;
import com.BIGRS_speed.shapeFile.ShapeFile;
import com.BIGRS_speed.utility.DistanceCalculator;

public class USRdsDemo {
	
	public final static double Baltimore_ymin = 39.195;
	public final static double Baltimore_ymax = 39.721;
	public final static double Baltimore_xmin = -76.897;
	public final static double Baltimore_xmax = -76.322;
	
	public static boolean inBoundary(double y, double x) {
		if(y > Baltimore_ymin && y < Baltimore_ymax && 
				x > Baltimore_xmin && x < Baltimore_xmax) {
			return true;
		}
		
		return false;
	}
	
	public ArrayList<String> extractCoordinates() throws Exception {
		DBF_File.LOG_INFO           = !false;
	    DBF_File.LOG_ONLOAD_HEADER  = false;
	    DBF_File.LOG_ONLOAD_CONTENT = false;
	    
	    SHX_File.LOG_INFO           = !false;
	    SHX_File.LOG_ONLOAD_HEADER  = false;
	    SHX_File.LOG_ONLOAD_CONTENT = false;
	    
	    SHP_File.LOG_INFO           = !false;
	    SHP_File.LOG_ONLOAD_HEADER  = false;
	    SHP_File.LOG_ONLOAD_CONTENT = false;
	      
	    // LOAD SHAPE FILE (.shp, .shx, .dbf)
	    ShapeFile shapefile = new ShapeFile("D:\\SPH RA\\USA_rds\\", "USA_roads").READ();
	    int number_of_shapes = shapefile.getSHP_shapeCount();
	    ArrayList<String> ls = new ArrayList<String> ();
	    
	    Double ymin = 39.195, ymax = 39.721;
	    Double xmin = -76.897, xmax = -76.322;
	    
	    for(int i=0; i<number_of_shapes; ++i) {
	    	ShpPolyLine spl = shapefile.getSHP_shape(i);
	    	double[][] vertex = spl.getPoints();
	    	
	    	for(int j=0; j<vertex.length; ++j) {
	    		double y = vertex[j][1];
	    		double x = vertex[j][0];
    		
	    		if(y > Baltimore_ymin && y < Baltimore_ymax && 
	    				x > Baltimore_xmin && x < Baltimore_xmax) {
	    			StringBuffer sb = new StringBuffer();
	    			//sb.append("\"");
	    			sb.append(y);
	    			sb.append(",");
	    			sb.append(x);
	    			//sb.append("\"");
	    			ls.add(sb.toString());
	    		}
	    	}
	    }
	    
	    return ls;
	}
	
	public double[][] parse(ArrayList<String> ls) throws Exception {
		double[][] crd = new double[ls.size()][2];
		
		int n = crd.length;
		for(int i=0; i<n - 1; ++i) {
			String loc = ls.get(i);
			
			int j = 0; 
			StringBuffer y = new StringBuffer(), x = new StringBuffer();
			while(j < loc.length() && loc.charAt(j) != ',') y.append(loc.charAt(j++));
			++j;
			while(j < loc.length()) x.append(loc.charAt(j++));	
			
			crd[i][0] = Double.parseDouble(y.toString()); 
			crd[i][1] = Double.parseDouble(x.toString());
		}
		
		return crd;
	}
	
	public ArrayList<String> partition(double[][] crd, int e) {
		int n = crd.length;
		ArrayList<String> ls = new ArrayList<String> ();
		
		DistanceCalculator distC = new DistanceCalculator();
		
		for(int i=0; i<n - 1; ++i) {

			double y1 = crd[i][0], x1 = crd[i][1];
			double y2 = crd[i+1][0], x2 = crd[i+1][1];
			double d = distC.distance(y1, x1, y2, x2, "K");
			double k = (double) (y2 - y1) / (x2 - x1);
			
			for(int p=1; p<=e-1; ++p) {
				double a = Math.sqrt(p*d/(e*k*k + 5)) + x1;
				double b = Math.sqrt(p*d*k*k/(e*k*k + 5)) + y1;
				
				String y = Double.toString(b);
				String x = Double.toString(a);
				
				if(!Double.isNaN(a) && !Double.isNaN(b) && inBoundary(b, a)) ls.add(y + "," + x);
			}
		}
		
		return ls;
	}
	 
	public static void main(String[] args) throws Exception {
		USRdsDemo usrd = new USRdsDemo();
		
		ArrayList<String> ls = usrd.extractCoordinates();
		double[][] crd = usrd.parse(ls);
		
		ArrayList<String> expand = usrd.partition(crd, 5);
		for(String s: expand) System.out.println(s);
		//System.out.println(expand.size());
	}
}
