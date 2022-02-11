package com.pigmice.frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RPMPControllerTest {

    private abstract static class MotorEncoderSim {
        protected double motorSetting = 0.0;
        public void setMotorSetting(double motorSetting) {
            this.motorSetting = motorSetting;
        }

        public abstract double getEncoderValue();
    }

    @Test
    public void testRPMPController2To1() {
        MotorEncoderSim sim = new MotorEncoderSim() {
			@Override
			public double getEncoderValue() {
                return motorSetting * 2;
			}
        };
        RPMPController controller = new RPMPController(0.5,10000);
        controller.setTargetRPM(700);
        for (int i=0;i<100;i++) {
            sim.setMotorSetting(controller.update(Utils.calculateRPM(sim.getEncoderValue(), FeedbackDevice.CTRE_MagEncoder_Relative)));
            System.out.println(Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative));
            if (Math.abs(700d-Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative)) < 1.0) {
                System.out.println("Found in "+i+" iterations");                
                break;
            }
        }
        double rpm = Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative);
        System.out.println(rpm);
        Assert.assertEquals(700d,rpm,1.0);
    }

    @Test
    public void testRPMPControllerWeird() {
        MotorEncoderSim sim = new MotorEncoderSim() {
			@Override
			public double getEncoderValue() {
                return motorSetting * .8232;
			}
        };
        RPMPController controller = new RPMPController(0.5,100);
        controller.setTargetRPM(700);
        for (int i=0;i<100;i++) {
            sim.setMotorSetting(controller.update(Utils.calculateRPM(sim.getEncoderValue(), FeedbackDevice.CTRE_MagEncoder_Relative)));
            System.out.println(Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative));
            if (Math.abs(700d-Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative)) < 1.0) {
                System.out.println("Found in "+i+" iterations");                
                break;
            }
        }
        double rpm = Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative);
        System.out.println(rpm);
        Assert.assertEquals(700d,rpm,1.0);
    }
    @Test
    public void testRPMPControllerOffset() {
        MotorEncoderSim sim = new MotorEncoderSim() {
			@Override
			public double getEncoderValue() {
                return motorSetting * .8232+120;
			}
        };
        RPMPController controller = new RPMPController(0.3,100);
        controller.setTargetRPM(700);
        for (int i=0;i<100;i++) {
            sim.setMotorSetting(controller.update(Utils.calculateRPM(sim.getEncoderValue(), FeedbackDevice.CTRE_MagEncoder_Relative)));
            if (Math.abs(700d-Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative)) < 1.0) {
                System.out.println("Found in "+i+" iterations");                
                break;
            }
        }
        double rpm = Utils.calculateRPM(sim.getEncoderValue(),FeedbackDevice.CTRE_MagEncoder_Relative);
        System.out.println(rpm);
        Assert.assertEquals(700d,rpm,1.0);
    }
}
