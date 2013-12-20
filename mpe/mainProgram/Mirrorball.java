package mpe.examples;

import processing.core.*;
import java.util.*;

public class Mirrorball {
	
	PApplet p;
	
	PVector center;
	float rad;
	int totalNum;
	ArrayList<PVector> positions;
	ArrayList<PVector> angles;
	
	
	public Mirrorball(PApplet p,PVector center, float rad){
		this.p = p;
		this.center = center;
		this.rad = rad;
		
		positions = new ArrayList<PVector>();
		angles = new ArrayList<PVector>();
		
		int count = 0;
		
        for(float theta = 0; theta < Math.PI/2.f; theta+= Math.PI/12.f){
            float t  = p.map(theta, 0, (float) (Math.PI/2.f), 1.f, 40.f);
             for(float ang = 0;  ang< Math.PI*2f; ang+= Math.PI*2.f/t){
               
               PVector temp = new PVector((float) (Math.cos(ang)*Math.sin(theta)), 
            		   						(float)(Math.sin(ang) * Math.sin(theta)),
            		   						(float) (Math.cos(theta)));
 
               temp.mult(rad);
              // temp.add(center);
               positions.add(temp);
               angles.add(new PVector(ang,theta));
               count++;
               //temp.mult(rad);
               //pushMatrix();
               //translate(temp.x,temp.y,temp.z);
               //rotateZ(ang);
               //rotateY(theta);
               //box(20,20,20);
               //popMatrix();           
             }
        }
        
        for(float theta = (float)Math.PI/2.f; theta < Math.PI; theta+= Math.PI/12.f){
            float t  = p.map(theta, (float) (Math.PI/2.f),(float)(Math.PI), 40.f, 1.f);
             for(float ang = 0;  ang< Math.PI*2f; ang+= Math.PI*2.f/t){
               
               PVector temp = new PVector((float) (Math.cos(ang)*Math.sin(theta)), 
            		   						(float)(Math.sin(ang) * Math.sin(theta)),
            		   						(float) (Math.cos(theta)));
 
               temp.mult(rad);
              // temp.add(center);
               positions.add(temp);
               angles.add(new PVector(ang,theta));
               count++;
               //temp.mult(rad);
               //pushMatrix();
               //translate(temp.x,temp.y,temp.z);
               //rotateZ(ang);
               //rotateY(theta);
               //box(20,20,20);
               //popMatrix();           
             }
        }    
        
    totalNum = count;
		
	}

	public int getTotalNum(){		
		return totalNum;
	}
	
	public PVector getPosition(int i){
		return positions.get(i);	
	}
	
	public PVector getAngles(int i){
		return angles.get(i);
	}
	
}
