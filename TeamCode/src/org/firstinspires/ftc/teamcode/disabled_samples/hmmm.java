package org.firstinspires.ftc.teamcode.disabled_samples;

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
public class hmmm extends OpMode{
    double LX, LY, RX, sensitivity = 0.5;
    DcMotor BL, FL, FR, BR, S1, S2;
    private DcMotor Shoulder;
    private Servo Elbow;
    private Servo Wrist;
    private Servo Intake;
    double w = 0;
    double e = w; //what does this do?
    @Override
    public void init(){
        BL = hardwareMap.dcMotor.get("back_left_motor");
        FL = hardwareMap.dcMotor.get("front_left_motor");
        FR = hardwareMap.dcMotor.get("front_right_motor");
        BR = hardwareMap.dcMotor.get("back_right_motor");
        S1 = hardwareMap.dcMotor.get("slide1");
        S2 = hardwareMap.dcMotor.get("slide2");
        S1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        S2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        S1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        S2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // This matches the DcMotors to the ones configured in the Control Hub
        S1.setDirection(FORWARD);
        S2.setDirection(FORWARD);
        FL.setDirection(REVERSE);
        Shoulder = hardwareMap.get(DcMotor.class, "Shoulder");
        Shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Elbow = hardwareMap.get(Servo.class, "Elbow");
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        Intake = hardwareMap.get(Servo.class, "Intake");
    }
    @Override
    public void loop(){
        telemetry.addLine("Controls:");
        LX = -gamepad1.left_stick_x * sensitivity;
        LY = -gamepad1.left_stick_y * sensitivity;
        RX = -gamepad1.right_stick_x * sensitivity;
        if (sensitivity < 1 && gamepad1.dpad_up){
            sensitivity += 0.01;
        }
        else if (sensitivity > 0 && gamepad1.dpad_down){
            sensitivity -= 0.01;
        }
        telemetry.addLine("Hold dpad up and dpad down to raise and lower sensitivity; Sensitivity: " + String.format("%.2g%n", sensitivity));
        BL.setPower(LY + LX - RX);
        FL.setPower(LY - LX - RX);
        FR.setPower(LY + LX + RX);
        BR.setPower(LY - LX + RX);
        telemetry.addLine("Hold left trigger and right trigger to raise and lower the slides; Slide position: " + S1.getCurrentPosition());
        S1.setPower((gamepad2.left_trigger - gamepad2.right_trigger) * -0.4);
        S2.setPower((gamepad2.left_trigger - gamepad2.right_trigger) * 0.4);
        //Control Shoulder

        Shoulder.setPower(gamepad2.left_stick_y);


        if(gamepad2.a) {
            Wrist.setPosition(1);
        } else {
            Wrist.setPosition(0);
        }

        if(gamepad2.x) {
            Elbow.setPosition(0);
        } else {
            Elbow.setPosition(0.7);
        }

        if(-gamepad2.right_stick_y != 0) {
            Intake.setPosition(-gamepad2.right_stick_y);
        } else {
            Intake.setPosition(0.5);
        }

        telemetry.addData("Shoulder Power", Shoulder.getPower());
        telemetry.addData("Elbow Position", Elbow.getPosition());
        telemetry.addData("Wrist Position", Wrist.getPosition());
        telemetry.addData("Intake Position", Intake.getPosition());
        telemetry.addLine("HI GUYS from Leo :)");
        telemetry.addLine("grgrgrfrfdrdrssrarar");
        telemetry.addLine("I don't think that I know wat I'm doing! AHHHHH");
        telemetry.addLine("bro just delete this Evan!");
        telemetry.update();
    }
}