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
import com.pigmice.frc.robot.commands.drivetrain.Auto2BallTarmacCenter;
import com.pigmice.frc.robot.commands.drivetrain.Auto2BallTarmacSide;
import com.pigmice.frc.robot.commands.drivetrain.AutoShootAndDrive;
import com.pigmice.frc.robot.commands.drivetrain.AutoShootFromFender;
import com.pigmice.frc.robot.commands.drivetrain.DriveDistance;
import com.pigmice.frc.robot.commands.indexer.SpinIndexerToAngle;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.StartShooterCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;
import com.pigmice.frc.robot.subsystems.Subsystem;
import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;
import com.pigmice.frc.robot.testmode.Testable;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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

	private boolean shootMode;

	private Trigger shootTarmac, shootLaunchpad, shootLow, shootFender, shootTrigger;

	private static final double liftPower = 0.30;
	private static final double rotatePower = 0.50;

	private SendableChooser<Command> autoChooser;

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

		SmartDashboard.putBoolean("Shoot Mode", shootMode);

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

		List<Command> autoCommands = List.of(new Auto2BallTarmacCenter(indexer, shooter, intake, drivetrain),
				new Auto2BallTarmacSide(indexer, shooter, intake, drivetrain),
				new AutoShootFromFender(indexer, shooter, intake),
				new AutoShootAndDrive(drivetrain, indexer, shooter, intake), new DriveDistance(drivetrain, 1.0));

		autoChooser = new SendableChooser<>();

		autoChooser.addOption("None", new WaitCommand(1.0));

		autoCommands.forEach(command -> {
			autoChooser.addOption(command.getName(), command);
		});

		Shuffleboard.getTab("Driver").add("Auto", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

		autoChooser.close();

		// Configure the button bindings
		try {
			configureButtonBindings(driver, operator);
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
	private void configureButtonBindings(XboxController driver, XboxController operator) {

		// DRIVER CONTROLS

		// [driver] slow mode makes the robot move and turn more slowly
		new JoystickButton(driver, Button.kY.value)
				.whenPressed(this.drivetrain::slow)
				.whenReleased(this.drivetrain::stopSlow);

		// [driver] align to hub using gloworm
		final VisionAlignCommand visionAlign = new VisionAlignCommand(this.drivetrain, driver, operator);
		new JoystickButton(driver, Button.kA.value)
				.whenPressed(visionAlign)
				.whenReleased(() -> {
					visionAlign.stop();
					visionAlign.cancel();
					this.drivetrain.stop();
				});

		// [driver] emergency stop
		new JoystickButton(driver, Button.kStart.value)
				.whenPressed(() -> {
					this.drivetrain.stop();
				});

		// OPERATOR CONTROLS

		// [operator] toggle shoot mode
		new JoystickButton(operator, Button.kLeftStick.value)
				.whenPressed(this::toggleShootMode);

		// [operator] extend and retract intake
		new Trigger(() -> shootMode == true &&
				new JoystickButton(operator, Button.kA.value).get())
				.whenActive(
						new ExtendIntake(intake, indexer))
				.whenInactive(
						new RetractIntake(intake, indexer));

		// new Trigger(() -> shootMode == true && new JoystickButton(operator,
		// Button.kRightBumper.value).get())
		// .whenPressed();

		// [operator] manually eject all balls
		new Trigger(() -> shootMode == true &&
				new JoystickButton(operator, Button.kBack.value).get())
				.whenActive(() -> {
					this.indexer.setMode(IndexerMode.EJECT_BY_INTAKE);
					this.intake.setReverse(true);
				})
				.whenInactive(() -> {
					this.indexer.setMode(IndexerMode.FREE_SPIN);
					this.intake.setReverse(false);
					this.indexer.clearBalls();
				});

		// [operator] manual climb keybinds
		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kRightBumper.value).get())
				.whenActive(() -> {
					this.lifty.setInAuto(false);
					this.lifty.setOutput(liftPower);
				})
				.whenInactive(() -> this.lifty.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kLeftBumper.value).get())
				.whenActive(() -> {
					this.lifty.setInAuto(false);
					this.lifty.setOutput(-liftPower);
				})
				.whenInactive(() -> this.lifty.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kA.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(rotatePower);
				})
				.whenInactive(() -> this.rotato.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kB.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(-rotatePower);
				})
				.whenInactive(() -> this.rotato.setOutput(0.0));

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kX.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(rotatePower / 2.0);
				})
				.whenInactive(() -> {
					this.rotato.setOutput(0.0);
				});

		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kY.value).get())
				.whenActive(() -> {
					this.rotato.setInAuto(false);
					this.rotato.setOutput(-rotatePower / 2.0);
				})
				.whenInactive(() -> {
					this.rotato.setOutput(0.0);
				});

		// ! UNUSED [operator] automatically climb a rung
		new Trigger(() -> shootMode == false &&
				new JoystickButton(operator, Button.kRightStick.value).get())
				.whenActive(new ClimbRung(lifty, rotato));

		// [operator] shoot a ball
		this.shootTrigger
				.whileActiveContinuous(new ShootBallCommand(shooter, indexer))
				.whenInactive(() -> this.indexer.setMode(IndexerMode.FREE_SPIN));

		// [operator] settings to spin up flywheels
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

		// turn shooter to auto mode when only trigger is pressed, and no shoot modes
		this.shootTrigger.whenActive(() -> {
			// only ever == ShooterMode.OFF when no other mode buttons are pressed
			if (this.shooter.getMode() == ShooterMode.OFF) {
				this.shooter.setMode(ShooterMode.AUTO);
			}
		});

		// disable shooter and reset indexer when no shoot trigger or mode buttons are
		// pressed
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
		this.indexer.enable();
		this.intake.enable();
		this.intake.resetEncoders();
	}

	public void onDisable() {
		CommandScheduler.getInstance().cancelAll();
		this.shooter.disable();
		this.indexer.disable();
		this.intake.disable();
	}

	public void nonTestInit() {
		this.subsystems.forEach(subsystem -> {
			subsystem.setTestMode(false);
			subsystem.nonTestInit();
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
		this.subsystems.forEach(subsystem -> subsystem.updateShuffleboard());
	}

	private void toggleShootMode() {
		this.shootMode = !this.shootMode;
		SmartDashboard.putBoolean("Shoot Mode", shootMode);
		if (this.shootMode) {
			this.intake.enable();
			this.indexer.enable();
		} else {
			this.indexer.disable();
		}
		CommandScheduler.getInstance().cancelAll();
		Controls.rumbleController(this.operator);
	}

	public SequentialCommandGroup getShooterModeCommands(ShooterMode mode) {
		return new SequentialCommandGroup(
				new InstantCommand(() -> {
					this.indexer.setMode(IndexerMode.ANGLE);
					this.indexer.stopMotor();
				}),
				new WaitUntilCommand(() -> this.indexer.getEncoderVelocity() == 0),
				new SpinIndexerToAngle(this.indexer, -90.0, false).withTimeout(1.0),
				new StartShooterCommand(shooter, mode));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 * 
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {

		return autoChooser.getSelected();
	}

	public List<Testable> getTestables() {
		List<Testable> result = new ArrayList<>();
		return result;
	}
}
