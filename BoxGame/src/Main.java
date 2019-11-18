import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        final int depth = 4;
        final boolean movesRandomized = true;
        Game game = new Game(3, 3, depth, movesRandomized);
        Player winningPlayer = game.startGame();
        logWin(winningPlayer);
    }

    // TODO Implement BEAM search if in early game.

    private static void logWin(final Player winningPlayer) {
//        String os = System.getProperty("os.name");
//        System.out.println(os);
        String filePath = "/home/jeff/Desktop/Programming/Lines&Dots/winner_log";
        File file = new File(filePath);

        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String date = df.format(today);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(date);
            bw.write(" " + winningPlayer);
            bw.newLine();
            bw.close();
            fw.close();

            Scanner scan = new Scanner(new File(filePath));
            double ties = 0; double playerOneWins = 0; double playerTwoWins = 0;
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                Scanner scan2 = new Scanner(line);
                scan2.next(); scan2.next();
                String winner = scan2.next();
                switch (winner){
                    case "PLAYER_ONE":
                        playerOneWins++;
                        break;
                    case "PLAYER_TWO":
                        playerTwoWins++;
                        break;
                    case "NO_PLAYER":
                        ties++;
                        break;
                    default:
                        throw new RuntimeException("Error scanning file. Unknown value encountered.");
                }
            }
            scan.close();
            System.out.println("Human wins: " + playerOneWins);
            System.out.println("A.I. wins: " + playerTwoWins);
            System.out.println("Ties: " + ties);

            System.out.println();
            if(playerOneWins != 0 && playerTwoWins != 0){
                System.out.println("Average A.I. winning percentage: " + String.format("%.3f", playerTwoWins/playerOneWins));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
