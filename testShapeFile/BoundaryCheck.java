package com.BIGRS_speed.testShapeFile;

import java.util.ArrayList;

import com.BIGRS_speed.files.dbf.DBF_File;
import com.BIGRS_speed.files.shp.SHP_File;
import com.BIGRS_speed.files.shp.ShpPolyLine;
import com.BIGRS_speed.files.shp.ShpShape;
import com.BIGRS_speed.files.shx.SHX_File;
import com.BIGRS_speed.shapeFile.ShapeFile;

class Coordinate {
	public double latitude;
	public double longitude;
	
	public Coordinate(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Coordinate() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

public class BoundaryCheck {
	
	private boolean onSegment(Coordinate p, Coordinate q, Coordinate r) {
		if(q.latitude <= Math.max(p.latitude, r.latitude) && 
				q.latitude >= Math.min(p.latitude, r.latitude) && 
				q.longitude <= Math.max(p.longitude, r.longitude) && 
				q.longitude >= Math.min(p.longitude, r.longitude))
		return true;
		
		return false;
	}
	
	private int orientation(Coordinate p, Coordinate q, Coordinate r) {
		double val = (q.longitude - p.longitude) * (r.latitude - q.latitude) - 
				(q.latitude - p.latitude) * (r.longitude - q.longitude);
		
		if(Math.abs(val - 0) < 0.000001) return 0;
		return (val > 0.0)? 1: 2;
	}
	
	private boolean doIntersect(Coordinate p1, Coordinate q1, Coordinate p2, Coordinate q2) {
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);
		
		if(o1 != o2 && o3 != o4) return true;
		
		if(o1 == 0 && onSegment(p1, p2, q1)) return true;
		if(o2 == 0 && onSegment(p1, q2, q1)) return true;
		if(o3 == 0 && onSegment(p2, p1, q2)) return true;
		if(o4 == 0 && onSegment(p2, q1, q2)) return true;
		
		return false;
	}
	
	private boolean isInside(Coordinate polygon[], Coordinate p) {
		int n = polygon.length;
		if(n < 3) return false;
		
		Coordinate extreme = new Coordinate(Integer.MAX_VALUE, p.longitude);
		
		int count = 0, i = 0;
		
		do {
			int next = (i + 1) % n;
			
			if(doIntersect(polygon[i], polygon[next], p, extreme)) {
				if(orientation(polygon[i], p, polygon[next]) == 0) {
					return onSegment(polygon[i], p, polygon[next]);
				}
				count++;
			}
			i = next;
		} while(i != 0);
		
		return (count & 1) == 1? true: false;
	}
	
	private Coordinate[] getBoundaryCoordinates() throws Exception {
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
	    ShapeFile shapefile = new ShapeFile("D:\\SPH RA\\Baltimore_City_boundary\\", "baltimore").READ();
	    ShpShape.Type shape_type = shapefile.getSHP_shapeType();
	    int number_of_shapes = shapefile.getSHP_shapeCount();
	    
//	    System.out.println(shape_type);
//	    System.out.println(number_of_shapes);
	    
	    ShpPolyLine spl = shapefile.getSHP_shape(0);
	    double[][] vertex = spl.getPoints();	
	    
	    Coordinate[] polygon = new Coordinate[vertex.length];
	    for(int i=0; i<vertex.length; ++i) {
	    	polygon[i] = new Coordinate(vertex[i][1], vertex[i][0]);
	    }
	    return polygon;
	}

	public static void main(String[] args) throws Exception {
		USRdsDemo usrd = new USRdsDemo();
		ArrayList<String> list = usrd.extractCoordinates();
    	double[][] btm_rds = usrd.parse(list);
    	
    	BoundaryCheck bc = new BoundaryCheck();
    	
    	Coordinate[] polygon = bc.getBoundaryCoordinates();
    	
    	System.out.println(btm_rds.length);
    	int e = 0;
    	for(int i=0; i<btm_rds.length; ++i) {
    		Coordinate c = new Coordinate(btm_rds[i][0], btm_rds[i][1]);
    		//System.out.println(c.latitude + "," + c.longitude);
    		if(bc.isInside(polygon, c)) {
    			++e;
    			System.out.println(c.latitude + "," + c.longitude);
    		}
    	}
    	System.out.println(e);
	}

}
