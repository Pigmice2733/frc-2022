// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.util.ArrayList;
import java.util.List;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.ShootBallCommand;
import com.pigmice.frc.robot.commands.VisionAlignCommand;
import com.pigmice.frc.robot.commands.climber.ClimbRung;
import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.indexer.SpinIndexerToAngle;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.StartShooterCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Subsystem;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;
import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;
import com.pigmice.frc.robot.testmode.Testable;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
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

	private List<Subsystem> subsystems;

	private XboxController driver;
	private XboxController operator;
	private GenericHID dpad;

	private boolean shootMode;

	private Trigger shootTarmac, shootLaunchpad, shootLow, shootFender, shootTrigger;

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

		subsystems = List.of(drivetrain, intake, shooter, indexer, lifty.getLeft(), lifty.getRight(), rotato.getLeft(),
				rotato.getRight());

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

		// rotato.setDefaultCommand(new RotateTo(rotato, this.rotato::getTarget, true,
		// this::usePower));
		// lifty.setDefaultCommand(new LiftTo(lifty, this.lifty::getTarget, true,
		// this::usePower));

		// Configure the button bindings
		try {
			configureButtonBindings(driver, operator, dpad);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

		final VisionAlignCommand visionAlign = new VisionAlignCommand(this.drivetrain, driver);
		new JoystickButton(driver, Button.kA.value)
				.whenPressed(visionAlign)
				.whenReleased(() -> CommandScheduler.getInstance().cancel(visionAlign));
		 

		// OPERATOR CONTROLS

		new JoystickButton(operator, Button.kLeftStick.value)
				.whenPressed(this::toggleShootMode);

		// TODO Create target variables for both rotato and lifty that the default
		// commands will use
		// make a double supplier that returns those and pass it in as the target state
		// for both default commands

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kRightBumper.value).get())
				.whenActive(() -> {
					this.lifty.setInAuto(false);
					this.lifty.setOutput(0.30);
				})
				.whenInactive(() -> this.lifty.setOutput(0.0));

		new Trigger(() -> shootMode == true &&
				new JoystickButton(operator, Button.kA.value).get())
				.whenActive(
					new ExtendIntake(intake)
				)
				.whenInactive(
					new RetractIntake(intake)
				);

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kLeftBumper.value).get())
				.whenActive(() -> {
					this.lifty.setInAuto(false);
					this.lifty.setOutput(-0.30);
				})
				.whenInactive(() -> this.lifty.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kA.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(-0.30);
				})
				.whenInactive(() -> this.rotato.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kB.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(0.30);
				})
				.whenInactive(() -> this.rotato.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kX.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(-0.15);
				})
				.whenInactive(() -> {
					this.rotato.setOutput(0.0);
				});

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kY.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(0.15);
				})
				.whenInactive(() -> {
					this.rotato.setOutput(0.0);
				});

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kRightStick.value).get())
				.whenActive(new ClimbRung(lifty, rotato));

		/*
		 * new Trigger(() -> mode == false &&
		 * new JoystickButton(operator, Button.kBack.value).get())
		 * .whenActive(new ClimbMid(lifty, rotato));
		 */

		// new Trigger(() -> shootMode == true &&
		// new JoystickButton(operator, Button.kA.value).get())
		// .whenActive(new ExtendIntake(intake))
		// .whenInactive(new RetractIntake(intake));

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

	public void onInit() {
		this.drivetrain.init();
	}

	public void onEnable() {
		this.shooter.setMode(ShooterMode.OFF);
		this.shooter.enable();
		this.indexer.enable();
		this.intake.enable();
		this.intake.resetEncoders();
	}

	public void onDisable() {
		this.shooter.setMode(ShooterMode.OFF);
		CommandScheduler.getInstance().cancelAll();
		this.shooter.disable();
		this.indexer.disable();
		this.intake.disable();
	}

	public void nonTestInit() {
		this.subsystems.forEach(subsystem -> {
			subsystem.setTestMode(false);
		});
	}

	public void testInit() {
		this.subsystems.forEach(subsystem -> {
			subsystem.testInit();
		});
	}

	public void testPeriodic() {
		this.subsystems.forEach(subsystem -> {
			subsystem.testPeriodic();
		});
	}

	public void updateShuffleboard() {
		this.indexer.updateShuffleboard();
	}

	private void toggleShootMode() {
		this.shootMode = !shootMode;
		Controls.rumbleController(this.operator);
	}

	public SequentialCommandGroup getShooterModeCommands(ShooterMode mode) {
		return new SequentialCommandGroup(
				new InstantCommand(() -> {
					this.indexer.setMode(IndexerMode.ANGLE);
					this.indexer.stopMotor();
				}),
				new WaitUntilCommand(() -> this.indexer.getEncoderVelocity() == 0),
				new SpinIndexerToAngle(this.indexer, 5.0, false),
				new StartShooterCommand(shooter, mode));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 * 
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		return new SpinIndexerToAngle(this.indexer, -90.0, false);
	}

	public List<Testable> getTestables() {
		List<Testable> result = new ArrayList<>();
		return result;
	}
}
