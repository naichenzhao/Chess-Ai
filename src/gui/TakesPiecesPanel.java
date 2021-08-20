package gui;

import chessEngine.ChessBoard.Move;
import chessEngine.ChessPieces.Piece;
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
        table.setRowHeight(45);
        this.scrollPane = new JScrollPane(table);

        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(TAKEN_PIECES_DIMENSION);

        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void redo(final MoveLog moveLog) {

        model.clear();

        int blackRow = 0;
        int whiteRow = 0;

        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();

                if(takenPiece.getAlliance().isWhite()) {
                    model.setValueAt(takenPiece, whiteRow, 0);
                    whiteRow++;
                } else if(takenPiece.getAlliance().isBlack()) {
                    model.setValueAt(takenPiece, blackRow, 1);
                    blackRow++;
                } else {
                    throw new RuntimeException("fuck. you did something wrong");
                }
            }
        }

    }

    private static class DataModel extends DefaultTableModel {

        private Column[] values;
        private static final String[] NAMES = {"White", "Black"};

        DataModel() {
            this.values = new Column[]{new Column(), new Column()};;
        }

        public void clear() {
            this.values = new Column[]{new Column(), new Column()};;
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return Math.max(this.values[0].getSize(), this.values[1].getSize());
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int col) {
            final Column currentCol = this.values[col];
            return currentCol.getValue(row);
        }

        @Override
        public void setValueAt(final Object value, final int row, final int col) {
            final Column currentCol = this.values[col];
            currentCol.addValue(row, (Piece) value);
            fireTableRowsInserted(row, col);
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return ImageIcon.class;
        }

        @Override
        public String getColumnName(final int column) {
            return NAMES[column];
        }

    }

    private static class Column {

        private List<Piece> pieces;

        private ImageIcon blankImage;

        public Column() {
            pieces = new ArrayList<>();
            try {
                String path = Table.getUtilsPath() + "Blank.gif";
                final BufferedImage image;
                image = ImageIO.read(new File(path));
                blankImage = new ImageIcon(image.getScaledInstance(45, 1, Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void addValue(int index, Piece value) {
            if(index >= pieces.size()) {
                pieces.add(value);
            }
            pieces.set(index, value);

            Collections.sort(pieces, new Comparator<>() {
                @Override
                public int compare(Piece o1, Piece o2) {
                    return Ints.compare(o1.getType().getValue(), o2.getType().getValue());
                }

            });
        }

        public ImageIcon getValue(int index) {
            if(index < pieces.size()) {
                Piece currentPiece = pieces.get(index);
                BufferedImage image;
                try {
                    String path = Table.getImagePath() +
                            currentPiece.getAlliance().toString().substring(0, 1) +
                            currentPiece + ".gif";
                    image = ImageIO.read(new File(path));
                    final ImageIcon imageLabel = new ImageIcon(image.getScaledInstance(
                            image.getWidth() - 15, image.getWidth() - 15, Image.SCALE_SMOOTH));
                    return imageLabel;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return blankImage;
        }

        public int getSize() {
            return pieces.size();
        }

        @Override
        public String toString() {
            return this.pieces.toString();
        }

    }



}
