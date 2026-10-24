import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class TalonMotorExample {
  //this is how you create a new talon motor include the id in the perinthases 
  private final TalonFX motor1 = new TalonFX(1);
  // this creates new configs for you to set limits on voltage and other things on the motor
  private TalonFXConfiguration exampleConfig = new TalonFXConfiguration();

  PositionVoltage pos = new PositionVoltage();

  private static final MotionMagicVoltage example = new  MotionMagicVoltage(0);
public TalonMotorExample(){
    //always do this to reset the motor upon start up 
    motor1.clearStickyFaults();
    // always have this for any motor you are runing this keeps from burn out 
    exampleConfig.CurrentLimits.SupplyCurrentLimit = 25;
    exampleConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    exampleConfig.CurrentLimits.SupplyCurrentLowerLimit = 30;
    exampleConfig.CurrentLimits.SupplyCurrentLowerTime = 35;
    /*if you have two motors conected recomend doing this set on or the other as a follower 
    *change from opposed if they are facing same way 
    */
    motor2.setControl(new Follower(motor1.getDeviceID(),
    MotorAlignmentValue.Opposed));

    exampleConfig.Slot0.kP = 0;
    exampleConfig.Slot0.kI = 0;
    exampleConfig.Slot0.kD = 0.1;

    exampleConfig.Slot0.kV = .12;

    exampleconfig.MotionMagic.MotionMagicCruiseVelocity = 40.0; 
    exampleconfig.MotionMagic.MotionMagicAcceleration = 80.0;   
    exampleconfig.MotionMagic.MotionMagicJerk = 0.0; 

    exampleconfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    exampleconfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    motor1.getConfigurator().apply(exampleConfig);
}
/*
this is how a command is ushally set up where speed is a number from 1 to 0 
determinds the speed output
*/
public Command motorSpin(double speed){
    return new InstantCommand(()-> motor1.set(speed));
}
/*  this is one of controlling the position of a motor so it goes to a certin position 
 */
public Command motorToRotations(double position){
    return new InstantCommand(()-> motor1.setControl(pos.withPosition(position)));
}
// this one is better for arms and elavators usally the best option 
public Command motorToRotationsMagic(double pos){
    return new InstantCommand(()-> motor1.setControl(example.withPosition(pos)));
}

}