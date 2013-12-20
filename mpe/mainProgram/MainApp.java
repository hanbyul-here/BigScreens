/**
 * Simple Template for the Big Screens Class, Fall 2013
 * <https://github.com/ITPNYU/BigScreens>
 * 
 * Note this project uses Processing 2.0.1
 */

package mpe.examples;
import mpe.client.*;
import processing.core.*;
import java.util.*;

@SuppressWarnings("serial")

////////when you are using 3d : it looks very different on screen !! 
///

public class MainApp extends PApplet {
	
	///To me  : DONT USE WIDTH & HEIGHT. USE thisW  && this H.
	// Your scene is being cut, don't trust camera. 
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	/////////////////////////WHAT MODE ARE YOU RUNNING IN?/////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	public static enum Mode {
		LOCAL, MPE, CUSTOM
	}
	
	public static Mode mode = Mode.MPE;
	
	// Client ID (0: Left, 1: Middle, 2: Right)
	// Should be adjusted only for "local" testing
	//-----------r--------------- ------------
	int ID = 2;
		
	
	// Only fiddle with this if you choose Mode.CUSTOM
	//--------------------------------------

	// Set it to 1 for actual sie, 0.5 for half size, etc.
	// This is useful for testing MPE locally and scaling it down to fit to your screen
	public static float scale = 0.15f;

	// if this is true, it will use the MPE library, otherwise just run stand-alone
	public static boolean MPE = true;
	public static boolean local = true;	
	
	TCPClient client;

	// "Real" dimensions of screenr
	final int tWidth = 11520;
	final int tHeight = 1080;
	
	// These we'll use for master width and height instead of Processing's built-in variables
	int mWidth;
	int mHeight;
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////YOUR VARIABLES/////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////


	//osc values from Max
	int[] values;
	
	//vars for whole scene

	StatusMachine sm;
	int sceneMachine;
	float s2Cam;		//have to edit this
	PVector eyePos; 
	PVector camPos;
	PVector originPos;
	boolean setOrigin;
	boolean camToOrigin;
	
	ArrayList<PVector> randomParticles;		//background stars
	ArrayList triangles;
	
	float bgcol;
	PVector normalLight0;
	PVector normalLight1;
	
	PVector mirrorBallLight0;
	PVector mirrorBallLight1;
	
	float val;
	
	//wave part
	float zScale;
	float currScale;
	float zDamper;
	float zAdded;
	
	float meshY;		//the perfect value for meshyis 1800..dont move other value (perspective prob)
	float rotY;				//rotating whole mesh - set the threshold
	boolean rotS; 			//trigger for rotating
	
	boolean meshS;			//meshScale
	Mesh mesh;
	int meshSize;
	
	boolean pull;
	float arz; 
	float arzDamper;
	float arzAdded;
	
	float currScaleDamper;
	float currScaleAdded;
	
	//cube part
	
	ParticleSystem[] ps; 	//cube particles
	PVector[] center;		//particles' centers
	

	boolean camToPopstar;				///cameara movement to popstar cube
	PopstarCube popstarCube;
	
	float explodeSpeed;
	float goBackSpeed;
	//mirror ball Scene
	
	Mirrorball[] mballs;
	float fastM;
	
	
	//--------------------------------------
	static public void main(String args[]) {
		
		//Set mode settings
		if(mode == Mode.LOCAL) {
			MPE = false;
			local = true;
			scale = .15f;			
		}
		
		else if(mode == Mode.MPE) {
			
			MPE = true;
			local = false;
			scale = 1.0f;
		}
		
		// Windowed
		if (local) {
			PApplet.main(new String[] {"mpe.examples.MainApp" });
		// FullScreen Exclusive Mode
		} else {
			PApplet.main(new String[] {"--present", "--5or=#000000", "mpe.examples.MainApp" });
		}
	}

	//--------------------------------------
	public void setup() {

		// If we are using the library set everything up
		if (MPE) {
			// make a new Client using an XML file
			String path = "mpefiles/";
			if (local) {
				path += "local/mpe" + ID + ".xml";
			} else {
				ID = IDGetter.getID();
				path += "6screens/mpe" + ID + ".xml";
			}
			client = new TCPClient(this, path);
			// Not rendering with OPENGL for local testing
			if (local) {
				
				size((int)(client.getLWidth()*scale), (int)(client.getLHeight()*scale),OPENGL);
				client.setLocalDimensions((int)(ID*client.getLWidth()*scale), 0, (int)(client.getLWidth()*scale), (int)(client.getLHeight()*scale));
			} else {
				size(client.getLWidth(), client.getLHeight(),OPENGL);
			}
			// the size is determined by the client's local width and height
			mWidth = client.getMWidth();
			mHeight = client.getMHeight();
			println("right after setting mwidth" + mWidth);
			
		} else {
			
			// Otherwise with no library, force size
			size(parseInt(11520*scale),parseInt(1080*scale),OPENGL);
			mWidth = 11520; //client.getMWidth();
			mHeight = 1080;// client.getMHeight();
			println("right after setting mwidth" + mWidth);
	
		}
		
		smooth();		
		resetEvent(client);

		if (MPE) {
			// IMPORTANT, YOU MUST START THE CLIENT!
			client.start();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////YOUR SETUP BELOW///////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	//--------------------------------------
	// Start over!
	// Happens automatically when new client connects to mpe server
	public void resetEvent(TCPClient c) {
		// random and noise seed must be identical for all clients
		randomSeed(1);
		noiseSeed(1);
		
		noCursor();
		
		// re-initialize all of your variables
		frameRate(30);
		background(255,0,255);
		
		//oscp5 = new OscP5(this,12000);// midi signal is consist of 5 digits.
		values = new int[7];
		for(int i=0;i<values.length;i++){
			values[i] = 0;
		}
		
		//vars for whole scene
		
		sm = new StatusMachine();
		sceneMachine = 0;
		s2Cam = 4500.f;		//have to edit this
		
		eyePos = new PVector(mWidth/2.f, mHeight/2.f, (float) ((mHeight/2.0) / tan((float) (PI*30.0 / 180.0))+ 5900.f)); //idea z pos : 4900
		camPos = new PVector(mWidth/2.f, mHeight/2.f , 5900.f);
		
		originPos = new PVector(eyePos.x,eyePos.y,eyePos.z);
		setOrigin = false;
		
		normalLight0 = new PVector(254, 266,230);
		normalLight1 =  new PVector(246, 230, 280);
	
		mirrorBallLight0 = new PVector(54, 166, 230);
		mirrorBallLight1 = new PVector(146, 230,280);
		
		val = 0f;

	    randomParticles = new ArrayList<PVector>();
	    
	    for(int i=0; i< 500; i++){
			  	 randomParticles.add(new PVector(random(-mWidth*2.5f,mWidth*2.5f),random(-mHeight*1.5f,mHeight*1.5f),random(-100,400)));	  
		  }
	    
		//wave part
		zScale = 3500.f;
		currScale = 0.3f;
		zDamper = 0.6f;
		zAdded = 3500.f;
		
		meshY = 1800.f;		//the perfect value for meshyis 1800..dont move other value (perspective prob)
		rotY = 1.2f;			//rotating whole mesh
		meshSize = -3; 		//mesh cube size 
		
		meshS = false;			//meshScale
		mesh = new Mesh(this);
		rotS = false;
		pull = false;
		arz = 0.f;
		arzDamper = 0.000000001f;
		arzAdded = 0.f;
		
		currScaleDamper = 0.8f;
		currScaleAdded = 0.1f;
		
		
		//cube part

		explodeSpeed = 0.f;
		goBackSpeed = 0.f;
		
		
		camToPopstar = false;				///cameara movement to popstar cube
		camToOrigin = false;
		
		
		  ps = new ParticleSystem[5];
		  center = new PVector[5];
		  
		  
		  for (int i=0; i<ps.length; i++) {  
		    center[i] = new PVector(((mWidth*1.5f)/(float)ps.length)*i-1000f, mHeight/4.f, random(1400,1800));
		    ps[i] = new ParticleSystem(center[i], this);
		    
		  }	
		  
		  for(int i=0; i<ps.length; i++){
			  ps[i].setEndPoint(center[2]);
		  }
		  
		  popstarCube = new PopstarCube(new PVector(center[2].x,center[2].y,center[2].z-3000), 400f,200.f,this);
		  mballs = new Mirrorball[5];
		  fastM = 0f;
		  
		  //I don't have any error handling now, have to figure out 
		  //I bet this is the stupidest code in the world fuck I don't care
		  mballs[0] = new Mirrorball(this,new PVector(center[0].x-1000,center[0].y,center[0].z), 600.f);
		  mballs[1] = new Mirrorball(this,center[1],5900.f);
		  mballs[2] = new Mirrorball(this,center[2],500.f);
		  mballs[3] = new Mirrorball(this,center[3],5450.f);
		  mballs[4] = new Mirrorball(this,new PVector(center[0].x+1000,center[0].y,center[0].z),550.f);

		 // popstarCube = new PopstarCube(new PVector(center[1].x,center[1].y,center[1].z-1000),100,this);
		  
		  for(int i=0; i<ps.length; i++){
			  
			  for(int j=0; j<ps[i].particles.length; j++){
				  ps[i].particles[j].setMirrorball(mballs[i].getPosition(j), mballs[i].getAngles(j).x,mballs[i].getAngles(j).y ) ;
			  }
			  
			  ps[i].setMirrorballSize(mballs[i].rad);
		  }
		  
		float fov = PI/2.5f;
		float cameraZ = (mHeight/2.f) / tan(fov/2.f);
		perspective(fov, (float)(mWidth)/(float)(mHeight), cameraZ/10.f, cameraZ*15.f);
		//ortho(0,mWidth, 0, mHeight);
		
		float xOffset = 0.f;
		float yOffset = 0.f;
	//	float mWidth = -1.f;
	//	float mHeight = -1.f;
		float lWidth = 1640.f;
		float lHeight = 480.f;
        float mod = 5f/10f;
        float left   = (xOffset - mWidth/2)*mod;
        float right  = (xOffset + lWidth - mWidth/2)*mod;
        float top    = (yOffset - mHeight/2)*mod;
        float bottom = (yOffset + lHeight-mHeight/2)*mod;
        float near   = cameraZ*mod;
        float far    = 58000;
		
		//frustum(left,right,top,bottom,near,far);
        frustum(-(mWidth/2)*mod, (mWidth/2)*mod,
                -(mHeight/2)*mod, (mHeight)*mod,
                camPos.z*mod, 38000);
        
		println("setup done");

	}
		
	//--------------------------------------
	// Keep the motor running... draw() needs to be added in auto mode, even if
	// it is empty to keep things rolling.
	public void draw() {
		
//		if(client.isRendering()){
//			client.placeScreen3D();
//			renderContents();
//			
//			client.done();
//		}

		// If we are on the 6 screens we want to preset the frame's location
		if (MPE && local) {
				frame.setLocation(ID*width,0);
		}

		// If we're not using the library frameEvent() will not be called automatically
		if (!MPE) {
			frameEvent(null);
		}
	//	println(frameRate);
	//	println("hello");
}
		
//	}
	
	//--------------------------------------
	// Separate event for receiving data
	// Or a controller app
//	
//	public void dataEvent(TCPClient c) {
//		String[] msg = c.getDataMessage();
//		
//		String colors = msg[0];
//		println("data is receving");
//		
//		float[] vals = parseFloat(colors.split(","));
//		for(int i=0; i<vals.length;i++){
//			values[i] = floor(vals[i]);
//		}
//	
//	}

	//--------------------------------------
	// Triggered by the client whenever a new frame should be rendered.
	// All synchronized drawing should be done here when in auto mode.

	
	public void frameEvent(TCPClient c) {
		
		//println(frameRate);
		// clear the screen
		if (!MPE || local) {
			scale(scale);
		}

		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////YOUR CODE//////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		// Receiving a message for // color
		// It is also possible to receive data inside frameEvent
		
		if (MPE && c.messageAvailable()) {
			String[] msg = c.getDataMessage();
			String colors = msg[0];
		//	println("this is value" + colors + " length : "+ msg.length);
			float[] vals = parseFloat(colors.split(","));
		//	println(vals);
			for(int i=0; i<vals.length;i++){
				values[i] = floor(vals[i]);
			}
		//	println("udp signal yo");
		
			getMessage();
		}
		
		renderContents();
	}
	
	
	
	///////////rendering//////
	
	void renderContents(){
		

		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////YOUR CODE//////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		// Receiving a message for // color
		// It is also possible to receive data inside frameEvent

		if(!sm.end ) background((abs(sin(bgcol)))*255.f);
		else background(255);
			
		  beginCamera();
		  camera(eyePos.x, eyePos.y, eyePos.z, camPos.x, camPos.y, camPos.z, 0.f, 1.f, 0.f);
		  endCamera();
			  

		//  ambientLight(200,200,200);	 

		  //just background part (twinkle twinkle starts)
		  stroke(150);
	//	  strokeWeight(2);
		  
		  //this effect is making weirdly long line. 

		  noStroke();
		 
		  for(PVector pv:randomParticles){
			  fill(abs(pv.z)/1.5f);
			  rect(pv.x,pv.y,10,10);
		  }
		  	

		
			if(sceneMachine == 0){
				  pointLight(255,255,255,0,-mHeight/4,s2Cam);
				  pointLight(255,255,255,mWidth/2,-mHeight/4,s2Cam);
				  pointLight(255,255,255,mWidth,-mHeight/4,s2Cam);

			  pushMatrix();
			  translate(0,meshY,0);
			  rotateX(rotY);
			  stroke(abs(sin(bgcol))*255.f);
			  if(!pull) mesh.update();
			  if(pull) {
				  arz = arz*arzDamper + (1-arzDamper) * arzAdded;
				  mesh.pull(arz);
				
			  }
			  mesh.display();
			  zScale = zScale*zDamper + (1-zDamper) * zAdded;
			  mesh.scaleZ(zScale);
			  currScale = currScale*currScaleDamper + (1-currScaleDamper)*currScaleAdded;
			  mesh.scaleWave(currScale);
			  if(meshS){ mesh.scale(meshSize);	  eyePos.z +=random(-20,20);}
			  if(rotS && rotY >0){
				  rotY -=0.015f;
			  }
			  
			  popMatrix();

			}
			
		  //debugging ellipse
		//  ellipse(tWidth/2,tHeight/2,1000,1000);
		  
		  if(sceneMachine == 1){
			  
			  if(!sm.end){
				  if(sm.mirrorball){
					  PVector tempLight0 = PVector.lerp(normalLight0,mirrorBallLight0,val);
					  PVector tempLight1 = PVector.lerp(normalLight1,mirrorBallLight1,val);
					  directionalLight(tempLight0.x, tempLight0.y, tempLight0.z, 1, 10, -100);
					  directionalLight(tempLight1.x, tempLight1.y, tempLight1.z,10, 10, -100);
					  if(val < 0.8f) val +=0.01f;
				  }
				  else{
					  directionalLight(normalLight0.x, normalLight0.y, normalLight0.z ,1, 10, -100);
					  directionalLight(normalLight1.x, normalLight1.y, normalLight1.z, 10, 10, -100);
				  
				  }
			  }
			  
			  
			  //pointLight(255,255,255,center[2].x,center[2].y,center[2].z+3000.f);

			 if(eyePos.z>6000){
				 eyePos.z -=10;
				 camPos.z -=10;
				// println(eyePos.z);
			 }
			 
			  
			 
			 //throwing error ! :( 
			  
			 noStroke();
			 if(!sm.end )  fill(255- (abs(sin(bgcol)))*255.f);
			 else fill(255);
			  for (int i=0; i<ps.length;i++) {
				  
				 if(sm.drop) ps[i].showup();
				 if(sm.changeSize) ps[i].changeSize();
				 if(sm.explode) ps[i].explode(explodeSpeed);
				 if(sm.stop) ps[i].stop();
				 if(sm.goBack) ps[i].goBack(goBackSpeed);
				 if(sm.mirrorball)	 ps[i].goMirrorball();
				 if(sm.fastMirrorball) ps[i].fastMirrorball(fastM);
				 if(sm.goEndPoint){	 ps[i].goEndPoint();
				 
				 }
					 
				 
				  ps[i].update();
				  ps[i].display();
			  }
			  
			  
	
			  if(camToPopstar){
				  
				  if(eyePos.z > popstarCube.pos.z+3550){  
				  eyePos.lerp(popstarCube.pos, 0.04f);
				  camPos.lerp(popstarCube.pos, 0.04f);
				
				  for (int i=0; i<ps.length;i++) {
						  ps[i].arrangeSize();
				  }
					  	  
				}else{
				//	popstarCube.changeSize(random(-1f,2f));
			
				} 

				  
			  }
			  
			  if(camToOrigin){

				  if(PVector.dist(eyePos,originPos)>50){
					  for (int i=0; i<ps.length;i++) {
						  ps[i].funkySize();
				  }
				  eyePos.lerp(originPos, 0.05f);
				  camPos.lerp(originPos, 0.05f);
				  }
				  
				}
			    
		  }
		
		
	}
	
	
	
	
	///////////////////////////////////////////////////////
	//////////////////////for itp & iac////////////////////
	//////////////////////////////////////////////////////
	
	void getMessage(){
		
		//1st: mystery 2nd : knob number 3rd : pitchband 4th : midinote 5th ; velocity 
		//4th value 59,60,7,64,59
		  println("sigial receving");
		  int triggerK = 0;		 
		  int gateNumber = floor(values[1]);
		  int gateValue = floor(values[4]);
		  int pitchband = floor(values[2]);
		  int hbinput0 = values[5];
		  int hbinput1 = values[6];

		  //reset. keyboard

		  //background color
		  if(pitchband < 64){
			  bgcol = map(pitchband,0,63,1.f,0.f);

		  }else if(pitchband>64){
			  
			  zAdded =  map(pitchband,65,127,3500.f,4000.f);
			  currScale = map(pitchband,65,127,0.3f,0.55f);
			  //status[4] = true;
		 
		  }else{
			  
			  if(hbinput0 == 8) {
				  resetEvent(client);
				  println("reset");
			  }
			  
			  
			  	triggerK = floor(values[3]);
			  	//println("this is tiggerK "+triggerK);
			 		  
			  	//meshRotation
			  
			  	if(triggerK ==37){
					 rotS = true;
			  	} 
			  
			  	if(triggerK ==38){
				  pull = true;
				  arzAdded+=3200000f;
				  println(arz);
		
			  	}
			  	
				  if(triggerK == 39){
					  
					  if(!setOrigin){
						  
						  originPos = new PVector(eyePos.x,eyePos.y,eyePos.z);
						  setOrigin = true;
						  
					  }
					  camToPopstar = true;
					  camToOrigin = false;
					  sm.changeSize = false;
				  }
				  
				  if(triggerK == 41){
					  camToPopstar = false;
					  camToOrigin = true;
					  println("hello let's go back");
				  }
				  
				  
				  if(triggerK == 42){
						  
						for(int i=0; i<ps.length; i++){
						  	ps[i].willThisBeMirrorball(true);
					}
					  sm.mirrorball = true;
					  sm.explode = false;
					  sm.stop = false;
					  sm.goBack = false;	
				  }
				  
				  if(triggerK ==44){
					  sm.end = true;
				  }

				  
				  //stop cube size chaniging
				  if(triggerK ==47){
					  meshS = false;
				  }
				  
				  //decreasing the size of the cube
				  if(triggerK == 48){
					  meshS = true; 
					  if(meshSize>0) meshSize *= -1;
				  }
				  //increasing the size of the cube
				  if(triggerK ==49){
						
					  meshS = true;
					  if(meshSize < 0)  meshSize *= -1;
					  
				  }
				  
				  
				  //have to adjust number for finale
				  if(triggerK == 50){
							  sm.mirrorball = false;
							  sm.explode = false;
							  sm.stop = false;
							  sm.goBack = false;	
							  sm.goEndPoint = true;
							
				  }
				  
				  if(triggerK ==52){
					  
				  }
				  
				  /////have to get the number
				  if(triggerK == 54){
					  
					  sm.explode = 	false;
					  sm.stop 	=  	false;
					  sm.goBack =	true;
					  sm.mirrorball = false;
					  sm.goEndPoint = false;
					  goBackSpeed = 5.5f;
					  
				  }

				  //changing size off
				  if(triggerK==55){
					  sm.changeSize = false;
				  }
				  
				  if(triggerK==56){
					  println("stop");
				
					  sm.explode = 	false;
					  sm.stop 	=  	true;
					  sm.goBack =	false;
					  sm.mirrorball = false;
					  sm.goEndPoint = false;
				  }	  
				  
					
				  //exploding
				  if(triggerK==57){
					  println("explode");
			
					  sm.explode = true;
					  sm.stop 	= false;
					  sm.goBack = false;
					  sm.mirrorball = false;
					  sm.goEndPoint = false;
					  ///have to adjust this
					  explodeSpeed = 4.5f;
				  }
				  

				  //changing size on				  
				  if(triggerK==58){
				
					  sm.changeSize = true;
				  }	   

				  //scene changing : from wave to cubinho
				  if(triggerK == 59){ sceneMachine = 1;}
				  
				  //box dropping
				  if(triggerK==60){
					  sm.drop = true;
				  }	
				  			  	  
		  }
			

	}
	
   
	
	///for local connection
	/*
		void oscEvent(OscMessage theOscMessage) {
			  println("receving");
			  int triggerK = 0;		  
			  int pitchband = floor(values[2]);
			  int hbinput0 = values[5];
			  int hbinput1 = values[6];
			  
			  if(pitchband <64){
				
				//  currScale = map(pitchband,0,63,20f,0.1f);
				  zAdded =  map(pitchband,0,63,4000.f,2500.f);
				  
				  currScale = map(pitchband,0,63,0.6f,0.3f);
				// currScale +=0.01f;
				//  zScale +=10;
				  
				  
			  }else if(pitchband>64){
				  
				  bgcol = map(pitchband,65,127,0.f,1.f);
				  //status[4] = true;
				  
			  }else{
					//  status[4] = false;
						  
					  triggerK = floor(values[3]);
					  
					  println("this is tiggerK "+triggerK);
					  
					  //mesh Size Change
					  
					  if(triggerK ==36){
						  if(meshS)  meshSize *= -1;
						  meshS = !meshS;
						  
					  }
					  //meshRotation
					  
					  if(triggerK ==37){
							 rotS = true;
					  }
					  
					  if(triggerK ==38){
						  pull = true;
						  arzAdded+=50000f;
						  println(arz);
				
					  }
					  
					  
					  //scene changing : from wave to cubinho
					  if(triggerK == 59){ sceneMachine = 1;}
					  
					  
					  //box dropping
					  if(triggerK==60){
			
						  sm.drop = true;
					  }		 
					  
					  //changing size
					  
					  if(triggerK==58){
					
						  sm.changeSize = !sm.changeSize;
					  }
					  
					
					  //exploding
					  if(triggerK==57){
						  println("explode");
				
						  sm.explode = true;
						  sm.stop 	= false;
						  sm.goBack = false;
						  
						  ///have to adjust this
						  explodeSpeed = 3.f;
					  }
					  
					  //stopping
					  
					  if(triggerK==56){
						  println("stop");
					
						  sm.explode = 	false;
						  sm.stop 	=  	true;
						  sm.goBack =	false;
					  }	  
					  /////have to get the number
					  if(triggerK == 54){
						  
						  sm.explode = 	false;
						  sm.stop 	=  	false;
						  sm.goBack =	true;
						  goBackSpeed = 5.f;
						  
					  }

					  
					  if(triggerK == 39){
						  camToPopstar = !camToPopstar;

						  float z = 0.f;
						  popstarCube.setRad(200.f);
//						  for (int i=0; i<ps.length;i++) {
//								 for(int j=0; j<ps[i].particles.length; j++){
//									 
//									 if (z > ps[i].particles[j].pos.z){
//										 popstarCube = ps[i].particles[j];
//										 z = ps[i].particles[j].pos.z;
//										 
//									 } 
//								 }	
//							  }
						  
						 println("done");
					  }
					  
					  if(triggerK == 41){
						  camToPopstar = false;
						  camToOrigin = true;
					  }
					  
					  
					  if(triggerK == 42){
						  //make mirrorball
						    //rad = p.random(70,120);
						  for(int i=0; i<ps.length; i++){
							  	ps[i].willThisBeMirrorball(true);
						}
					  
					  sm.mirrorball = true;
					  sm.explode = false;
					  sm.stop = false;
					  sm.goBack = false;
					  
					  }
					  
			  }
				
		
		}

	
		*/
		//for local testing without connection
	
	public void mouseDragged(){
		
//		temp -=2.f;
//		temp%=1000.f;
//		//temp  = constrain(temp,200,1000);
//		
//		  for (int i=0; i<3; i++) {
//			    center[i].z -=10;
//			    center[i].y -=1;
//		  }
//		
		println(frameRate);
		 
		// zAdded =  map(mouseY,0,153,1500.f,0.f);
		// currScaleAdded = map(mouseY,0,153,0.8f,0.2f);
		//eyePos.z -= 15;
		//println("eyePos.z" + eyePos.z);
		//println(frameRate);
	}
	
	
	public void keyPressed() {

		  if(key =='s') {
			  sceneMachine = 1;
		  }
		  
		  if(key=='1'){
			  
			  sm.drop = true;
			  
		  }		 
		  
		  if(key=='2'){
			  
			  sm.changeSize = !sm.changeSize;
			  
		  }
		  
		  if(key=='3'){
			  sm.explode = true;
			  sm.stop = false;
			  sm.goBack = false;
			  sm.mirrorball = false;
			  sm.goEndPoint = false;
			  explodeSpeed = 3.f;
		  }
		  
		  if(key=='4'){
			  sm.explode = false;
			  sm.stop = true;
			  sm.goBack = false;
			  sm.mirrorball = false;
		  }
		  
		  if(key=='5'){
			  sm.explode = false;
			  sm.stop = false;
			  sm.goBack = true;		
			  sm.mirrorball =false;
			  goBackSpeed  = 5f;
			  
		  }
		  
		  if(key=='v'){
			   currScale+=0.01f; 
			   zScale+=50;
			   println(zScale);
			}
		  
		  if(key=='q'){
			  camPos.z += 50;
			  eyePos.z += 50;
		
		  }
		
		  if(key=='h'){
			  eyePos.z -= 50;
			  camPos.z -= 50;
		  }
		  
	
		  if(key =='o'){
			  originPos = new PVector(eyePos.x,eyePos.y,eyePos.z);
			  
			  camToPopstar = true;
			  camToOrigin = false;
			  sm.changeSize = false;
			  

			 // float z = 100000.f;
			  
//			  for (int i=0; i<ps.length;i++) {
//					 for(int j=0; j<ps[i].particles.length; j++){
//						 
//						 if (z > ps[i].particles[j].pos.z){
//							 popstarCube = ps[i].particles[j];
//							 z = ps[i].particles[j].pos.z;
//							 
//						 } 
//					 }	
//				  }
//			  
			 println("done");	
			// println(z);
		  }
		  
		  if(key =='j'){
			  camToPopstar = false;
			  camToOrigin = true;
		  }
		  
		  if(key =='z'){
			 rotS = true;
		  }
		  
		  if(key =='n'){
			 currScale = 0.01f;
			 zScale = 100;
			  // currScale -=0.001f;
		  }
		  
		  if(key =='7'){
			  if(meshS)  meshSize *= -1;
			  meshS = !meshS;
			  
		  }
		  
		  if(key==' '){
			  pull = !pull;
			  arz = 10000f;
		  }
		  
		  if(key=='g'){
			//  arz+=50000f;
			  pull = true;
			  arzAdded +=5000000f;
			  println(arz);
	
		  }
		  
		  if(key=='y'){
			  resetEvent(client);
		  }
		  
		  if(key=='m'){
			  
			    //rad = p.random(70,120);
			 	float sphereRad = 2000;
			  	ArrayList<PVector> mirrorballPos = new ArrayList<PVector>();
			  	PVector c = center[2].get();
			  	
			  	for(float ang = 0;  ang< Math.PI*2f; ang+= 0.20f){
			  		for(float theta = 0; theta < Math.PI; theta+=0.25f){
			  			
			  			PVector temp = new PVector(cos(ang)*sin(theta), sin(ang)*sin(theta),cos(theta));
			  			temp.mult(sphereRad);
			  			temp.add(c);
			  			mirrorballPos.add(temp);
			  			
			  		}
			  	}
			  	
			  	int count = 0;
			  	
				for(int i=0; i<ps.length; i++){
					  	ps[i].willThisBeMirrorball(true);
				}
				    
			  sm.mirrorball = true;
			  sm.explode = false;
			  sm.stop = false;
			  sm.goBack = false;		
			  sm.goEndPoint = false;
			  
			//  println(wholeCubes.size()+"   mirrorball : " + mirrorballPos.size());
			  

		  }
		  
			if(key=='u'){
				  sm.mirrorball = false;
				  sm.explode = false;
				  sm.stop = false;
				  sm.goBack = false;	
				  sm.goEndPoint = true;
			}
			
			if(key=='.'){
				  sm.fastMirrorball = true;
			}
				
		}
	


}
