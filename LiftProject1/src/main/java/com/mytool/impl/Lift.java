package com.mytool.impl;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Lift {
	
	Lift(){};
	
	private LiftDirection liftDirection = LiftDirection.IDLE; 
	// CONSTANTS can be changed to any number as required
	public static final int START_FLOOR_NO = 1;
	public static final int TOTAL_FLOORS = 10; 
	//public static final int START_FLOOR_NO = -2;
	//public static final int TOTAL_FLOORS = 8; 
	public int currentFloorNo = START_FLOOR_NO;
	private PriorityQueue<Integer> liftRequestsPQueue = new PriorityQueue<Integer>(TOTAL_FLOORS, new Comparator<Integer>(){
		       public int compare(Integer x, Integer y){
		    	   if ( liftDirection  == LiftDirection.DOWNWARD ){
		    		   if ( x < y ) { return 1;}
		    		   else if ( y > x ) { return -1;}
		    	   }
		    	   else if( liftDirection  == LiftDirection.UPWARD ){
		    		   if ( x < y ) { return -1;}
		    		   else if ( y > x ) { return 1;}
		    	   }
		    	   return 0;
		       }
			});
    

	// Lift to be moved either Upward or Downward one floor
	public void moveOneFloor(){
		if( (currentFloorNo <  TOTAL_FLOORS) && (liftDirection == LiftDirection.UPWARD) ) 
		{ 
			currentFloorNo  = currentFloorNo + 1 ;
		}
		else if( ( currentFloorNo > START_FLOOR_NO) && (liftDirection == LiftDirection.DOWNWARD) ) 
		{  
			currentFloorNo  = currentFloorNo - 1 ;
		}
	}
	
	public PriorityQueue<Integer> getLiftRequestsPQueue(){
		return liftRequestsPQueue;
	}

	public void setLiftDirection(LiftDirection liftDirection){
		this.liftDirection = liftDirection;
	}
	public LiftDirection getLiftDirection(){
		return liftDirection;
	}
	
	public int getCurrentFloorNo(){
		return currentFloorNo;
	}
	
	public boolean isLiftIdle(){
		return liftDirection == LiftDirection.IDLE;
	}
	
	// Lift should open and Lift users to get inside the lift 
	public void getLiftUsers(){
		System.out.println("Lift Open .... Getting Lift users ....");
	}
	
	// User enters inside lift, should press floor number to go  
	public void addLiftRequestToFloor(int currentFloorNo){
		// when there is a request to currentFloorNo but currentFloorNo request was NOT present in lift request so far
		if(!(liftRequestsPQueue.contains(currentFloorNo))) {
			// if this is the 1st request for lift and no other lift request is present so far :
			if (liftRequestsPQueue.isEmpty()){
				if(this.currentFloorNo < currentFloorNo){
					setLiftDirection(LiftDirection.UPWARD);
				}
				else if(this.currentFloorNo > currentFloorNo){
					setLiftDirection(LiftDirection.DOWNWARD);
				}
				else  // this.currentFloorNo == currentFloorNo
				{
					setLiftDirection(LiftDirection.IDLE);
				}
			}
			liftRequestsPQueue.add(currentFloorNo);
		}
	}
	
	public void removeLiftRequestToFloor(int currentFloorNo){
		if(liftRequestsPQueue.contains(currentFloorNo)){
			liftRequestsPQueue.remove();
		}
	}
	
   public String toString(){
	   return "Lift : Current Floor:" + getCurrentFloorNo() + " Lift _Direction:" + getLiftDirection() + " LIFT REQUESTS liftRequestsPQueue:" + liftRequestsPQueue.toString(); 
   }
}
