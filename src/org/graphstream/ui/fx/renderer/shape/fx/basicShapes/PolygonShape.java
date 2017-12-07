package org.graphstream.ui.fx.renderer.shape.fx.basicShapes;

import org.graphstream.ui.fx.Backend;
import org.graphstream.ui.fx.FxDefaultCamera;
import org.graphstream.ui.fx.renderer.AreaSkeleton;
import org.graphstream.ui.fx.renderer.Skeleton;
import org.graphstream.ui.fx.renderer.shape.fx.baseShapes.PolygonalShape;
import org.graphstream.ui.fx.util.AttributeUtils;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;

public class PolygonShape extends PolygonalShape implements AttributeUtils {
	Point3[] theValues = null ;
	Point3 minPoint = null ;
	Point3 maxPoint = null ;
	Object valuesRef = null ;
	
	@Override
	public void configureForElement(Backend bck, GraphicElement element, Skeleton skel, FxDefaultCamera camera) {
		super.configureForElement(bck, element, skel, camera);
		
        if(element.hasAttribute( "ui.points" )) {
			Object oldRef = valuesRef;
			valuesRef = element.getAttribute("ui.points");
			// We use valueRef to avoid
			// recreating the values array for nothing.
			if( ( theValues == null ) || ( oldRef != valuesRef ) ) {
				theValues = getPoints(valuesRef);
				
				if(skel instanceof AreaSkeleton) {
				    Tuple<Point3, Point3> tuple = boundingBoxOfPoints(theValues);

				    minPoint = tuple.x;
				    maxPoint = tuple.y;
				}
			}
		
			AreaSkeleton ninfo = (AreaSkeleton)skel;
			ninfo.theSize.set(maxPoint.x-minPoint.x, maxPoint.y-minPoint.y);
			area.theSize.copy(ninfo.theSize);
		}
	}
	
	@Override
	public void make(Backend backend, FxDefaultCamera camera) {
		double x = area.theCenter.x;
		double y = area.theCenter.y;
        double n = theValues.length;
        
        theShape().reset();
        
        if(n > 0) {
        	theShape().moveTo(x+theValues[0].x, y+theValues[0].y);
        	for(int i = 0 ; i < n ; i++) {
        	    theShape().lineTo(x+theValues[i].x, y+theValues[i].y);
        	}
        }		
	}
	
	@Override
	public void makeShadow(Backend backend, FxDefaultCamera camera) {
		double n = theValues.length;
		double x  = area.theCenter.x + shadowable.theShadowOff.x;
		double y  = area.theCenter.y + shadowable.theShadowOff.y;

        theShape().reset();
        
        if(n > 0) {
        	theShape().moveTo(x+theValues[0].x, y+theValues[0].y);
        	for(int i = 0 ; i < n ; i++) {
        	    theShape().lineTo(x+theValues[i].x, y+theValues[i].y);
        	}
        }
	}
}