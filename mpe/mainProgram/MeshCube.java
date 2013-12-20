package mpe.examples;

import processing.core.*;

public class MeshCube {
	
	PApplet p;
	PVector pos;
	PVector dir;
	
	PVector[] top;
	PVector[] left;
	PVector[] right;
	PVector[] front;
	PVector[] back;
	
	float xScale;
	float yScale;
	
	float originXS;
	float originYS;
	
	float baseZ;
	
	boolean posSet;
	boolean pull;
	
	public MeshCube(PApplet p, PVector pos,float xScale,float yScale){
		
		
		this.p = p;
		this.pos = pos;
	//	p.println("hello");
		
		baseZ = yScale;
		posSet = false;
		
		this.xScale = xScale;
		this.yScale = yScale;
		
		
		this.top = new PVector[4];
		this.left = new PVector[4];
		this.right = new PVector[4];
		this.front = new PVector[4];
		this.back = new PVector[4];
		
		this.originXS = xScale;
		this.originYS = xScale;
		
		this.dir = new PVector(0,0,0);
		
		
	}
	
	public void update(){
		
		pos.add(dir);

	}
	
	public void display(){
	
		//p.noStroke();
		p.pushMatrix();
		p.translate(pos.x,pos.y,0);
		
		p.beginShape(p.QUAD);
		
		//order of quads : 00 0y xy x0
		for(int i = 0; i< top.length; i++){
			p.vertex(top[i].x *xScale ,top[i].y * yScale,top[i].z);
		}
		p.endShape();
		
		
		
	
		//front
		p.beginShape(p.QUAD);
		p.vertex(0,0,top[0].z - baseZ);
		p.vertex(xScale,0,top[3].z - baseZ);
		p.vertex(xScale,0,top[3].z);
		p.vertex(0,0,top[0].z);
		
		p.endShape();
		
		//back
	
		p.beginShape(p.QUAD);
		p.vertex(0,yScale,top[1].z-baseZ);
		p.vertex(xScale,yScale,top[2].z-baseZ);
		p.vertex(xScale,yScale,top[2].z);
		p.vertex(0,yScale,top[1].z);
		p.endShape();
		
		
		//left
			
		p.beginShape(p.QUAD);
		p.vertex(0,0,top[0].z - baseZ);
		p.vertex(0,yScale,top[1].z - baseZ);
		p.vertex(0,yScale,top[1].z);
		p.vertex(0,0,top[0].z);
		p.endShape();
		
		//right

		p.beginShape(p.QUAD);
		p.vertex(xScale,0,top[3].z - baseZ);
		p.vertex(xScale,yScale,top[2].z - baseZ);
		p.vertex(xScale,yScale,top[2].z);
		p.vertex(xScale,0,top[3].z);
		p.endShape();		
	
		p.popMatrix();
		

	}
	
	public void scale(float num){
		
		xScale+=num*((float)(xScale/yScale));
		yScale+=num ;
		
		xScale = p.constrain(xScale,0,originXS);
		yScale = p.constrain(yScale,0,originYS);

		
	}


}
