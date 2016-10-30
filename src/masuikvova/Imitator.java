package masuikvova;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.Port;

public class Imitator {
	private final String nircmdFilePath = "C:\\RemoteControl\\nircmd.exe";
	private boolean mode = true;
	private Robot robot = null;         
	private int mouse_possition_x;
	private int mouse_possition_y;
	private int mouse_step =10;
	private int volume_step =5000;
	
	public void createImitator(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		//System.out.println(mouse_possition_x +":" + mouse_possition_y);		
	}
	public void setComand(int command){
		if(mode){//key mode
			switch(command){
				case 1:
					Mute();
					break;
				case 2:
					MaxVolume();
					break;
				case 3:
					VolumeUp();
					break;
				case 4:
					ShutDownWindowsHalfHour();
					break;
				case 5:
					VKSpace();
					break;
				case 6:
					VolumeDown();
					break;
				case 7:
					WinD();
					break;
				case 8:
					LockWindows();
					break;
				case 9:
					VKEsc();
					break;
				case 10: //*
					SleepWindows();
					break;
				case 11: //0
					mode = !mode; //change mode false - key mode true - mousemode
					break;
				case 12: //#
					ShutDownWindows();
					break;
				case 13: //up
					VKUp();
					break;
				case 14: //left
					VKLeft();
					break;
				case 15: //ok
					VKEnter();
					break;
				case 16: //right
					VKRight();
					break;
				case 17: //down
					VKDown();
					break;				
			}			
		}
		else{//mouse mode
			switch(command){
				case 1:
					Mute();
					break;
				case 2:
					MaxVolume();
					break;
				case 3:
					VolumeUp();
					break;
				case 4:
					ShutDownWindowsHalfHour();
					break;
				case 5:
					VKSpace();
					break;
				case 6:
					VolumeDown();
					break;
				case 7:
					WinD();
					break;
				case 8:
					LockWindows();
					break;
				case 9:
					VKEsc();
					break;
				case 10: //*
					SleepWindows();
					break;
				case 11: //0
					mode = !mode; //change mode false - key mode true - mousemode
					break;
				case 12: //#
					ShutDownWindows();
					break;
				case 13: //up
					MouseUp();
					break;
				case 14: //left
					MouseLeft();
					break;
				case 15: //ok
					MouseLeftClick();
					break;
				case 16: //right
					MouseRight();
					break;
				case 17: //down
					MouseDown();
					break;			
			}			
		}		
	}	
	private void MouseLeft(){
		getCurrentMousePosition();
		mouse_possition_x -=mouse_step;
		robot.mouseMove(mouse_possition_x, mouse_possition_y);
	}	
	private void MouseRight(){
		getCurrentMousePosition();
		mouse_possition_x +=mouse_step;
		robot.mouseMove(mouse_possition_x, mouse_possition_y);
	}	
	private void MouseUp(){
		getCurrentMousePosition();
		mouse_possition_y -=mouse_step;
		robot.mouseMove(mouse_possition_x, mouse_possition_y);
	}	
	private void MouseDown(){
		getCurrentMousePosition();
		mouse_possition_y +=mouse_step;
		robot.mouseMove(mouse_possition_x, mouse_possition_y);
	}	
	private void MouseLeftClick(){
		getCurrentMousePosition();
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}	
	private void VolumeUp(){
		try {
			Process process = Runtime.getRuntime().exec(nircmdFilePath + " changesysvolume " + volume_step);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
	private void VolumeDown(){
		
		int avolume = volume_step * (-1);
		try {
			Process process = Runtime.getRuntime().exec(nircmdFilePath + " changesysvolume " + avolume);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
	private void MaxVolume(){
		try {
			Process process = Runtime.getRuntime().exec(nircmdFilePath + " nircmd.exe setsysvolume 65535");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}	
	private void Mute(){
		try {
			Process process = Runtime.getRuntime().exec(nircmdFilePath + " nircmd.exe mutesysvolume 2");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	private void ShutDownWindows(){
		// Shutdown after specific time (here 60 seconds)
		Runtime r=Runtime.getRuntime();
		try {
			r.exec("shutdown -s -t 60");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void ShutDownWindowsHalfHour(){
		// Shutdown after specific time (here 60 seconds)
		Runtime r=Runtime.getRuntime();
		try {
			r.exec("shutdown -s -t 3600");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void SleepWindows(){
		try {
			Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	private void LockWindows(){
		try {
			Runtime.getRuntime().exec("Rundll32.exe user32.dll,LockWorkStation");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void VKSpace(){
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
	}
	private void VKUp(){
		robot.keyPress(KeyEvent.VK_UP);
		robot.keyRelease(KeyEvent.VK_UP);
	}
	private void VKDown(){
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
	}
	private void VKLeft(){
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_LEFT);
	}
	private void VKRight(){
		robot.keyPress(KeyEvent.VK_RIGHT);
		robot.keyRelease(KeyEvent.VK_RIGHT);
	}
	private void VKEsc(){
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);
	}
	private void VKEnter(){
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	private void WinD(){
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyPress(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
	}
	public void getCurrentMousePosition(){
		PointerInfo pointerInfo = MouseInfo.getPointerInfo();
		Point point = pointerInfo.getLocation();
		mouse_possition_x = (int) point.getX();
		mouse_possition_y = (int) point.getY();
	}
	
	public static void main(String [] args){
		Imitator im = new Imitator();
		im.createImitator();
		//im.WinD();	
	}
}
