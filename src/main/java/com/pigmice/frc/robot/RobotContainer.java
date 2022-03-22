// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.ShootBallCommand;
import com.pigmice.frc.robot.commands.VisionAlignCommand;
import com.pigmice.frc.robot.commands.climber.ClimbRung;
import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.drivetrain.Ramsete;
import com.pigmice.frc.robot.commands.indexer.SpinIndexerToAngle;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.StartShooterCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;
import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;
import com.pigmice.frc.robot.testmode.Testable;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

	// The robot's subsystems and commands are defined here...
	private final Drivetrain drivetrain;
	private final Intake intake;
	private final Indexer indexer;
	private final Shooter shooter;
	private final Lifty lifty;
	private final Rotato rotato;
	// private final Lights lights;
	private final Controls controls;

	private XboxController driver;
	private XboxController operator;
	private GenericHID dpad;

	private boolean shootMode;

	private Trigger shootTarmac, shootLaunchpad, shootLow, shootFender, shootTrigger;

	private SendableChooser<String> autoTrajectories;
	String trajectoryJSON = "";
	Trajectory trajectory;

	// private final ExampleCommand m_autoCommand = new
	// ExampleCommand(m_exampleSubsystem);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		drivetrain = new Drivetrain();
		intake = new Intake();
		shooter = new Shooter();
		indexer = new Indexer(this.intake, this.shooter);
		lifty = new Lifty();
		rotato = new Rotato();
		// lights = new Lights();

		driver = new XboxController(Constants.driverControllerPort);
		operator = new XboxController(Constants.operatorControllerPort);
		controls = new Controls(driver, operator);

		shootMode = true;

		shootTarmac = new Trigger(() -> shootMode == true && new POVButton(operator, 0).get());
		shootLaunchpad = new Trigger(() -> shootMode == true && new POVButton(operator, 90).get());
		shootLow = new Trigger(() -> shootMode == true && new POVButton(operator, 180).get());
		shootFender = new Trigger(() -> shootMode == true && new POVButton(operator, 270).get());
		shootTrigger = new Trigger(
				() -> operator.getRightTriggerAxis() >= (1 - DrivetrainConfig.axisThreshold));

		drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain,
				controls::getDriveSpeed, controls::getTurnSpeed));

		// rotato.setDefaultCommand(new RotateTo(rotato, () -> this.rotateOutput, true,
		// () -> true));
		// lifty.setDefaultCommand(new LiftTo(lifty, () -> this.liftOutput, true, () ->
		// true));

		// Configure the button bindings
		try {
			configureButtonBindings(driver, operator, dpad);
		} catch (Exception e) {
			e.printStackTrace();
		}

		autoTrajectories = new SendableChooser<String>();
		autoTrajectories.addOption("1 ball, left position", "PathWeaver/Autos/oneBallLeft");
		autoTrajectories.addOption("2 balls, center position, shoot first", "PathWeaver/Autos/threeBallsCenter");
		autoTrajectories.addOption("2 balls, right position, shoot first", "PathWeaver/Autos/threeBallsRight");
		autoTrajectories.addOption("2 balls, center position, pick up first", "PathWeaver/Autos/twoBallsCenter");
		autoTrajectories.addOption("2 balls, right position, pick up first", "PathWeaver/Autos/twoBallsRight");
		trajectory = new Trajectory();
	}

	private double rotateOutput = 0.0;
	private double liftOutput = 0.0;
	private double distanceToHub = 0.0d; // TODO define this

	/**
	 * Use this method to define your button -> command mappings. Buttons can be
	 * created by instantiating a {@link GenericHID} or one of its subclasses
	 * ({@link
	 * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
	 * it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	 */
	private void configureButtonBindings(XboxController driver, XboxController operator, GenericHID pad) {

		// DRIVER CONTROLS

		new JoystickButton(driver, Button.kY.value)
				.whenPressed(this.drivetrain::slow)
				.whenReleased(this.drivetrain::stopSlow);

		final VisionAlignCommand visionAlign = new VisionAlignCommand(this.drivetrain);
		new JoystickButton(driver, Button.kA.value)
				.whenPressed(visionAlign)
				.whenReleased(() -> CommandScheduler.getInstance().cancel(visionAlign));

		new JoystickButton(driver, Button.kX.value)
				.whenPressed(new ExtendIntake(intake));
		// .whenReleased(intake::disable);

		// OPERATOR CONTROLS

		new JoystickButton(operator, Button.kLeftStick.value)
				.whenPressed(() -> this.shootMode = !shootMode);

		// TODO Create target variables for both rotato and lifty that the default
		// commands will use
		// make a double supplier that returns those and pass it in as the target state
		// for both default commands

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kRightBumper.value).get())
				.whenActive(() -> this.liftOutput = 0.30)
				.whenInactive(() -> this.liftOutput = 0.00);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kLeftBumper.value).get())
				.whenActive(() -> this.liftOutput = -0.30)
				.whenInactive(() -> this.liftOutput = 0.00);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kA.value).get())
				.whenActive(() -> this.rotateOutput = 0.35)
				.whenInactive(() -> this.rotateOutput = 0.00);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kB.value).get())
				.whenActive(() -> this.rotateOutput = -0.35)
				.whenInactive(() -> this.rotateOutput = 0.00);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kX.value).get())
				.whenActive(() -> this.rotateOutput = 0.15)
				.whenInactive(() -> this.rotateOutput = 0.00);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kY.value).get())
				.whenActive(() -> this.rotateOutput = -0.15)
				.whenInactive(() -> this.rotateOutput = 0.00);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kRightStick.value).get())
				.whenActive(new ClimbRung(lifty, rotato));

		/*
		 * new Trigger(() -> mode == false &&
		 * new JoystickButton(operator, Button.kBack.value).get())
		 * .whenActive(new ClimbMid(lifty, rotato));
		 */

		new Trigger(() -> shootMode == true &&
				new JoystickButton(operator, Button.kA.value).get())
				.whenActive(new ExtendIntake(intake))
				.whenInactive(new RetractIntake(intake));

		this.shootTrigger
				.whileActiveOnce(new ShootBallCommand(shooter, indexer))
				.whenInactive(() -> this.indexer.setMode(IndexerMode.FREE_SPIN));

		this.shootTarmac
				.whenActive(getShooterModeCommands(ShooterMode.TARMAC))
				.whenInactive(new StartShooterCommand(shooter, ShooterMode.OFF));

		this.shootLaunchpad
				.whenActive(getShooterModeCommands(ShooterMode.LAUNCHPAD))
				.whenInactive(new StartShooterCommand(shooter, ShooterMode.OFF));

		this.shootLow
				.whenActive(getShooterModeCommands(ShooterMode.FENDER_LOW))
				.whenInactive(new StartShooterCommand(shooter, ShooterMode.OFF));

		this.shootFender
				.whenActive(getShooterModeCommands(ShooterMode.FENDER_HIGH))
				.whenInactive(new StartShooterCommand(shooter, ShooterMode.OFF));

		this.shootTrigger.whenActive(() -> {
			if (this.shooter.getMode() == ShooterMode.OFF) {
				this.shooter.setMode(ShooterMode.AUTO);
			}
		});

		shootTrigger.or(shootTarmac).or(shootLaunchpad).or(shootLow).or(shootFender)
				.whenInactive(() -> {
					this.shooter.disable();
					this.indexer.setMode(IndexerMode.FREE_SPIN);
				});
	}

	public void onEnable() {
		this.shooter.setMode(ShooterMode.AUTO);
		this.shooter.enable();
		this.indexer.enable();
		this.intake.enable();
	}

	public void onDisable() {
		this.shooter.setMode(ShooterMode.OFF);
		CommandScheduler.getInstance().cancelAll();
		this.shooter.disable();
		this.indexer.disable();
		this.intake.disable();
	}

	public SequentialCommandGroup getShooterModeCommands(ShooterMode mode) {
		return new SequentialCommandGroup(
				new InstantCommand(() -> {
					this.indexer.setMode(IndexerMode.ANGLE);
					this.indexer.stopMotor();
				}),
				new WaitUntilCommand(() -> this.indexer.getEncoderVelocity() == 0),
				new SpinIndexerToAngle(this.indexer, -90.0, false),
				new StartShooterCommand(shooter, mode));
	}

	// private double getPower() {
	// return usePower() ? this.rotateOutput : rotato.getTarget();
	// }

	// private boolean usePower() {
	// return operator.getAButton() || operator.getBButton() ||
	// operator.getRightBumper() || operator.getLeftBumper();
	// }

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 * 
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// return new SPathCommand(this.drivetrain);

		trajectoryJSON = autoTrajectories.getSelected();
		try {
			trajectory = TrajectoryUtil.fromPathweaverJson(
				Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON));
		} catch (IOException ex) {
			DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
		}
		return new Ramsete(this.drivetrain, trajectory);
	}

	public List<Testable> getTestables() {
		List<Testable> result = new ArrayList<>();
		return result;
	}
}
