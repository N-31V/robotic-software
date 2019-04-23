package test;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageClient {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        try {
            try {
            	final Random random = new Random();
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 9090); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedImage img = new BufferedImage(720, 540, BufferedImage.TYPE_INT_RGB);
                for(int i=0;i<720*540;i+=1) {
                	int r=random.nextInt(256);
                	int g=random.nextInt(256);
                	int b=random.nextInt(256);
                    img.setRGB(i % 720, i / 720, (0xff000000 | (r<<16) | (g<<8) | b));
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();
                clientSocket.getOutputStream().write(imageBytes);
                System.out.println("Вы что-то хотели сказать? Введите это здесь:");
                String word = reader.readLine(); // ждём пока клиент что-нибудь
                // не напишет в консоль
                out.write(word + "\n"); // отправляем сообщение на сервер
                out.flush();
                String serverWord = in.readLine(); // ждём, что скажет сервер
                System.out.println(serverWord); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
