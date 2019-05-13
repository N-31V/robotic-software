package test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;
 
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.video.Video;

public class CameraClientTest {

    private static int WIDTH = 160;
    private static int HEIGHT = 120;
    private static int NUM_PIXELS = WIDTH * HEIGHT;
    private static int FRAME_SIZE = NUM_PIXELS * 2;
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
 
    public static void main(String[] args) throws IOException {
        EV3 ev3 = (EV3) BrickFinder.getLocal(); // подключение к локальному ev3
        Video video = ev3.getVideo(); // Получение экземпляра объекта Video
        video.open(WIDTH, HEIGHT); // Подготовка камеры к работе
        byte[] frame = video.createFrame(); //   Создание массива байтов для хранения кадра 
        clientSocket = new Socket(InetAddress.getByName("10.0.1.2"), 9090); // этой строкой мы запрашиваем
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB); // Объект для пересылаемого изображения
        //while (Button.ESCAPE.isUp()) {
	        video.grabFrame(frame); // Получение кадра
	        for(int i=0;i<FRAME_SIZE;i+=4) {
	            int y1 = frame[i] & 0xFF;
	            int y2 = frame[i+2] & 0xFF;
	            int u = frame[i+1] & 0xFF;
	            int v = frame[i+3] & 0xFF;
	            int rgb1 = convertYUVtoARGB(y1,u,v);
	            int rgb2 = convertYUVtoARGB(y2,u,v);
	            img.setRGB((i % (WIDTH * 2)) / 2, i / (WIDTH * 2), rgb1);
	            img.setRGB((i % (WIDTH * 2)) / 2 + 1, i / (WIDTH * 2), rgb2);
	        }
	        writeJpg(clientSocket.getOutputStream(), img); 
        //}
        // всё закрываем
        video.close();
        out.close();
        clientSocket.close();
    }
 
    private static void writeJpg(OutputStream stream, BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();
        stream.write(imageBytes);
        String serverWord = in.readLine(); // ждём, что скажет сервер
        LCD.drawString(serverWord, 0, 0);
        out.write("end\n");
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
