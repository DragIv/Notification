import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sound.sampled.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static ArrayList<String> arr = new ArrayList<>();
    public static String choose = "";
    public static File standartmusc = new File("src/Lo-fi.wav");

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{

        Scanner scanner = new Scanner(System.in);
        File musc1 = new File("src/Lo-fi.wav");
        File musc2 = new File("src/Clearly-You-Dont-Care.wav");
        File musc3 = new File("src/I-Feel-Upbeat.wav");

        String date,time,name = "";
        String change = "";

        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 1000);

        try {
            List<String> allLines = Files.readAllLines(Paths.get("src/file"));
            arr.addAll(allLines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!choose.equals("выход")){
            choose = scanner.next();
            if (choose.equals("добавить")) {
                System.out.println("Напишите дату для уведомления");
                date = scanner.next();
                System.out.println("Напишите время для уведомления");
                time = scanner.next();
                System.out.println("Напишите краткую информацию для уведомления");
                name = scanner.next();

                System.out.println("Хотите поменять мелодию уведомления? (да/нет)");
                change = scanner.next();
                switch (change) {
                    case ("да") -> {
                        System.out.println("1: Lo-fi, 2:Clearly, 3:Upbeat");
                        String chooseSound = scanner.next();
                        switch (chooseSound) {
                            case ("1") -> standartmusc = musc1;
                            case ("2") -> standartmusc = musc2;
                            case ("3") -> standartmusc = musc3;
                            default -> System.out.println("incorrect data");
                        }
                    }
                    case ("нет") -> standartmusc = musc1;
                    default -> System.out.println("incorrect data");
                }
                try (FileWriter writer = new FileWriter("src/file", true)) {
                    writer.write("\n"+date + " " + time + "\n" + name);
                    writer.flush();
                    arr.add(date + " " + time);
                    arr.add(name);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("Уведомление успрешно создано!");
            }

            if (choose.equals("узнать")){
                try(FileReader reader = new FileReader("src/file")) {
                    int c;
                    while((c=reader.read())!=-1){
                        System.out.print((char)c);
                    }
                    System.out.println();
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    static class SayHello extends TimerTask {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(standartmusc);
        Clip clip = AudioSystem.getClip();


        public SayHello() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            clip.open(audioStream);
        }
        public void run()  {
            Date datetime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).equals(formatter.format(datetime))){

                    try { audioStream = AudioSystem.getAudioInputStream(standartmusc);
                    } catch (UnsupportedAudioFileException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    try { clip = AudioSystem.getClip();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    try { clip.open(audioStream);
                    } catch (LineUnavailableException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    clip.setMicrosecondPosition(0);
                    clip.start();
                    System.out.println("Описание: " + arr.get(i+1));
                    System.out.println("Напишите стоп для выключения");
                }
            }
            if (choose.equals("стоп")){
                clip.stop();
                choose = "";
            }

        }
    }
}