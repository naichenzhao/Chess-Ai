package gui;

import chessEngine.ChessBoard.Move;
import chessEngine.ChessPieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import gui.Table.MoveLog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakesPiecesPanel extends JPanel {

    private final DataModel model;
    private final JScrollPane scrollPane;

    private static Color PANEL_COLOUR = new Color(253, 245, 230);
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(120, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakesPiecesPanel() {
        super(new BorderLayout());
        setBackground(PANEL_COLOUR);
        setBorder(PANEL_BORDER);
        model = new DataModel();

        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);

        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(TAKEN_PIECES_DIMENSION);

        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void redo(final MoveLog moveLog) {

        model.readMoveLog(moveLog);

        for(final Piece takenPiece : model.getWhiteTakenPieces()) {
            final BufferedImage image;
            try {
                String path = Table.getImagePath() +
                        takenPiece.getAlliance().toString().substring(0, 1) +
                        takenPiece + ".gif";
                image = ImageIO.read(new File(path));
                final JLabel imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(
                        image.getWidth() - 15, image.getWidth() - 15, Image.SCALE_SMOOTH)));
                System.out.println(image.getWidth() - 15);
                imageLabel.setBackground(PANEL_COLOUR);
                this.blackPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        for(final Piece takenPiece : model.getBlackTakenPieces()) {
            final BufferedImage image;
            try {
                String path = Table.getImagePath() +
                        takenPiece.getAlliance().toString().substring(0, 1) +
                        takenPiece.toString() + ".gif";
                image = ImageIO.read(new File(path));
                final JLabel imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(
                        image.getWidth() - 15, image.getWidth() - 15, Image.SCALE_SMOOTH)));
                imageLabel.setBackground(PANEL_COLOUR);
                this.whitePanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        validate();


    }



    public static void addBlankImage(JPanel panel) {
        JLabel imageLabel = null;
        try {
            String path = Table.getUtilsPath() + "Blank.gif";
            final BufferedImage image;
            image = ImageIO.read(new File(path));
            imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(45, 1, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel.add(imageLabel);

    }

    private static class DataModel extends DefaultTableModel {

        private final List<Row> values;
        private static final String[] NAMES = {"White", "Black"};

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int col) {
            final Row currentRow = this.values.get(row);
            if(col == 0) {
                return currentRow.getWhiteMove();
            } else if(col == 1) {
                return currentRow.getBlackMove();
            }
            return null;
        }

        @Override
        public void setValueAt(final Object value, final int row, final int col) {
            final Row currentRow;
            if (this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }

            if (col == 0) {
                currentRow.setWhiteMove((ImageIcon) value);
                fireTableRowsInserted(row, col);
            }else if(col == 1) {
                currentRow.setBlackMove((ImageIcon) value);
                fireTableCellUpdated(row, col);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int column) {
            return NAMES[column];
        }

    }

    private static class Row {

        private ImageIcon whiteMove;
        private ImageIcon blackMove;

        public ImageIcon getWhiteMove() {
            return this.whiteMove;
        }

        public ImageIcon getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(final ImageIcon move) {
            this.whiteMove = move;
        }

        public void setBlackMove(final ImageIcon move) {
            this.blackMove = move;
        }

    }



}
