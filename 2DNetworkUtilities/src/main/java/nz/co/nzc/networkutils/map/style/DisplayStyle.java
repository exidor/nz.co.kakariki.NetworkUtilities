package nz.co.nzc.networkutils.map.style;

import java.awt.Color;

import org.geotools.styling.Style;

public abstract class DisplayStyle {

	//Color.getHSBColor(33,66,33);
	public static final Color NODEB_POINT_NORMAL =  Color.getHSBColor(0.64f,0.40f,0.80f);//light blue
	public static final Color NODEB_POINT_BRIGHT =  Color.getHSBColor(0.64f,0.80f,0.80f);//blue
	
	public static final Color NODEB_LINE_NORMAL =   Color.getHSBColor(0.64f,0.40f,0.80f);//light blue
	public static final Color NODEB_LINE_BRIGHT =   Color.getHSBColor(0.64f,0.80f,0.80f);//blue (target)
	public static final Color NODEB_LINE_SELECT =   Color.getHSBColor(0.33f,0.80f,0.80f);//green (source)
	
	public static final Color SECTOR_POINT_NORMAL = Color.getHSBColor(0.64f,0.40f,0.80f);//light blue
	public static final Color SECTOR_POINT_BRIGHT = Color.getHSBColor(0.64f,0.80f,0.80f);//blue
	
	public static final Color SECTOR_LINE_NORMAL =  Color.getHSBColor(0.00f,0.00f,0.60f);//light grey
	public static final Color SECTOR_LINE_BRIGHT =  Color.getHSBColor(0.11f,1.00f,1.00f);//orange
	public static final Color SECTOR_LINE_SELECT =  Color.getHSBColor(0.00f,1.00f,1.00f);//red
	
	public enum StyleType {
		Normal,Highlight,Select;
	}
	
	public Style getPointStyle(StyleType st){
    	switch(st){
    	case Normal: return createNormalPointStyle();
    	case Highlight: return createHighlightPointStyle();
    	}
		return null;
    }
	
	public Style getLineStyle(StyleType st){
    	switch(st){
    	case Normal: return createNormalLineStyle();
    	case Highlight: return createHighlightLineStyle();
    	case Select: return createSelectLineStyle();
    	}
		return null;
    }
	
	public Style getPolyStyle(StyleType st){
    	switch(st){
    	case Normal: return createPolyStyle();
    	case Highlight: return createPolyStyle();
    	}
		return null;
    }
    

	protected abstract Style createNormalPointStyle();
	protected abstract Style createHighlightPointStyle();
    protected abstract Style createNormalLineStyle();
    protected abstract Style createHighlightLineStyle();
    protected abstract Style createSelectLineStyle();
    protected abstract Style createPolyStyle();
	
	
}
