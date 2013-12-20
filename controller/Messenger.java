/**

 * Simple Template for the Big Screens Class, Fall 2013

 * <https://github.com/ITPNYU/BigScreens>

 * 
//just checking
 * Note this project uses Processing 2.0.1

 */



package controller;

import mpe.client.*;
import java.util.ArrayList;
import processing.core.*;
import oscP5.*;


///////////DID YOU CHECK CONNECTION BTW ABLETON LIVE & MAX & PROCESSING ? 

@SuppressWarnings("serial")

public class Messenger extends PApplet {

    //--------------------------------------
	//3th // 4th // 2&5 
	

	TCPClient client;

    PFont font;
    OscP5 oscp5;

    ///my vars

    int z;
    int[] values;
    int hbinput0;
    int hbinput1;
    
    String msg;
    
    boolean bgChange;
    
/////////////////////////////////////////////////////////////////////////////////////

///////////////////////////WHAT MODE ARE YOU RUNNING IN?/////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////

    enum Mode {

    LOCAL, ITP, IAC
 
    }



// Where are you?

// We need to know in order to connect to server

Mode mode = Mode.ITP;


    //--------------------------------------

    static public void main(String args[]) {

        PApplet.main(new String[] { "controller.Messenger" });

    }



    //--------------------------------------

    public void setup() {

        size(800,600);

        

		smooth();
		
		frameRate(30);
		
		font = createFont("Arial", 18);
		values = new int[5];
		
		for(int i=0; i<values.length; i++){
			values[i] = 1;
		}
		hbinput0 = 1;
		hbinput1 = 1;
		
		 
		msg = "";
		// set up the client
		
		String path = "mpefiles/"
		
		+ (mode == Mode.LOCAL ? "local" : "6screens" )
		
		+ "/asynch" + (mode == Mode.ITP ? "ITP" : "") + ".xml";


        // make a new Client using an XML file

        client = new TCPClient(this, path); 
        
        // IMPORTANT, YOU MUST START THE CLIENT!

        client.start();
        oscp5 = new OscP5(this,12000);
 
        
    }

    

    // Change colors values based on mouse position

    public void draw() {


    	background(255,100,100);
	
    	textFont(font);
    	//String msg = r + "," + g + "," + b;    
    	String msg = values[0]+","+values[1] +","+values[2]+","+values[3]+","+values[4]+","+hbinput0+","+hbinput1;
    	text("Broadcasting: " + msg,width/2,height/2);

    }

    

    

    public void mouseDragged(){



    }

    

    public void mouseReleased(){

    

    }

    
    

    //--------------------------------------

    // asynchreceive must be set to true in

    // asynch.xml to receive data here

//    public void dataEvent(TCPClient c) {
//
//		    println("Raw message: " + c.getRawMessage());
//		
//		    if (c.messageAvailable()) {
//		
//		    String[] msgs = c.getDataMessage();
//		
//		    for (int i = 0; i < msgs.length; i++) {
//		
//		    	println("Parsed message: " + msgs[i]);
//		
//		    }
//		
//		    }
//
//
//    }
//
//    


    
    
void oscEvent(OscMessage theOscMessage) {

	println("hello i am osc");
	//1st: mystery 
	//2nd : knob number
	//3rd : pitchband 
	//4th : midi note 
	//5th ; velocity 
	
	//6th : hb's input 0
	//7th : hb's input 1 - for testing
	
  for(int i=0; i<values.length; i++){
	  values[i] = theOscMessage.get(i).intValue();
//	  println("I got it");

  }
//
	String msg = values[0]+","+values[1] +","+values[2]+","+values[3]+","+values[4];
//	
//	
//	fill(0);
//	
//	textAlign(CENTER);
//	
	if(frameCount > 1)	client.broadcast(msg);


}



public void keyPressed() {

	
	values[2] = 64;
	
	
	  if(key =='s') {
		  //scene chagne
		  values[3] = 59;
	  }
	  
	  
	  if(key =='z'){
		  //mesh roMesh tation
		 hbinput0 = 1;
	  }
	  
	  if(key =='n'){
		  //cam xpos
		 hbinput0 = 2;
	  }
	  
	  if(key =='7'){
		 //mesh size
		  hbinput0 = 3;
		  
	  }
	  
	  if(key==' '){
		  //pull start
		  hbinput0 = 4;
	  }
	  
	  if(key=='g'){
		  
		  //pull the mesh
		  hbinput0 = 5;
	  }
	  if(key=='y'){
		  //reset everything
		  hbinput0 = 8;
	  }
	  
	  
	  //cubinho
	  if(key=='1'){
		   //drop
		  hbinput1 = 1;
	  }		 
	  
	  if(key=='2'){
		  //size
		  hbinput1 = 2;
		  
	  }
	  
	  if(key=='3'){
		  //explode
		  hbinput1 = 3;
	  }
	  
//	  if(key=='4'){
//		  //stop
//		  hbinput1 = 4;
//	  }
	  
	  if(key=='5'){
		 //goback
		  hbinput1 = 5;
		  
	  }
	  
	  if(key =='o'){
		  //popstar cue
		  hbinput1 = 6;
	  }
	  
	  if(key=='m'){ 
		  //make mirrorball
		  hbinput1 = 7;
	  }
	  
	  
	  if(key=='q'){
		  //campos --
		  hbinput1 = 8;
	
	  }
	
	  if(key=='h'){
		  //cam pos ++
		  hbinput1 = 9;
	  }
	  
	  if(key=='4'){
		  	values[3] = 44;

	  }
		msg = values[0]+","+values[1] +","+values[2]+","+values[3]+","+values[4]+","+ hbinput0+","+hbinput1;
		if(frameCount > 1)	client.broadcast(msg);
	}

    

}