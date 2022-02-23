package my_chat.chat_client;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class HistoryLogging {
    private static final int NUMBER_OF_HISTORY_LINES = 30;
    private File history;

    public HistoryLogging (String login) {
        String filename = "history/" + login + "_history" + ".txt";
        this.history = new File(filename);
        if (!history.exists()) {
            File filePath = new File("history/");
            filePath.mkdirs();
        }
    }

    public Stack<String> readMessagesFromHistory() {
        Stack<String> list = new Stack<>();
        if(history.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(history))){
                Stack<String> temp = new Stack<>();

                String str = reader.readLine();
                while (str!=null) {
                    temp.push(str);
                    str = reader.readLine();;
                }

                for (int i = 0; i < NUMBER_OF_HISTORY_LINES; i++) {
                    if(!temp.empty()) {
                        list.push(temp.pop());
                    } else {
                        break;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            list.push("History does not exist");
        }
        return list;
    }

    public void writeMessageToHistory(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(history, true))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
