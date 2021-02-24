package com.mytool.impl;

import java.util.Random;

/**
 * class used to pass lift requests from users
 * 
 *
 */
public class LiftExecutor {

	// CONSTANTS : can be set as required 
	private static final boolean IS_ZEROTH_FLOOR_PRESENT = true;
	private static final int TOTAL_LIFT_REQUESTS = 15; 
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
         System.out.println("test");
      LiftsCentralProcessing liftsCentralProcessing = new LiftsCentralProcessing();
      int topMostFloorNo = 0;
      int startFloorNo = Lift.START_FLOOR_NO;
      int totalFloors = Lift.TOTAL_FLOORS;
      
      if (IS_ZEROTH_FLOOR_PRESENT ) {
    	  topMostFloorNo = startFloorNo + totalFloors - 1;
      }
      else {
    	  topMostFloorNo = startFloorNo + totalFloors ;
      }
      
      try{
    	  liftsCentralProcessing.instantiateAllLifts();	 
    	  liftsCentralProcessing.processLiftRequest();
      int randomFloorNo = 0;
      int randomDirection = 0;
      // For Lifts with 0th Floor 
      //for (int floorNo = startFloorNo; floorNo <= topMostFloorNo; floorNo++)
      for (int liftReqNo = 0; liftReqNo <TOTAL_LIFT_REQUESTS; liftReqNo++)
      {
    	  randomFloorNo = (int)(Math.random() * topMostFloorNo);
    	 // randomFloorNo = new Random().nextInt((topMostFloorNo -startFloorNo )+1)+startFloorNo ;
    	  
    	  for (int directionNo = 1 ; directionNo <= 2 ; directionNo ++)
    	  {
    		  randomDirection =  (int)(Math.random() * 2);
    		  // generating to get 1 direction at random for a request     		  
    	  }
    	  System.out.println("random current Floor No from which to press UP or DOWN : " + randomFloorNo);
    	  System.out.println("random Direction = " + randomDirection ); 
    	  if ( randomDirection == 1)
    	  {	  
    		System.out.println("call liftsCentralProcessing.pressUpOrDown(randomFloorNo, LiftDirection.UPWARD);");  
    	    liftsCentralProcessing.pressUpOrDown(randomFloorNo, LiftDirection.UPWARD);
    	  }
    	  else{
    		  System.out.println("call liftsCentralProcessing.pressUpOrDown(randomFloorNo, LiftDirection.DOWNWARD);");
    		  liftsCentralProcessing.pressUpOrDown(randomFloorNo, LiftDirection.DOWNWARD);
    	  } 
    		
      }
      
      // For lifts without 0th floor ------- same as above exclude No.0
      }catch(Exception e){
    	  throw new Exception("Exception while execution : " + e.getMessage());
      }   
	}
	


}
