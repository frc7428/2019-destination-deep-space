package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
// Previous Values on A_BUTTON = 1 and B_BUTTON = 2

public class Robot extends TimedRobot {
  private static final int A_BUTTON = 1;
  private static final int B_BUTTON = 2; 
  private static final int Y_BUTTON = 4;
  private static final int X_BUTTON = 3;

  //private static final int RIGHT_Y_AXIS = 5;
  private static final int LEFT_BUMPER_BUTTON = 5;
  private static final int RIGHT_BUMPER_BUTTON = 6; 
  private static final int BACK_BUTTON = 7;
  private static final int START_BUTTON = 8;
  private static final int TRIGGER_BUTTON = 1;
  private static final int SIDETRIGGER_BUTTON = 2;

  
  // Designed for Extreme 3D Pro; Camera switching 
  private static final int CAM_BUTTON_FRONT = 11;
  private static final int CAM_BUTTON_BACK = 12;
  private static final int CAM_BUTTON_BOTH = 10;

private static final int CAN_LEFT_FRONT_MOTOR_CONTROLLER = 1;
private static final int CAN_RIGHT_FRONT_MOTOR_CONTROLLER = 3;
private static final int CAN_LEFT_REAR_MOTOR_CONTROLLER = 0;
private static final int CAN_RIGHT_REAR_MOTOR_CONTROLLER = 2;

private static final int PCM_SOLENOID_FORWARD = 5;
private static final int PCM_SOLENOID_REVERSE = 2;

private static final int PCM_SOLENOID_OPEN_HATCH = 0;
private static final int PCM_SOLENOID_CLOSE_HATCH = 7;

private static final int PCM_SOLENOID_IN_HOLDER = 1;
private static final int PCM_SOLENOID_OUT_HOLDER = 6;

private static final int PCM_SOLENOID_RAISE = 3;
private static final int PCM_SOLENOID_LOWER = 4;


//private static final int CAN_BALL_ANGLE = 4;
private static final int CAN_TOP_ROLLER = 4;
private static final int CAN_BOTTOM_ROLLER = 5; 


// Robot Drive Components
  private SpeedController leftFront;
  private SpeedController leftRear;
  private SpeedController rightFront;
  private SpeedController rightRear;
  private SpeedControllerGroup left;
  private SpeedControllerGroup right;
  private DifferentialDrive drive;
  private GenericHID driverOne;
  private GenericHID driverTwo;
  //private SpeedController ballAngle; 
  private SpeedController topRoller;
  private SpeedController bottomRoller; 

  //private PowerDistributionPanel pdp;
  private Compressor compressor;
  private DoubleSolenoid solenoid;
  private DoubleSolenoid solenoidHatch;
  private DoubleSolenoid solenoidHolder;
  private DoubleSolenoid solenoidLift;


  private UsbCamera front = null;
  private UsbCamera rear = null;

  // Still working on this 
  //private BallAssembly ;

  // May need to use this for camera switching 
// private UsbCamera camera; 

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    leftFront = new WPI_VictorSPX(CAN_LEFT_FRONT_MOTOR_CONTROLLER);
    leftRear = new WPI_VictorSPX(CAN_LEFT_REAR_MOTOR_CONTROLLER);
    left = new SpeedControllerGroup(leftFront, leftRear);
    rightFront = new WPI_VictorSPX(CAN_RIGHT_FRONT_MOTOR_CONTROLLER);
    rightRear = new WPI_VictorSPX(CAN_RIGHT_REAR_MOTOR_CONTROLLER);
    right = new SpeedControllerGroup(rightFront, rightRear);
    drive = new DifferentialDrive(left, right);
    
    //ballAngle = new Spark(Robot.PWM_BALL_ANGLE);
    topRoller = new WPI_VictorSPX(Robot.CAN_TOP_ROLLER);
    bottomRoller = new WPI_VictorSPX(Robot.CAN_BOTTOM_ROLLER);
//  switched
    driverOne = new Joystick(1);
    driverTwo = new XboxController(0);

    compressor = new Compressor();
    compressor.setClosedLoopControl(true);
    solenoid = new DoubleSolenoid(PCM_SOLENOID_FORWARD, PCM_SOLENOID_REVERSE);
    solenoid.set(Value.kOff);

    solenoidHatch = new DoubleSolenoid(PCM_SOLENOID_OPEN_HATCH,PCM_SOLENOID_CLOSE_HATCH);
    solenoidHatch.set(Value.kOff);

    solenoidHolder = new DoubleSolenoid(PCM_SOLENOID_IN_HOLDER,PCM_SOLENOID_OUT_HOLDER);
    solenoid.set(Value.kOff);

    solenoidLift = new DoubleSolenoid(PCM_SOLENOID_RAISE,PCM_SOLENOID_LOWER);
    solenoid.set(Value.kOff);

    rear = CameraServer.getInstance().startAutomaticCapture(0);
    front = CameraServer.getInstance().startAutomaticCapture(1);

    rear.setResolution(1600, 1200);
    front.setResolution(1600, 1200);

    //Trying to find the function in order to switch cameras
    //camera = UsbCamera
   
  }

  /**P
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    this.teleopPeriodic();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(-driverOne.getRawAxis(1), driverOne.getRawAxis(0));

    //ballAngle.set(driverTwo.getRawAxis(Robot.RIGHT_Y_AXIS));
    SmartDashboard.putNumber("left speed", left.get());
    SmartDashboard.putNumber("right speed", -right.get());
    SmartDashboard.putBoolean("Cylinder Out", solenoidHolder.get() == Value.kForward);
    
    if (driverTwo.getRawButton(Robot.LEFT_BUMPER_BUTTON)) {
      topRoller.set(1);
      bottomRoller.set(-1);
    } else if (driverTwo.getRawButton(Robot.RIGHT_BUMPER_BUTTON)) {
      topRoller.set(-1);
      bottomRoller.set(1);
    } else {
      topRoller.set(0);
      bottomRoller.set(0);
    }

    // Maybe add this as a timed command
    if (driverTwo.getRawButton(Y_BUTTON)) {
      solenoidHatch.set(solenoidHatch.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }

    if (driverTwo.getRawButton(BACK_BUTTON)) {
      solenoidHolder.set(solenoidHolder.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }

    if (driverTwo.getRawButton(A_BUTTON)) {
      solenoid.set(solenoid.get() == Value.kForward ? Value.kReverse : Value.kForward);
    }

    if (driverOne.getRawButton(TRIGGER_BUTTON)) {
      solenoidLift.set(solenoidLift.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }

   // Experimental Setup Camera
   // Cameras in this script may run in a loop 
   // By switching Camera we can save bandwidth and have clearer images. 
   // If needed we can use both at the same time (In theory with the use of 10 button)
   // Front Camera should be camera 0 and should start automatically and can be switched to front with 11
   // Back Camera should be activated with use of button 12 
   // May need to use USB 
   // Cameraserver.getInstance().startautomaticCapture maybe the wrong way of calling the different cameras


  //  if (driverOne.getRawButton(CAM_BUTTON_FRONT)) {
  //    if (rear != null) {
  //     CameraServer.getInstance().removeCamera("rear");
  //     rear = null;
  //    }

  //    if (front == null) {
  //     front = CameraServer.getInstance().startAutomaticCapture("front", 0);
  //   }
     
  // } else if (driverOne.getRawButton(CAM_BUTTON_BACK)) {
  //   if (front != null) {
  //     CameraServer.getInstance().removeCamera("front");
  //     front = null;
  //   }

  //   if (rear == null) {
  //     rear = CameraServer.getInstance().startAutomaticCapture("rear",1);
  //   }
  // } else if (driverOne.getRawButton(CAM_BUTTON_BOTH)) {
  //   if (front == null) {
  //     front = CameraServer.getInstance().startAutomaticCapture("front", 0);
  //   }
    
  //   if (rear == null) {
  //     rear = CameraServer.getInstance().startAutomaticCapture("rear",1);
  //   }  

  // } 
  

}
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    if (driverTwo.getRawButton(Robot.A_BUTTON)) {
      //pdp.clearStickyFaults();
    }
  }
}


