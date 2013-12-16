package mpe.examples;
import processing.core.*;

public class PopstarCube{
	
	PApplet p;
	PVector pos;
	float rad;
	
	float split;
	float gap;
	float mag;
	float xp;
	float yp;
	
	
	float x1,x2,y1,y2,z1,z2;
	
	  public PopstarCube(PVector pos,float rad,float mag, PApplet p){
	    
		this.p = p;
		this.rad = rad;
	    this.pos = pos.get();
	    
	    this.split = 4f;
	    this.mag = mag;
	    
	    this.x1 = -rad/2f; 
	    this.x2 = rad/2f;
	    this.y1 = -rad/2f; 
	    this.y2 = rad/2f;
	    this.z1 = -rad/2f; 
	    this.z2 = rad/2f;
	    
	    gap = rad / split;
	  }
	   
	  
	  public void display(){
		  

		  xp +=0.1f;
		  yp +=0.1f;

		  p.pushMatrix();
		  p.translate(pos.x, pos.y,pos.z);
		  p.rotateX((float)Math.PI/12.f);
		  p.rotateY((float)Math.PI/11.f);
		  p.noStroke();
		  p.beginShape(p.QUADS);
		      for(float y= y1; y<y2; y+=gap){
		        for(float x= x1; x<x2; x+=gap){
		            
		            float za = p.noise(x + xp ,y + yp)* -mag+z1;
		            float zb = p.noise(x + xp +gap, y +yp)* -mag+z1;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)* -mag+z1;
		            float zd = p.noise(x +xp,y+yp+gap)*-mag+z1;
		            
		           if(x == x1 ){
		              za = z1;
		              zd = z1;
		            }
		            if( y == y1){
		              zb = z1;
		              za = z1;
		            }
		            
		            if(x + gap == x2){
		           
		             zb = z1;
		             zc = z1; 
		              
		            }
		            if( y + gap == y2){
		              zc = z1;
		              zd = z1;
		              
		            }
		            
		            p.vertex(x,y,za);
		            p.vertex(x+gap,y,zb);
		            p.vertex(x+gap,y+gap,zc);
		            p.vertex(x,y+gap,zd);
		  
	 
		        }
		  }
		  p.endShape();
		  
		  p.beginShape(p.QUADS);
		      for(float y= z1; y < z2; y+=gap){
		        for(float x= y1; x < y2; x+=gap){
		   
		            float za = p.noise(x + xp ,y + yp)*mag + x2;
		            float zb = p.noise(x + xp +gap, y +yp)*mag + x2;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)*mag + x2;
		            float zd = p.noise(x +xp,y+yp+gap)*mag + x2;
		            
		           if(x == y1 ){
		              za = x2;
		              zd = x2;
		            }
		            
		            if( y == z1){
		              zb = x2;
		              za = x2;
		            }
		            
		            if(x + gap == y2){
		              
		             zb = x2;
		             zc = x2; 
		              
		            }
		            if( y + gap == z2){
		              zc = x2;
		              zd = x2;
		              
		            }
		            
		            p.vertex(za,x,y);
		            p.vertex(zb,x+gap,y);
		            p.vertex(zc,x+gap,y+gap);
		            p.vertex(zd,x,y+gap);
		     
		        }
		  }
		  
		  p.endShape();
		  
		  
		    p.beginShape(p.QUADS);
		      for(float y= z1; y < z2; y+=gap){
		        for(float x= y1; x < y2; x+=gap){
		   
		            
		            float za = p.noise(x + xp ,y + yp)* -mag + x1;
		            float zb = p.noise(x + xp +gap, y +yp)* -mag + x1;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)* -mag + x1;
		            float zd = p.noise(x +xp,y+yp+gap)* -mag + x1;
		            
		           if(x == y1 ){
		              za = x1;
		              zd = x1;
		            }
		            
		            if( y == z1){
		              zb = x1;
		              za = x1;
		            }
		            
		            if(x + gap == y2){
		              
		             zb = x1;
		             zc = x1; 
		              
		            }
		            if( y + gap == z2){
		              zc = x1;
		              zd = x1;
		              
		            }
		            
		            p.vertex(za,x,y);
		            p.vertex(zb,x+gap,y);
		            p.vertex(zc,x+gap,y+gap);
		            p.vertex(zd,x,y+gap);
		    //  
		        }
		  }
		  
		  p.endShape();
		  

		  p.normal(0, 0, -1);
		  
		  p.beginShape(p.QUADS);
		      for(float y= y1; y < y2; y+=gap){
		        for(float x= x1; x < x2; x+=gap){
		           //  fill(random(255));
		          //println("hanbyul sucks");
		        	
		            float za = p.noise(x + xp ,y + yp)*mag + z2;
		            float zb = p.noise(x + xp +gap, y +yp)*mag + z2;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)*mag + z2;
		            float zd = p.noise(x +xp,y+yp+gap)*mag + z2;
		            
		           if(x == y1 ){
		              za = x2;
		              zd = x2;
		            }
		            
		            if( y == z1){
		              zb = x2;
		              za = x2;
		            }
		            
		            if(x + gap == y2){
		              
		             zb = x2;
		             zc = x2; 
		              
		            }
		            
		            if( y + gap == z2){
		              zc = x2;
		              zd = x2;
		            }
		            
		            p.vertex(x,y,za);
		            p.vertex(x+gap,y,zb);
		            p.vertex(x+gap,y+gap,zc);
		            p.vertex(x,y+gap,zd);
		    //  
		        }
		    }
		  
		      p.endShape();
		  
		  // top
		  
		  // bottom

		      p.normal(0, -1, 0);
		  
		      p.beginShape(p.QUADS);
		      for(float y= x1; y < x2; y+=gap){
		        for(float x= z1; x < z2; x+=gap){
		            
		            float za = p.noise(x + xp ,y + yp)*mag + y2;
		            float zb = p.noise(x + xp +gap, y +yp)*mag + y2;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)*mag + y2;
		            float zd = p.noise(x +xp,y+yp+gap)*mag + y2;
		            
		           if(x == z1 ){
		              za = y2;
		              zd = y2;
		            }
		            
		            if( y == x1){
		              zb = y2;
		              za = y2;
		            }
		            
		            if(x + gap == z2){
		              
		             zb = y2;
		             zc = y2; 
		              
		            }
		            
		            if( y + gap == x2){
		              zc = y2;
		              zd = y2;
		            }
		            
		            p.vertex(x,za,y);
		            p.vertex(x+gap,zb,y);
		            p.vertex(x+gap,zc,y+gap);
		            p.vertex(x,zd,y+gap);
		    //  
		        }
		    }
		  
		      p.endShape();
		  
		    p.normal(0, 1, 0);
		   
		    p.beginShape(p.QUADS);
		   
		      for(float y= x1; y < x2; y+=gap){
		        for(float x= z1; x < z2; x+=gap){
		
		            float za = p.noise(x + xp ,y + yp)*mag + y2;
		            float zb = p.noise(x + xp +gap, y +yp)*mag + y2;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)*mag + y2;
		            float zd = p.noise(x +xp,y+yp+gap)*mag + y2;
		            
		           if(x == z1 ){
		              za = y2;
		              zd = y2;
		            }
		            
		            if( y == x1){
		              zb = y2;
		              za = y2;
		            }
		            
		            if(x + gap == z2){
		              
		             zb = y2;
		             zc = y2; 
		              
		            }
		            
		            if( y + gap == x2){
		              zc = y2;
		              zd = y2;
		            }
		            
		            p.vertex(x,za,y);
		            p.vertex(x+gap,zb,y);
		            p.vertex(x+gap,zc,y+gap);
		            p.vertex(x,zd,y+gap); 
		        }
		    }
		  
		    p.endShape();
		 
		   // top
		  p.normal(0, 1, 0);
		  

		    p.beginShape(p.QUADS);
		   
		      for(float y= x1; y < x2; y+=gap){
		        for(float x= z1; x < z2; x+=gap){
		
		            
		            float za = p.noise(x + xp ,y + yp)*-mag + y1;
		            float zb = p.noise(x + xp +gap, y +yp)*-mag + y1;
		            float zc = p.noise(x+ xp + gap,y+ yp +gap)*-mag + y1;
		            float zd = p.noise(x +xp,y+yp+gap)*-mag + y1;
		            
		           if(x == z1 ){
		              za = y1;
		              zd = y1;
		            }
		            
		            if( y == x1){
		              zb = y1;
		              za = y1;
		            }
		            
		            if(x + gap == z2){
		              
		             zb = y1;
		             zc = y1; 
		              
		            }
		            
		            if( y + gap == x2){
		              zc = y1;
		              zd = y1;
		            }
		            
		            p.vertex(x,za,y);
		            p.vertex(x+gap,zb,y);
		            p.vertex(x+gap,zc,y+gap);
		            p.vertex(x,zd,y+gap);
		    
		        }
		    }
		  
	      p.endShape();
		  p.popMatrix();
	  }
	  
	  public void setRad(float r){
		  this.rad = r ;
	  }
	   
	  
	  public void dance(float f){
		  this.mag = f;
		  
	  }

}
