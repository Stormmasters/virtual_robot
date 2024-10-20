//This is the updated version of FraserDrive, with code to manage the arm and a servo. Feel free to test this in virtual_robot, just remember to select Arm bot or it will cause an error
package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp
public class FraserDriveV1 extends OpMode{
    DcMotor BL, FL, FR, BR, Arm;
    Servo S1;
    double LX, LY, RX, LXa, LYa, Angle, AngleA, r, sensitivity = 0.5, armPos, tickCount = 0;
    boolean fieldOriented = true, invControls = false, moveToStart = false, moveToMax = false;
    @Override
    public void init(){
        IMU imu = hardwareMap.get(IMU.class, "imu");
        Orientation orientation = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        // Configures IMU
        imu.resetYaw();
        // "Calibrates" IMU by making the current heading 0
        BL = hardwareMap.dcMotor.get("back_left_motor");
        FL = hardwareMap.dcMotor.get("front_left_motor");
        FR = hardwareMap.dcMotor.get("front_right_motor");
        BR = hardwareMap.dcMotor.get("back_right_motor");
        Arm = hardwareMap.dcMotor.get("arm_motor");
        S1 = hardwareMap.servo.get("hand_servo");
        Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // This matches the DcMotors to the ones configured in the Control Hub
        BL.setDirection(REVERSE);
        FL.setDirection(REVERSE);
        FR.setDirection(FORWARD);
        BR.setDirection(FORWARD);
        Arm.setDirection(FORWARD);
        S1.setDirection(Servo.Direction.FORWARD);
        // Sets motor directions
    }
    @Override
    public void loop(){
        telemetry.addLine("Controls: ");
        IMU imu = hardwareMap.get(IMU.class, "imu");
        Orientation orientation = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        // Gets the robot's current orientation, reconfigures
        if (gamepad1.left_bumper){
            if (fieldOriented){
                fieldOriented = false;
            }
            else{
                fieldOriented = true;
            }
        }
        if (fieldOriented){
            telemetry.addLine("Press the left bumper to disable field oriented control");
        }
        else{
            telemetry.addLine("Press the left bumper to enable field oriented control");
        }
        // This code allows the left bumper to switch whether fieldOriented is true
        if (gamepad1.right_bumper){
            if (invControls){
                invControls = false;
            }
            else{
                invControls = true;
            }
        }
        if (invControls){
            telemetry.addLine("Press the right bumper to disable inverted controls");
        }
        else{
            telemetry.addLine("Press the right bumper to enable inverted controls");
        }
        // This code allows the right bumper to switch whether invControls is true
        if (sensitivity < 1 && gamepad1.dpad_up){
            sensitivity += 0.0004;
        }
        else if (sensitivity > 0 && gamepad1.dpad_down){
            sensitivity -= 0.0004;
        }
        if (sensitivity > 1){
            sensitivity = 1;
        }
        else if (sensitivity < 0){
            sensitivity = 0;
        }
        telemetry.addLine("Press dpad down to lower the sensitivity and dpad up to raise it");
        // Right trigger makes sensitivity go up and vice versa
        if (gamepad1.b){
            imu.resetYaw();
        }
        telemetry.addLine("Press b to reset the IMU");
        // Resets/recalibrates IMU is b is pressed
        LX = sensitivity * -gamepad1.left_stick_x;
        LY = sensitivity * -gamepad1.left_stick_y;
        RX = sensitivity * -gamepad1.right_stick_x;
        if (invControls){
            LX *= -1;
            LY *= -1;
            RX *= -1;
        }
        // I like to turn controller inputs into smaller variables because "gamepad1.ect." is such a mouthful
        // It also multiplies them by "sensitivity", which allows the driver to change the sensitivity
        if (fieldOriented){
            r = Math.hypot(LY, LX);
            // Getting the "r", or distance of the controller input from the origin
            Angle = Math.atan2(LY, LX);
            // "atan2" converts a point into an angle from the origin
            AngleA = -orientation.firstAngle - Angle;
            // Difference of -yaw and Angle with the 0.5 * pi added because it starts out offset by 90 degrees
            LYa = -Math.sin(AngleA) * r;
            LXa = Math.cos(AngleA) * r;
            // The adjusted inputs are multiplied by r in order to get the magnitude of a movement, allowing for speed control
        }
        else{
            LYa = LY;
            LXa = LX;
            // If fieldOriented is turned off, the adjusted inputs are just set to the regular ones
        }
        BL.setPower(LYa + LXa - RX);
        FL.setPower(LYa - LXa - RX);
        FR.setPower(LYa + LXa + RX);
        BR.setPower(LYa - LXa + RX);
        // Sets motor powers
        telemetry.addData("Field oriented ", fieldOriented);
        telemetry.addData("Controls inverted", invControls);
        telemetry.addData("Sensitivity", sensitivity);
        if (gamepad1.left_trigger > 0 && Arm.getCurrentPosition() < 2240){
            Arm.setPower(0.5 * gamepad1.left_trigger);
            moveToStart = false;
        }
        else if (gamepad1.right_trigger > 0 && Arm.getCurrentPosition() > 0 && Arm.getCurrentPosition() != 0){
            Arm.setPower(-0.5 * gamepad1.right_trigger);
            moveToStart = false;
            S1.setPosition(0);
        }
        else{
            Arm.setPower(0);
        }
        if (gamepad1.dpad_left && !moveToStart){
            moveToStart = true;
            moveToMax = false;
        }
        if (gamepad1.dpad_right && !moveToMax){
            moveToMax = true;
            moveToStart = false;
        }
        if (gamepad1.a){
            moveToStart = false;
            moveToMax = false;
        }
        telemetry.addData("Arm position", Arm.getCurrentPosition());
        armPos = Arm.getCurrentPosition();
        if (Arm.getCurrentPosition() == 2240){
            telemetry.addLine("Max arm position reached");
        }
        if (Arm.getCurrentPosition() == 0){
            telemetry.addLine("Min arm position reached");
        }
        telemetry.addData("Arm power", Arm.getPower());
        if (moveToStart && Arm.getCurrentPosition() > 0){
            telemetry.addLine("Moving to min position");
            telemetry.addLine("Press 'a' to terminate movement");
            Arm.setPower(-0.5);
            S1.setPosition(0);
        }
        else{
            moveToStart = false;
        }
        if (moveToMax && Arm.getCurrentPosition() < 2240){
            telemetry.addLine("Moving to max position");
            telemetry.addLine("Press 'x' to terminate movement");
            Arm.setPower(0.5);
        }
        else{
            moveToMax = false;
        }
        if (Arm.getCurrentPosition() == 2240){
            telemetry.addLine("Press 'y' to close claw amd 'x' to open claw");
            telemetry.addData("Current servo position", S1.getPosition());
            if (S1.getPosition() > 0.4){
                S1.setPosition(0.4);
            }
            else if (S1.getPosition() < 0){
                S1.setPosition(0);
            }
            if (gamepad1.y && S1.getPosition() < 0.4){
                S1.setPosition(S1.getPosition() + 0.001);
            }
            else if (gamepad1.x && S1.getPosition() > 0){
                S1.setPosition(S1.getPosition() - 0.001);
            }
            else if (S1.getPosition() >= 0.4){
                telemetry.addLine("Max servo position reached");
            }
            else if (S1.getPosition() <= 0){
                telemetry.addLine("Min servo position reached");
            }
        }
    }
}
