package ru.nsu.fit.bolodya.networking.server;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Map;

class View extends JFrame {

    private JTable table;

    View(Server server) {
        super("Speed test");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 213);
        setResizable(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table = new JTable(11, 3);
        table.setDefaultRenderer(String.class, centerRenderer);
        table.updateUI();
        table.setEnabled(false);

        table.setValueAt("host", 0, 0);
        table.setValueAt("total", 0, 1);
        table.setValueAt("local", 0, 2);
        add(table);

        new Timer(1000, e -> {
            Map<String, Speed> map = server.getSpeedMap();
            fillTable(map);
        }).start();

        setVisible(true);
    }

    private void fillTable(Map<String, Speed> map) {
        for (int i = 1; i < table.getRowCount(); i++)
            for (int j = 0; j < table.getColumnCount(); j++)
                table.setValueAt("", i, j);

        int i = 1;
        for (Map.Entry<String, Speed> entry : map.entrySet()) {
            table.setValueAt(entry.getKey().substring(1, entry.getKey().length()), i, 0);
            table.setValueAt(formatSpeed(entry.getValue().getTotalSpeedInBytes()), i, 1);
            table.setValueAt(formatSpeed(entry.getValue().getLocalSpeedInBytes()), i, 2);
            if (++i >= 10)
                break;
        }
    }

    private String formatSpeed(double speedInBytes) {
        if (speedInBytes < 1024.)
            return String.format("%.3f b/s", speedInBytes);
        else if (speedInBytes < 1024. * 1024.)
            return String.format("%.3f Kb/s", speedInBytes / 1024.);
        else if (speedInBytes < 1024. * 1024. * 1024.)
            return String.format("%.3f Mb/s", speedInBytes / 1024. / 1024.);
        else
            return String.format("%.3f Gb/s", speedInBytes / 1024. / 1024. / 1024.);
    }
}
