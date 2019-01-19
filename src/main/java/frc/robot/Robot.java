package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final int A_BUTTON = 1;
  private static final int B_BUTTON = 2; 

private static final int CAN_LEFT_FRONT_MOTOR_CONTROLLER = 1;
private static final int CAN_RIGHT_FRONT_MOTOR_CONTROLLER = 3;
private static final int CAN_LEFT_REAR_MOTOR_CONTROLLER = 0;
private static final int CAN_RIGHT_REAR_MOTOR_CONTROLLER = 2;

private static final int PCM_SOLENOID_FORWARD = 0;
private static final int PCM_SOLENOID_REVERSE = 7;




  // Robot Drive Components
  private SpeedController leftFront;
  private SpeedController leftRear;
  private SpeedController rightFront;
  private SpeedController rightRear;
  private SpeedControllerGroup left;
  private SpeedControllerGroup right;
  private DifferentialDrive drive;
  private GenericHID driverOne;

  private PowerDistributionPanel pdp;
  private Compressor compressor;
  private DoubleSolenoid solenoid;

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

    driverOne = new Joystick(0);
    compressor = new Compressor();
    compressor.setClosedLoopControl(true);
    solenoid = new DoubleSolenoid(PCM_SOLENOID_FORWARD, PCM_SOLENOID_REVERSE);
    solenoid.set(Value.kOff);
    CameraServer.getInstance().startAutomaticCapture();
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
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(-driverOne.getRawAxis(1), driverOne.getRawAxis(0));

    if (driverOne.getRawButton(A_BUTTON)) {
      solenoid.set(Value.kForward);
    } else if (driverOne.getRawButton(B_BUTTON)) {
      solenoid.set(Value.kReverse);
    } 
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}





