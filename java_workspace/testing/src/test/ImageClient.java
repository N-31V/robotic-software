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

    public static void main(String[] args) {
        try {
            try {
            	final Random random = new Random();
                clientSocket = new Socket("localhost", 9090); 
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                for(int i=0;i<WIDTH*HEIGHT;i+=1) {
                	int r=random.nextInt(256);
                	int g=random.nextInt(256);
                	int b=random.nextInt(256);
                    img.setRGB(i % WIDTH, i / WIDTH, (0xff000000 | (r<<16) | (g<<8) | b));
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();
                clientSocket.getOutputStream().write(imageBytes);
                //System.out.println("Вы что-то хотели сказать? Введите это здесь:");
                //String word = reader.readLine(); // ждём пока клиент что-нибудь не напишет в консоль
                //out.write(word+"\n"); // отправляем сообщение на сервер
                //out.flush();
                String serverWord = in.readLine(); // ждём, что скажет сервер
                System.out.println(serverWord); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки               
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Клиент был закрыт...");
            }
            try {
                clientSocket = new Socket("localhost", 9090); 
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedImage img2 = ImageIO.read(new File("/home/n-31v/Изображения/img2.jpg"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img2, "jpg", baos);
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
            try {
            	final Random random = new Random();
                clientSocket = new Socket("localhost", 9090); 
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                out.write("end"); // отправляем сообщение на сервер
                out.flush();
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
