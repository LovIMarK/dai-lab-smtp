package dai.lab.smtp;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        VictimContainer victims = new VictimContainer("lab-smtp\\resources\\victims.txt");
        
        for(String victim : victims.getVictims())
            System.out.println(victim);
    }
}
