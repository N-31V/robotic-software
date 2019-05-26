package test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageClient {
    private static int WIDTH = 160;
    private static int HEIGHT = 120;
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // ридер читающий с консоли
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    
    static byte[] createYUV(){
    	final Random random = new Random();
        byte[] frame = new byte[WIDTH*HEIGHT*2];
        for(int i=0; i<WIDTH*HEIGHT*2; i+=4) {
        	int r=random.nextInt(256);
        	int g=random.nextInt(256);
        	int b=random.nextInt(256);
            double Kr=0.299;
            double Kb=0.114;
            byte y = (byte) (Kr*r+(1-Kr-Kb)*g+Kb*b);
            byte u = (byte) (b-y);
            byte v = (byte) (r-y);
            frame[i] = y;
            frame[i+2] = y;
            frame[i+1] = u;
            frame[i+3] = v;                  
        }    	
		return frame;  
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
    public static void main(String[] args) {
        try {
            try {
            	final Random random = new Random();
                clientSocket = new Socket("localhost", 9090); 
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedImage img = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
                byte[] frame = createYUV();
                for(int i=0; i<WIDTH*HEIGHT*2; i+=4) {
                    int y1 = frame[i] & 0xFF;
                    int y2 = frame[i+2] & 0xFF;
                    int u = frame[i+1] & 0xFF;
                    int v = frame[i+3] & 0xFF;
                    int rgb1 = convertYUVtoARGB(y1,u,v);
                    int rgb2 = convertYUVtoARGB(y2,u,v);
                    img.setRGB((i % (WIDTH * 2)) / 2, i / (WIDTH * 2), rgb1);
                    img.setRGB((i % (WIDTH * 2)) / 2 + 1, i / (WIDTH * 2), rgb2);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();
                clientSocket.getOutputStream().write(imageBytes);
                String serverWord = in.readLine(); // ждём, что скажет сервер
                System.out.println(serverWord); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки               
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Клиент был закрыт...");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
