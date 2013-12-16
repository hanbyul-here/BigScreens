package mpe.examples;
import processing.core.*;

public class Particle {
	
	PApplet p;
	
	  PVector start; 
	  PVector dest;
	  PVector pos;
	  PVector exdir;
	  PVector dir;
	  PVector showupSpeed;
	  PVector mirrorDest;
	  PVector mirrorballCenter;
	  PVector randomPosition;
	  
	  float rad;
	  float col;
	  float speed;
	  float speedDiv;
	  float acc; 
	  float originRad;
	  float funkyRad;
	  float radDamper;
	  float multNum;
	  
	  float endSpeed;
	  float endAcc;
	  
	  float ang;
	  float theta;
	  
	  float mirAng;
	  float mirTheta;
	  
	  float a;
	  float ai;
	  
	  boolean mir;
	  boolean setFunky;
	  
	  public Particle(PApplet p){
	    
		this.p = p;
		
	   // this.pos = pos.get();
	    this.dest = new PVector(0,0,0);
	    
		this.pos = new PVector(0,-2000,0);
	    //this.pos = new PVector(dest.x,-1000,dest.z);
	    this.mirrorballCenter = new PVector(0,0,0);
	    
	    rad = p.random(60,80);
	    originRad = 70;
	    funkyRad = 0f;
	    radDamper = 0.8f;
	    multNum = 1.8f;

	    ang = p.random((float) (Math.PI*2.f));
	    theta = p.random((float) (Math.PI));
	    
	    endSpeed = p.random(25f,35f);
	    endAcc = 0.98f;
	    		
	    		
	    exdir = new PVector(p.cos(ang)*p.sin(theta), p.sin(ang)*p.sin(theta),p.cos(theta));
	    speed = p.random(-7.f,7.f);
	    speedDiv = p.random(10,23);
	    exdir.mult(speed);
	    acc = 1.2f;
	    dir = new PVector(0,0,0);
	    showupSpeed = new PVector(0,1,0);
	    
	    col = 0.f;
	    a = 0;//p.random((float) (Math.PI*-2.f),(float) (Math.PI*2.f));
	    ai = speed/23.f;
	    
	    mir = false;
	    setFunky = false;
	    
	    
	  }
	  
	  public void showup(){
		  
		  if(pos.y < dest.y){
			  dir = showupSpeed.get();
			  showupSpeed.mult(1.2f);
		  }else dir = new PVector(0,0,0);
		
	  }
	  
	  public void changeSize(float f){
		  rad += f;
		  
	  }
	  
	  
	  //have to change it to 
	  public void explode(float speed){
		  
		  ai = (float)speed/speedDiv;
		  dir = exdir.get(); 
		  dir.mult(speed*multNum);
		  a+=ai;
		  acc = 1.1f;
		  	  
	  }
	  
	  
	  public void goBack(float speed){
		  ai = (float)speed/speedDiv;
		  a -= ai;
		  dir = exdir.get();
		  dir.mult(-speed);
		  dir.mult(acc);
		  acc+=0.01;
		  
	  }
	  
	  public void stop(){
		  acc = 1.1f;
		  dir = new PVector(0,0,0);
		  ai = 0;
		  
	  }
	  
	  public void display(){
	    
	    p.pushMatrix();
	    p.translate(pos.x,pos.y,pos.z);
	 
	    if(mir) {            
	    	p.rotateZ(mirAng);
	    	p.rotateY(mirTheta);}
	    else{ 
	    	p.rotateX(-a);
	   	    p.rotateY(a);
	   	    p.rotateZ(0f);
	    }
	    float x1 = -rad/2f; float x2 = rad/2f;
	    float y1 = -rad/2f; float y2 = rad/2f;
	    float z1 = -rad/2f; float z2 = rad/2f;
	    
	    // TODO not the least bit efficient, it even redraws lines
	    // along the vertices. ugly ugly ugly!

	    p.beginShape(p.QUADS);

	    
	    // front
	    
	    p.normal(0, 0, 1);
	    p.vertex(x1, y1, z1);
	    p.vertex(x2, y1, z1);
	    p.vertex(x2, y2, z1);
	    p.vertex(x1, y2, z1);

	    // right
	    
	    p.normal(1, 0, 0);
	    p.vertex(x2, y1, z1);
	    p.vertex(x2, y1, z2);
	    p.vertex(x2, y2, z2);
	    p.vertex(x2, y2, z1);

	    // back
	    
	    p.normal(0, 0, -1);
	    p.vertex(x2, y1, z2);
	    p.vertex(x1, y1, z2);
	    p.vertex(x1, y2, z2);
	    p.vertex(x2, y2, z2);

	    // left
	    p.normal(-1, 0, 0);
	    p.vertex(x1, y1, z2);
	    p.vertex(x1, y1, z1);
	    p.vertex(x1, y2, z1);
	    p.vertex(x1, y2, z2);

	    // top
	    p.normal(0, 1, 0);
	    p.vertex(x1, y1, z2);
	    p.vertex(x2, y1, z2);
	    p.vertex(x2, y1, z1);
	    p.vertex(x1, y1, z1);

	    // bottom
	    p.normal(0, -1, 0);
	    p.vertex(x1, y2, z1);
	    p.vertex(x2, y2, z1);
	    p.vertex(x2, y2, z2);
	    p.vertex(x1, y2, z2);

	    p.endShape();
	    p.popMatrix();
	    
	  }
	  
	   public void update(){
	 
		   pos.add(dir);
	     
	   }
	   
	   public void goMirrorball(){
		   mir = true;
		   dir = PVector.sub(mirrorDest,pos);
		   dir.div(30.f);
		//   p.println(dir);
	   }

	   public void setMirrorball(PVector pv,float a,float t){
		   
		   mirrorDest = pv.get();
		   mirAng = a;
		   mirTheta = t;
		   
	   }
	   
	   public void goEndPoint(){
		   mir= false;
		   dir = PVector.sub(dest,pos);
		   dir.div(endSpeed);
		   endSpeed *= endAcc;
		   if(PVector.dist(dest,pos) <20f){
			 //  System.out.println("hello");
		   if(a > 0) a -= endSpeed/10000.f;
		   else a = 0f;
		   }
		   multNum = 15.5f;
		   // if( a > 0.001f)  a -= 0.05f;//(endSpeed/2f);
		   
	   }
	   
	   public void arrangeSize(){
		   
		   if(!setFunky){
			   funkyRad = rad;
			   setFunky = true;
		   }
		   rad = rad * radDamper + (1- radDamper) * originRad;
	   }
	   
	   public void funkySize(){
		   
		   rad = rad * radDamper + (1- radDamper) * funkyRad;
		   setFunky = false;
		   
	   }
	   
	   

}
