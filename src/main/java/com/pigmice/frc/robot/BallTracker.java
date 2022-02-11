package com.pigmice.frc.robot;

import java.util.LinkedList;
import java.util.Queue;

//this here class is to find where yo ballz at
public class BallTracker{

    Queue<BallColor> balls = new LinkedList<>();
    
    //set the first position to a color and shift everything else up
    public void newBallStored(BallColor color){
        balls.add(color);
    }

    //get rid of the second position and shift everything else up
    public void ballLaunched(){
        balls.remove();
    }

    //return the color of ball in a spot. 0 is the next one to be shot, 1 is the second one to be shot 
    public String getCompartment(int spotNumber){
        if(number == 0)
        {
            return balls.peek();
        }
        else if(number == 1)   
        {
            return balls.element();
        }
    }

    enum BallColor {
        RED,BLUE,NONE
    }
}
