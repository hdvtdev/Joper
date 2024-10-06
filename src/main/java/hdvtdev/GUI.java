package hdvtdev;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

@Deprecated
public class GUI {

    public static void common() {
        JFrame frame = new JFrame("DisBot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 450);
        frame.getContentPane().setBackground(Color.decode("#1a2426"));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setBackground(Color.decode("#1a2426"));
        frame.getContentPane().add(scrollPane);
        textArea.setBackground(Color.decode("#1a2426"));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));

        PrintStream printStream = new PrintStream(new InputListener(textArea));
        System.setOut(printStream);
        System.setErr(printStream);

        frame.setVisible(true);
        System.out.println("=============================================================================");
        System.out.println("\t[DisBot] [WARN] It is not recommended to use GUI mode");
        System.out.println("=============================================================================");
    }
}

class InputListener extends OutputStream {
    private final JTextArea textArea;
    private final StringBuilder currentLine = new StringBuilder();

    public InputListener(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        char c = (char) b;
        if (c == '\r') {
            handleCarriageReturn(); // Обработка символа \r
        } else {
            currentLine.append(c);
            if (c == '\n') {
                textArea.append(currentLine.toString());
                currentLine.setLength(0); // Очистка строки
            }
        }
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }

    private void handleCarriageReturn() {
        try {
            int start = textArea.getLineStartOffset(textArea.getLineCount() - 1);
            textArea.replaceRange(currentLine.toString(), start, textArea.getDocument().getLength());
            currentLine.setLength(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
