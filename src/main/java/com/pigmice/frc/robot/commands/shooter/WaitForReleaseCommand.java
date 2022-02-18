package com.pigmice.frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class WaitForReleaseCommand extends WaitUntilCommand {

    public WaitForReleaseCommand() {
        super(250D);
    }
}
