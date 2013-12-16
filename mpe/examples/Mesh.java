package mpe.examples;

import processing.core.*;

import java.util.*;

public class Mesh {

	PApplet p; 
	
	MeshCube[][] mcubes;
	
	int ns = 1; //noise seed
	float xoff;
	float yoff;
	
	int startx;
	int starty;
	
	int xScale;
	int yScale;
	float zScale;
	float currScale;
	int xNumSegments;
	int yNumSegments;
	
	

	float xySpeed;
	
	float arz;
	float angle;
	PVector target;
	
	Mesh(PApplet p){
		
		xoff = 0;
		yoff = 0;
		
		// frameRate 4: -80,-40,300,300,90,10
		
		startx = -60;
		starty = -30;
		
		xScale = 370;
		yScale = 370;
		zScale = 4000;
		currScale = 0.3f;
		xNumSegments = 75;
		yNumSegments = 12 ;
		
		angle = 0f;
		//change this to 0.3f if this doens't work
		xySpeed = -0.01f;
		angle = -0.5f;
		arz = 0.f;
		
		this.p = p;
		mcubes = new MeshCube[p.abs(starty) + yNumSegments][p.abs(startx)+ xNumSegments];
		
		int county = 0;
		int countx  = 0; 
		
		target = new PVector(5000f,-1300f);
		
		for (int y = starty; y < yNumSegments; y++) {
		    for (int x = startx; x < xNumSegments; x++) {
		
		    	//PVector[] pv = new PVector[4];
		    	PVector pp = new PVector (x*xScale,y*yScale);
		    	mcubes[county][countx] = new MeshCube(p,pp,xScale,yScale);
		    	countx ++;
		    }
		    countx = 0;
		    county++;
		    
		}
	}
	
	
	public void update(){


		  xoff+=xySpeed;//p.cos(angle);
		  yoff+=xySpeed;//xySpeed;//p.sin(angle);
		
		  
		  
		  int county = 0;
		  int countx = 0;
		  
	
			for (int y = starty; y < yNumSegments; y++) {
				
			    for (int x = startx; x < xNumSegments; x++) {
					 float currZ = p.noise(xoff+x*currScale, yoff+y*currScale, currScale);
					 
				//	 p.fill(100+y*10f);
					 
					 currZ = p.noise(xoff+x*currScale, yoff+y*currScale, currScale);
			    	
			    	mcubes[county][countx].top[0] = new PVector (0, 0, currZ*zScale);
		   			mcubes[county][countx].top[1] = new PVector(0, 1, p.noise(xoff+x*currScale, yoff+(y+1)*currScale,currScale)*zScale);
		   			mcubes[county][countx].top[2] = new PVector(1, 1, p.noise(xoff+(x+1)*currScale, yoff+(y+1)*currScale, currScale)*zScale);
	 				mcubes[county][countx].top[3] = new PVector(1, 0, p.noise(xoff+(x+1)*currScale, yoff+y*currScale, currScale)*zScale);
			    
	 				countx++;
			    }
			    countx = 0;
			    county++;
			}
			
		
	}
	
	public void scaleZ(float z){
	
		this.zScale = z;
	
	}
	
	public void scaleWave(float w){
		
		  currScale = w;
	}
	
	
	public void display(){
		
		for (int y = 0; y < yNumSegments + p.abs(starty); y++) {
		    for (int x = 0; x < xNumSegments + p.abs(startx); x++) {
		    	p.fill(y*1.5f + 30f);
		    	p.stroke(255,255,255);
		    	mcubes[y][x].display();
		    }
		}
		
		
	}
	
	public void scale(float n){
		
		for (int y = 0; y < yNumSegments + p.abs(starty); y++) {
		    for (int x = 0; x < xNumSegments + p.abs(startx); x++) {
	
		    	mcubes[y][x].scale(n);
		    }
		}
		
	}

	public void pull(float arz){
		
		//target = new PVector(mc.pos.x, mc.pos.y,arz);
	
		this.arz = arz;
		
		
		for (int y = 0; y < yNumSegments + p.abs(starty); y++) {
		    for (int x = 0; x < xNumSegments + p.abs(startx); x++) {
	
		    	MeshCube thisCube = mcubes[y][x];
		    	PVector[] thisPos = new PVector[4];
		    	
		    	thisPos[0] = thisCube.pos.get();
		    	thisPos[1] = new PVector(thisPos[0].x,thisPos[0].y+yScale);
		    	thisPos[2] = new PVector(thisPos[0].x + xScale , thisPos[0].y + yScale);
		    	thisPos[3] = new PVector(thisPos[0].x + xScale,thisPos[0].y);
		    	
//		    	for ( int i=0; i<thisPos.length; i++){
//		    		thisPos[i] = new PVector(thisCube.top[i].x, thisCube.top[i].y);
//		    	}
//		    	
//		    	
		    	
		        PVector current = new PVector(thisPos[0].x,thisPos[0].y);
		        PVector nextY =  new PVector(thisPos[1].x,thisPos[1].y);
		        PVector nextXY = new PVector(thisPos[2].x,thisPos[2].y);
		        PVector nextX = new PVector(thisPos[3].x,thisPos[3].y);


		        
		        float dist = p.dist(current.x, current.y, target.x, target.y);
		        float distx = p.dist(nextX.x, nextX.y, target.x, target.y);
		        float disty = p.dist(nextY.x, nextY.y, target.x, target.y);
		        float distxy = p. dist(nextXY.x, nextXY.y, target.x, target.y);
		        
		        

		       // float distMax = - p.dist(15000,15000,target.x,target.y);
		        
		        
		        
		        float zz = 0f; 
		        float zzx = 0f;
		        float zzy = 0f;
		        float zzxy = 0f;
		        

		        
		          //fill(255, 0, 0);
		            zz = -arz/(dist);
		            zzx = -arz/(distx);
		            zzy = -arz/(disty);
		            zzxy =-arz/(distxy);
		            
		             
		            thisCube.top[0].z += (zz/50.f);
		            thisCube.top[1].z += (zzy/50.f);
		            thisCube.top[2].z += (zzxy/50.f);
		            thisCube.top[3].z += (zzx/50.f); 
		            
		            
		    } 
		    	
		}
		
	}
	

	
	

}
