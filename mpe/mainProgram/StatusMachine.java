package mpe.examples;


//this class is only for controlling elements

public class StatusMachine {

	public boolean drop;
	public boolean changeSize;
	public boolean explode;
	public boolean stop;
	public boolean goBack;
	public boolean mirrorball;
	public boolean goEndPoint;
	public boolean fastMirrorball;
	public boolean end;
	
	public StatusMachine(){
		
		drop 		= false;
		changeSize 	= false;
		explode 	= false;
		stop 		= false;
		goBack 		= false;
		mirrorball	 = false;
		goEndPoint	 = false;
		fastMirrorball = false;
		end = false;

	}
	

}
