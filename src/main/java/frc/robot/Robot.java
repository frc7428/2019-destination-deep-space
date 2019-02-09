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
  // Designed for Extreme 3D Pro; Camera switching 
  private static final int CAM_BUTTON_FRONT = 11;
  private static final int CAM_BUTTON_BACK = 12;
  private static final int CAM_BUTTON_BOTH = 10;

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
  private SpeedController topRoller;
  private SpeedController bottomRoller; 

  private Compressor compressor;
  private DoubleSolenoid solenoid;
  private DoubleSolenoid solenoidHatch;
  private DoubleSolenoid solenoidHolder;
  private DoubleSolenoid solenoidLift;

  private UsbCamera front = null;
  private UsbCamera rear = null; 

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    leftFront = new WPI_VictorSPX(RobotMap.CAN_LEFT_FRONT_MOTOR_CONTROLLER);
    leftRear = new WPI_VictorSPX(RobotMap.CAN_LEFT_REAR_MOTOR_CONTROLLER);
    left = new SpeedControllerGroup(leftFront, leftRear);
    rightFront = new WPI_VictorSPX(RobotMap.CAN_RIGHT_FRONT_MOTOR_CONTROLLER);
    rightRear = new WPI_VictorSPX(RobotMap.CAN_RIGHT_REAR_MOTOR_CONTROLLER);
    right = new SpeedControllerGroup(rightFront, rightRear);
    drive = new DifferentialDrive(left, right);
    
    topRoller = new WPI_VictorSPX(RobotMap.CAN_TOP_ROLLER);
    bottomRoller = new WPI_VictorSPX(RobotMap.CAN_BOTTOM_ROLLER);
//  switched
    driverOne = new Joystick(1);
    driverTwo = new XboxController(0);

    compressor = new Compressor();
    compressor.setClosedLoopControl(true);
    solenoid = new DoubleSolenoid(RobotMap.PCM_SOLENOID_FORWARD, RobotMap.PCM_SOLENOID_REVERSE);
    solenoid.set(Value.kOff);

    solenoidHatch = new DoubleSolenoid(RobotMap.PCM_SOLENOID_OPEN_HATCH, RobotMap.PCM_SOLENOID_CLOSE_HATCH);
    solenoidHatch.set(Value.kOff);

    solenoidHolder = new DoubleSolenoid(RobotMap.PCM_SOLENOID_IN_HOLDER, RobotMap.PCM_SOLENOID_OUT_HOLDER);
    solenoid.set(Value.kOff);

    solenoidLift = new DoubleSolenoid(RobotMap.PCM_SOLENOID_RAISE, RobotMap.PCM_SOLENOID_LOWER);
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

    SmartDashboard.putNumber("left speed", left.get());
    SmartDashboard.putNumber("right speed", -right.get());
    SmartDashboard.putBoolean("Cylinder Out", solenoidHolder.get() == Value.kForward);
    
    if (driverTwo.getRawButton(RobotMap.LEFT_BUMPER_BUTTON)) {
      topRoller.set(1);
      bottomRoller.set(-1);
    } else if (driverTwo.getRawButton(RobotMap.RIGHT_BUMPER_BUTTON)) {
      topRoller.set(-1);
      bottomRoller.set(1);
    } else {
      topRoller.set(0);
      bottomRoller.set(0);
    }

    // Maybe add this as a timed command
    if (driverTwo.getRawButton(RobotMap.Y_BUTTON)) {
      solenoidHatch.set(solenoidHatch.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }

    if (driverTwo.getRawButton(RobotMap.BACK_BUTTON)) {
      solenoidHolder.set(solenoidHolder.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }

    if (driverTwo.getRawButton(RobotMap.A_BUTTON)) {
      solenoid.set(solenoid.get() == Value.kForward ? Value.kReverse : Value.kForward);
    }

    if (driverOne.getRawButton(RobotMap.TRIGGER_BUTTON)) {
      solenoidLift.set(solenoidLift.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }
}
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    if (driverTwo.getRawButton(RobotMap.A_BUTTON)) {
      //pdp.clearStickyFaults();
    }
  }
}


