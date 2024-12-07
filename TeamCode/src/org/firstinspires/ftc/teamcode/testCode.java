package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

public class testCode extends LinearOpMode{

    private DcMotor back_left_motor;
    private DcMotor back_right_motor;
    private DcMotor front_left_motor;
    private DcMotor front_right_motor;
    private DcMotor shoulder;
    private Servo claw;
    private Servo wrist;
    private DcMotor left_slide;
    private DcMotor right_slide;
    @Override
    public void runOpMode(){

        front_left_motor = hardwareMap.get(DcMotor.class,("front_left_motor"));
        front_right_motor = hardwareMap.get(DcMotor.class,("front_left_motor"));
        back_left_motor = hardwareMap.get(DcMotor.class,("front_left_motor"));
        back_right_motor = hardwareMap.get(DcMotor.class,("front_left_motor"));
        left_slide = hardwareMap.get(DcMotor.class, ("left_slide"));
        right_slide = hardwareMap.get(DcMotor.class, ("right_slide"));
        shoulder = hardwareMap.get(DcMotor.class,("shoulder"));
        wrist = hardwareMap.get(Servo.class, ("wrist"));
        claw = hardwareMap.get(Servo.class, ("claw"));

        front_left_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        right_slide.setDirection(DcMotorSimple.Direction.REVERSE);
        left_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        back_left_motor.setPower(.3);
        back_right_motor.setPower(.3);
        front_left_motor.setPower(.3);
        front_right_motor.setPower(.3);
        claw.setPosition(0);

        sleep(1000);

    }
}
