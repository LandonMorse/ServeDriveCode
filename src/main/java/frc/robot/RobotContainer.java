package frc.robot;

import java.io.File;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final CommandXboxController driver = new CommandXboxController(0);
    private final CommandXboxController auxdriver = new CommandXboxController(1);

    /* Drive Controls */
    // private final int translationAxis = XboxController.Axis.kLeftY.value;
    // private final int strafeAxis = XboxController.Axis.kLeftX.value;
    // private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();

    /*spicial opjects*/
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        NamedCommands.registerCommand("marker_1", s_Swerve.DefaultAutoCom());
        // configures all the autos 
        autoSetup();

        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve,
                driver::getLeftY,
                driver::getLeftX,
                driver::getRightX,
                ()-> false//driver.leftBumper().getAsBoolean()
            )
        );

        // s_Swerve.setDefaultCommand(
        //     new TeleopSwerve(
        //         s_Swerve, 
        //         () -> -driver.getRawAxis(translationAxis), 
        //         () -> -driver.getRawAxis(strafeAxis), 
        //         () -> -driver.getRawAxis(rotationAxis), 
        //         () -> driver.leftBumper().getAsBoolean()
        //     )
        // );
        
        // Configure the button bindings
        configureButtonBindings();
    }
    /*pathplanner auto asinmint and choser setup */
    private void autoSetup(){

    File pathPlannerFolder = new File(Filesystem.getDeployDirectory(), "pathplanner/autos");

    String[] autoFiles = pathPlannerFolder.list((dir, name) -> name.endsWith(".auto"));

    autoChooser.setDefaultOption("Default Auto", new InstantCommand());

    if (autoFiles != null) {
        for (String fileName : autoFiles) {
     // Remove extension for display
            String autoName = fileName.replace(".auto", "");

            System.out.println("autoname");

            autoChooser.addOption(autoName, AutoBuilder.buildAuto(autoName));
            }
        SmartDashboard.putData("Auto Mode", autoChooser);
        }
    } 
    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        driver.y().onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
        driver.leftBumper().whileTrue(new InstantCommand(()-> s_Swerve.setHeading(s_Swerve.getHeading())));
        
        /*Aux Driver Buttons */

    }
    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        
        return autoChooser.getSelected();
    }
}
