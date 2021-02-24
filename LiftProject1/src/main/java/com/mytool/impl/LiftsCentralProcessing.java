package com.mytool.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * User : should press button outside lift to get a lift (UPWARD or DOWNWARD)
 * If any closest lift already assigned -ontheway- in the same direction , that lift will serve
 * the lift request.
 * If there is no such lift- ontheway - assign the lift that is  -IDLE
 * If there is no such  - IDLE lift also then assign a lift that is currently serving - REQSERVING
 */
public class LiftsCentralProcessing {
	// CONSTANTS can be changed to any number as required
	public static final int TOTAL_LIFTS = 5;
	//public static final int TOTAL_LIFTS = 3;
		
	int totalLifts = TOTAL_LIFTS;
	// instantiate each lift

	//List<Integer> testList = null;
	//List<Lift> liftList = null ;
	//ArrayList<Lift> liftList = null;
	List<Lift> allLiftList = new ArrayList<Lift>(); 
	Lift lift = null;
	
	
	// instantiate each lift with liftDirection as IDLE
	public void instantiateAllLifts(){
        for( int liftNo = 0 ; liftNo < totalLifts; liftNo++){
		     System.out.println( "instantiateAllLifts: liftNo:" + liftNo);
               lift = new Lift();
               lift.setLiftDirection(LiftDirection.IDLE);
              allLiftList.add(lift);
              
		}
        System.out.println( "instantiateAllLifts: totalLifts: instantiated ..." );
	}	
	
	/** User should press 'UP' or 'DOWN' outside the lift to request for a lift
	 * 
	 * @param currentFloor
	 * @param liftDirection
	 * @throws Exception
	 * 
	 * step 1: Find if any Nearest lift on the way (in same direction)
	 * step 2: ELSE if there is no such Nearest lift on the way (in same direction)
	 *         THEN find if any Nearest IDLE lift.
	 * step 3: ELSE if there is no such Nearest IDLE lift also 
	 *         THEN find if any nearest lift that is serving request already 
	 */
	public void pressUpOrDown(int currentFloor, LiftDirection liftDirection ) throws Exception{
	  Lift nearestLift = null;
	  nearestLift = getNearestOnTheWayLift(currentFloor, liftDirection);
	  if (nearestLift != null){
		  nearestLift.addLiftRequestToFloor(currentFloor);
		  System.out.println("pressUpOrDown : getNearestOnTheWayLift : Found a lift : currentFloor= "+ currentFloor +" ..liftDirection="+liftDirection);
		  return;
	  }
	  else {
		  nearestLift =  getNearestIdleLift(currentFloor);
		  if (nearestLift != null){
			  nearestLift.setLiftDirection(liftDirection);
			  nearestLift.addLiftRequestToFloor(currentFloor);
			  System.out.println("pressUpOrDown : getNearestIdleLift : Found a lift : currentFloor= "+ currentFloor +" ..liftDirection="+liftDirection + "..nearestLift Direction=" + nearestLift.getLiftDirection());
			  return;
		  }
		  else{
			  nearestLift =  getNearestReqServingLift(currentFloor);
			  if (nearestLift != null){
				  nearestLift.addLiftRequestToFloor(currentFloor);
				  System.out.println("pressUpOrDown : getNearestReqServingLift: Found a lift : currentFloor= "+ currentFloor +" ..liftDirection="+liftDirection + "..nearestLift direction ="+ nearestLift.getLiftDirection());
				  return;
			  }
			  else {
				  throw new Exception ("pressUpOrDown :No Lift is ACTIVE at this moment. All Lifts are in " + LiftDirection.MAINTENANCE + " state.");
			  }
		  }
	  }
		
	}
	
	/* When user press 'UP' or 'DOWN' outside the lift to request for a lift, below 
	 * method finds the lift that is already nearest on the way (in same direction)
	 * ------------------------------------
	*/
	private Lift getNearestOnTheWayLift(int currentFloor, LiftDirection iftDirection){
		Lift nearestOnTheWayLift = null ;
		int shortestPathDistance = Lift.TOTAL_FLOORS;
		List<Lift> activeLiftList = getActiveLiftList();
		for (Lift lift : activeLiftList){
			if ((lift.getLiftDirection() == LiftDirection.UPWARD)  ){
			   if((currentFloor >= lift.getCurrentFloorNo()) && (currentFloor <= lift.getLiftRequestsPQueue().peek()) ) {
				if(Math.abs(lift.getCurrentFloorNo() - currentFloor) < shortestPathDistance){
					nearestOnTheWayLift = lift;
					System.out.println("getNearestOnTheWayLift UPWARD nearestOnTheWayLift = " + nearestOnTheWayLift.toString() + "\n lift.getCurrentFloorNo()=" + lift.getCurrentFloorNo() + " .currentFloor=" + currentFloor);
					shortestPathDistance = Math.abs(lift.getCurrentFloorNo() - currentFloor);
				}
				else{ 
					System.out.println("getNearestOnTheWayLift UPWARD ... lift NOT assigned. currentFloor=" + currentFloor);
				    }
			   }
			}
			else if((lift.getLiftDirection() == LiftDirection.DOWNWARD) ){
				if((currentFloor <= lift.getCurrentFloorNo() && currentFloor >= lift.getLiftRequestsPQueue().peek())){
				if (Math.abs(lift.getCurrentFloorNo() - currentFloor) < shortestPathDistance){
					nearestOnTheWayLift = lift;
					System.out.println("getNearestOnTheWayLift DOWNWARD nearestOnTheWayLift = " + nearestOnTheWayLift.toString()+ "\n lift.getCurrentFloorNo()=" + lift.getCurrentFloorNo()+ " .currentFloor=" + currentFloor);
					shortestPathDistance = Math.abs(lift.getCurrentFloorNo() - currentFloor);
				}
				else{ 
					System.out.println("getNearestOnTheWayLift DOWNWARD ... lift NOT assigned. currentFloor = " + currentFloor);
				}
				}
			}
		}
		return nearestOnTheWayLift;
	}
	
	/**
	 * 
	 * @return List of Lifts that are NOT UNDER MAINTENANCE
	 */
	public List<Lift> getActiveLiftList(){
		List<Lift> activeLiftList = new ArrayList<Lift>();
		for (Lift lift : allLiftList){
			if (lift.getLiftDirection() != LiftDirection.MAINTENANCE){
				activeLiftList.add(lift);
				System.out.println("getActiveLiftList(): Added to activeLiftList : lift to String : " + lift.toString());
				
			}
		}
		System.out.println("getActiveLiftList(): activeLiftList.size = " + activeLiftList.size());
				
		return activeLiftList;	
	}
	
	/**
	 * 
	 * @return a Lift that is already serving a request nearby
	 */
	private Lift getNearestReqServingLift(int currentFloor){
		Lift nearestReqServingLift = null ;
		int shortestPathDistance = Lift.TOTAL_FLOORS;
		List<Lift> activeLiftList = getActiveLiftList();
		for (Lift lift : activeLiftList){
			if (Math.abs(lift.getCurrentFloorNo() - currentFloor) < shortestPathDistance){
				nearestReqServingLift = lift;
				
				shortestPathDistance = Math.abs(lift.getCurrentFloorNo() - currentFloor);
			}
		}
		System.out.println("getNearestReqServingLift: nearestReqServingLift is :" + nearestReqServingLift.toString() + " .nearestReqServingLift.getCurrentFloorNo() ="+nearestReqServingLift.getCurrentFloorNo() + "  ..currentFloor=" + currentFloor);
		return nearestReqServingLift;
	}
	
	/* When user press 'UP' or 'DOWN' outside the lift to request for a lift, below 
	 * method finds the lift that is already nearest BUT IDLE
	 * ------------------------------------
	*/
	private Lift getNearestIdleLift(int currentFloor){
		Lift nearestIdleLift = null ;
		int shortestPathDistance = Lift.TOTAL_FLOORS;
		List<Lift> activeLiftList = getActiveLiftList();
		for (Lift lift : activeLiftList){
			int mathVal = Math.abs(lift.getCurrentFloorNo() - currentFloor);
			System.out.println("getNearestIdleLift mathVal = "+ mathVal + " ..shortestPathDistance=" + shortestPathDistance);
			
			if (lift.isLiftIdle() ){
				if(Math.abs(lift.getCurrentFloorNo() - currentFloor) < shortestPathDistance){
					nearestIdleLift = lift;
					System.out.println("getNearestIdleLift IDLE ... lift assigned ...nearestIdleLift :"+ nearestIdleLift.toString() + ".nearestIdleLift.getCurrentFloorNo() =" + nearestIdleLift.getCurrentFloorNo() + " .currentFloor="+currentFloor);
					shortestPathDistance = Math.abs(lift.getCurrentFloorNo() - currentFloor);
				}
				else{ 
					System.out.println("getNearestIdleLift IDLE ... lift NOT assigned");
				}
			}
			else {
				 System.out.println("getNearestIdleLift lift NOT IDLE ... lift Direction : " + lift.getLiftDirection() + " ..Lift current floor No: " + lift.getCurrentFloorNo());
			}
		}
		return nearestIdleLift;
	}

	void processLiftRequest()
	{
		for (Lift lift : allLiftList){
			if(lift.getLiftRequestsPQueue().isEmpty()){
				System.out.println("processLiftRequest: isEmpty()");
				lift.setLiftDirection(LiftDirection.IDLE);
			}
		}
		
		List<Lift> activeLiftList = getActiveLiftList();
		int startFloorNo = Lift.START_FLOOR_NO;
		for (Lift activeLift : activeLiftList){
			if(activeLift.isLiftIdle() ){
				// assign start floor No. to the lift
				System.out.println("processLiftRequest: isLiftIdle()");
				activeLift.addLiftRequestToFloor(startFloorNo);
			}
			if(!(activeLift.getLiftRequestsPQueue().isEmpty())){
				System.out.println("processLiftRequest: !(activeLift.getLiftRequestsPQueue().isEmpty()");
			      if(activeLift.getLiftRequestsPQueue().peek() == activeLift.getCurrentFloorNo()){
			    	  System.out.println("processLiftRequest:  getLiftUsers()");
			    	  activeLift.getLiftUsers();
			    	  
			      }
			      else{
			    	  System.out.println("processLiftRequest: moveOneFloor()");
			    	  activeLift.moveOneFloor();
			      }
			}
		}
			
		
	}
}
