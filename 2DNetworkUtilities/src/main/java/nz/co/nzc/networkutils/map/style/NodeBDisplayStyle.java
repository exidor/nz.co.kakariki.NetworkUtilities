package nz.co.nzc.networkutils.map.style;
/*
 * This file is part of 2DNetworkUtilities.
 *
 * 2DNetworkUtilities is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 3 of the 
 * License, or (at your option) any later version.
 *
 * 2DNetworkUtilities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory;

public class NodeBDisplayStyle extends DisplayStyle {
	
	 static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
	 static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /**
     * Create a Style to draw polygon features with a thin blue outline and
     * a cyan fill
     */
    protected Style createPolyStyle() {

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.RED),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.MAGENTA),
                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    
    /**
     * Create a Style to draw line features as thin blue lines
     */
    private Style createLineStyle(Color colour) {
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(colour),
                filterFactory.literal(10));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geometry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    
    protected Style createNormalLineStyle(){
    	return createLineStyle(NODEB_LINE_NORMAL);
    }
    protected Style createHighlightLineStyle(){
    	return createLineStyle(NODEB_LINE_BRIGHT);
    }
    protected Style createSelectLineStyle(){
    	return createLineStyle(NODEB_LINE_SELECT);
    }

    /**
     * Create a Style to draw point features as circles with blue outlines
     * and cyan fill
     */
    protected Style createPointStyle(Color colour) {

		Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getTriangleMark();

        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(colour), filterFactory.literal(5)));

        mark.setFill(styleFactory.createFill(filterFactory.literal(colour)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(10));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    
    protected Style createNormalPointStyle(){
    	return createPointStyle(NODEB_POINT_NORMAL);
    }
    protected Style createHighlightPointStyle(){
    	return createPointStyle(NODEB_POINT_BRIGHT);
    }
 
}
