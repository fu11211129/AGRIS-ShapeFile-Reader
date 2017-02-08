package com.BIGRS_speed.files.shp;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Locale;

import com.BIGRS_speed.files.ShapeFileReader;
import com.BIGRS_speed.shapeFile.ShapeFile;

public class SHP_File extends ShapeFileReader {
	
	/**
	 *  @protected ShapeFile parent_shapefile;
		@protected File file;
		@protected ByteBuffer bb;
	 */
	  
	  /** enable/disable general info-logging. */
	  public static boolean LOG_INFO = true;
	  /** enable/disable logging of the header, while loading. */
	  public static boolean LOG_ONLOAD_HEADER = true;
	  /** enable/disable logging of the content, while loading. */
	  public static boolean LOG_ONLOAD_CONTENT = true;
	  
	  
	  private SHP_Header header;
	  private ArrayList<ShpShape> shapes = new ArrayList<ShpShape>(); // works independent of any *.shx file.

	  
	  public SHP_File( ShapeFile parent_shapefile, File file ) throws Exception{
	    super(parent_shapefile, file);
	  }


	  @Override
	  public void read() throws Exception{
	    // READ HEADER
	    header = new SHP_Header(parent_shapefile, file); 
	    header.read(bb);
	    
	    if( LOG_ONLOAD_HEADER )
	      printHeader();
	    
	    ShpShape.Type shape_type = header.getShapeType();

	    // READ CONTENT (depends on the Shape.Type)
	    if( shape_type == ShpShape.Type.NullShape ){
	      ;// TODO: handle NullShapes
	    } else if( shape_type.isTypeOfPolygon   ()) { while( bb.position() != bb.capacity()) shapes.add( new ShpPolygon(shape_type)   .read(bb) );
	    } else if( shape_type.isTypeOfPolyLine  ()) { while( bb.position() != bb.capacity()) shapes.add( new ShpPolyLine(shape_type)  .read(bb) );
	    } else if( shape_type.isTypeOfPoint     ()) { while( bb.position() != bb.capacity()) shapes.add( new ShpPoint(shape_type)     .read(bb) );
	    } else if( shape_type.isTypeOfMultiPoint()) { while( bb.position() != bb.capacity()) shapes.add( new ShpMultiPoint(shape_type).read(bb) );
	    } else if (shape_type == ShpShape.Type.MultiPatch){
	      System.err.println("(ShapeFile) Shape.Type.MultiPatch not supported at the moment.");
	    }

	    if( LOG_ONLOAD_CONTENT )
	      printContent();
	    
	    if( LOG_INFO )
//	      System.out.println("(ShapeFile) loaded *.shp-File: \""+file.getName()+"\",  shapes="+shapes.size()+"("+shape_type+")");
	      System.out.printf("(ShapeFile) loaded File: \"%s\", records=%d (%s-Shapes)\n", file.getName(), shapes.size(), shape_type);
	  }
	  

	  public SHP_Header getHeader(){
	    return header;
	  }
	  /**
	   * get the shapes of the file as an ArrayList.<br>
	   * <pre>
	   * elements can be of type (proper casting!):
	   * ShpPoint
	   * ShpMultiPoint
	   * ShpPolygon
	   * ShpPolyLine
	   * </pre>
	   * @return ArrayList with elements of type: ShpShape
	   */
	  public ArrayList<ShpShape> getShpShapes(){
	    return shapes;
	  }

	  @Override
	  public void printHeader() {
	    header.print();
	  }


	  @Override
	  public void printContent() {
	    System.out.printf(Locale.ENGLISH, "\n");
	    System.out.printf(Locale.ENGLISH, "________________________< CONTENT >________________________\n");
	    System.out.printf(Locale.ENGLISH, "  FILE: \"%s\"\n", file.getName());
	    System.out.printf(Locale.ENGLISH, "\n");
	    for( ShpShape shape: shapes ){
	      shape.print();
	    }
	    System.out.printf(Locale.ENGLISH, "\n");
	    System.out.printf(Locale.ENGLISH, "________________________< /CONTENT >________________________\n");
	  }
	}
