package mpe.examples;

import processing.core.*;

public class ParticleSystem {
    
	  public Particle[] particles; 
	  PApplet p;
	  int ptNum; 
	  PVector center;
	  PVector endPoint;
	  boolean mball;
	  boolean beMirrorball;
	  
	  float a;
	  float mirrorX;
	  float mirrorY;
	  float mirrorZ;
	  float endSpeed;
	  float endAcc;
	  
	  float mirrorballRad;
	  float xAdd;
	  float yAdd;
	  float z;
	  
   ParticleSystem(PVector center,PApplet p){
     
     this.ptNum = 294;//146;//(int)p.random(150,180);
     this.p = p;
     this.center = center.get();
     particles = new Particle[ptNum];
     a = 0.f;
     mirrorX = 0.f;
     z = 0;
     mirrorZ = p.random(0.1f,0.4f);
     mball = false;
     for(int i = 0; i< particles.length; i++){
       particles[i] = new Particle(p); 
     }
     xAdd = 0.01f;
     mirrorY = 0.f;
   }

   
   public void willThisBeMirrorball(boolean b){
	   this.beMirrorball = b;
   }

   public void showup(){
	    for(int i = 0; i< particles.length; i++){
	         particles[i].showup();
	         
	     }
   }
   
   public void update(){   
     for(int i = 0; i< particles.length; i++){
       particles[i].update();
     }
   }
   
   public void changeSize(){   
	     for(int i = 0; i< particles.length; i++){
	       particles[i].changeSize(p.random(-4.f,4.f));
	     }
	   }
 
 
   public void explode(float speed){   
	     for(int i = 0; i< particles.length; i++){
	       particles[i].explode(speed);
	    
	     }
	   }
   
   public void stop(){
	     for(int i = 0; i< particles.length; i++){
		       particles[i].stop();
		     }
   }
   
   
   public void goBack(float speed){
	     for(int i = 0; i< particles.length; i++){
		       particles[i].goBack(speed);
		     }
   }
   
   
   
  public void  display(){
	  p.pushMatrix();
	  
	  	p.translate(center.x, center.y,center.z);
	//  p.rotateX((float)Math.PI/2f);
	  	
	  	if(beMirrorball){
	  		if(mirrorX < Math.PI/2f+mirrorZ) {
	  			mirrorX += xAdd;
	  		}
	  		else{
	  			
	  			if(mirrorballRad > 1500f) mirrorX += xAdd;
	  		}
	  		
	  		if(z < mirrorZ) z+=0.01f;
	  		
	  		mirrorY += yAdd;
	  		
	  	}
	  	p.rotateY(mirrorY);
	  	p.rotateX(mirrorX);

    	  for(int i = 0; i< particles.length; i++){  
    		  particles[i].display();
    	  }   	  
    	  
    	  p.popMatrix();
    	  
  }
  
  public void setMirrorballSize(float msize){
	  this.mirrorballRad = msize;
	  this.yAdd = (float) 10f/mirrorballRad;
  }


  public void goMirrorball() {

	 // p.rotateX((float)(Math.PI/2f));
	  for(int i = 0; i< particles.length; i++){  
		  particles[i].goMirrorball();	
	  } 
	  
	}
  
  public void fastMirrorball(float add){
	  
	  	//yAdd = add;
	  	xAdd = add;
	  	//mirrorX = add;
  }
  
  public void arrangeSize() {

	 // p.rotateX((float)(Math.PI/2f));
	  for(int i = 0; i< particles.length; i++){  
		  particles[i].arrangeSize();	
	  } 
	  
	}
  
  public void funkySize() {

	 // p.rotateX((float)(Math.PI/2f));
	  for(int i = 0; i< particles.length; i++){  
		  particles[i].funkySize();	
	  } 
	  
	}
  
  public void setEndPoint(PVector p){
	  
	  endPoint = p.get();
	  endSpeed = 25f;
	  endAcc = 0.98f;
  }
  
  public void goEndPoint(){
	  
	  beMirrorball = false;
	  if(PVector.dist(endPoint,center)<50){
	  if(mirrorY > 0) mirrorY-= endSpeed/ 3000f*Math.PI;
	  else  mirrorY = 0f;
	  if(mirrorX > 0) mirrorX -= endSpeed/3000f*Math.PI;
	  else mirrorX = 0;
	  }
	   PVector dir = PVector.sub(endPoint,center);
	   dir.div(endSpeed);
	   endSpeed*=endAcc;
	   center.add(dir);

	  for(int i = 0; i< particles.length; i++){  
		  particles[i].goEndPoint();	
	  } 
	  
	  
  }

  
}
