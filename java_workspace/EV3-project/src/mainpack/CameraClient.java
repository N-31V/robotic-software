package mainpack;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import lejos.hardware.ev3.EV3;
import lejos.hardware.video.Video;

public class CameraClient {

    private int WIDTH;
    private int HEIGHT;
    private int NUM_PIXELS;
    private int FRAME_SIZE;
    private String IP;
    private Video video;
    private byte[] frame;
    BufferedImage img;

    CameraClient(EV3 ev3, String ip) throws IOException{
    	this(ev3, 160, 120, ip);
    }
    
    CameraClient(EV3 ev3, int width, int height, String ip) throws IOException{
    	this.WIDTH = width;
    	this.HEIGHT = height;
    	this.NUM_PIXELS = this.WIDTH * this.HEIGHT;
    	this.FRAME_SIZE = this.NUM_PIXELS * 2;
    	this.IP=ip;
    	this.video = ev3.getVideo();
    	this.img = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
    	
    }
    
    protected void finalize() throws IOException {
    	this.video.close();
    }
    
    boolean SendPhoto(int port) throws UnknownHostException, IOException{
    	this.video.open(this.WIDTH, this.HEIGHT);
    	this.frame = video.createFrame();
        this.video.grabFrame(this.frame);
        this.video.close();
        for(int i=0;i<this.FRAME_SIZE;i+=4) {
            int y1 = this.frame[i] & 0xFF;
            int y2 = this.frame[i+2] & 0xFF;
            int u = this.frame[i+1] & 0xFF;
            int v = this.frame[i+3] & 0xFF;
            int rgb1 = convertYUVtoARGB(y1,u,v);
            int rgb2 = convertYUVtoARGB(y2,u,v);
            this.img.setRGB((i % (this.WIDTH * 2)) / 2, i / (this.WIDTH * 2), rgb1);
            this.img.setRGB((i % (this.WIDTH * 2)) / 2 + 1, i / (this.WIDTH * 2), rgb2);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(this.img, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();
        Socket clientSocket = new Socket(InetAddress.getByName(this.IP), port);
    	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientSocket.getOutputStream().write(imageBytes);
        String response = in.readLine(); 
        in.close();
        clientSocket.close();
        if(response.equals("true")){
        	return true;        	
        }
        else{
        	return false;
        }
    }
 
    private static int convertYUVtoARGB(int y, int u, int v) {
        int c = y - 16;
        int d = u - 128;
        int e = v - 128;
        int r = (298*c+409*e+128)/256;
        int g = (298*c-100*d-208*e+128)/256;
        int b = (298*c+516*d+128)/256;
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (r<<16) | (g<<8) | b;
    }
}
