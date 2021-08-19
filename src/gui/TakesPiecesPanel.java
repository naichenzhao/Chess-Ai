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

    private final JScrollPane whitePanel;
    private final JScrollPane blackPanel;

    private static Color PANEL_COLOUR = new Color(253, 245, 230);
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(120, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakesPiecesPanel() {
//        super(new BorderLayout());
//        setBackground(PANEL_COLOUR);
//        setBorder(PANEL_BORDER);
//
//        this.whitePanel = new JScrollPane(new GridLayout(8, 2));
//        this.blackPanel = new JScrollPane(new GridLayout(8, 2));
//
//
//        this.whitePanel.setBackground(PANEL_COLOUR);
//        this.whitePanel.setBorder(BorderFactory.createTitledBorder("White"));
//        addBlankImage(whitePanel);
//
//        this.blackPanel.setBackground(PANEL_COLOUR);
//        this.blackPanel.setBorder(BorderFactory.createTitledBorder("Black"));
//        addBlankImage(blackPanel);
//
//        add(this.whitePanel, BorderLayout.WEST);
//        add(this.blackPanel, BorderLayout.EAST);
//
//        setPreferredSize(TAKEN_PIECES_DIMENSION);
//        this.setVisible(true);
    }

    public void redo(final MoveLog moveLog) {

        this.blackPanel.removeAll();
        this.whitePanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.getAlliance().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("fuck. you did something wrong");
                }
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<>() {

            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getType().getValue(), o2.getType().getValue());
            }
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>() {

            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getType().getValue(), o2.getType().getValue());
            }
        });

        for(final Piece takenPiece : whiteTakenPieces) {
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

        for(final Piece takenPiece : blackTakenPieces) {
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

        private static final String[] NAMES = {"White", "Black"};
        List<Piece> whiteTakenPieces;
        List<Piece> blackTakenPieces;

        public DataModel() {
            whiteTakenPieces = new ArrayList<>();
            blackTakenPieces = new ArrayList<>();

        }

        public void clear() {
            this.whiteTakenPieces.clear();
            this.blackTakenPieces.clear();
            setRowCount(0);
        }

        public List<Piece> getWhiteTakenPieces() {
            return ImmutableList.copyOf(whiteTakenPieces);
        }

        public List<Piece> getBlackTakenPieces() {
            return ImmutableList.copyOf(blackTakenPieces);
        }

        @Override
        public int getRowCount() {
            return Math.max(whiteTakenPieces.size(), blackTakenPieces.size());
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        public int readMoveLog(final MoveLog moveLog) {

            for(final Move move : moveLog.getMoves()) {
                if(move.isAttack()) {
                    final Piece takenPiece = move.getAttackedPiece();
                    if(takenPiece.getAlliance().isWhite()) {
                        whiteTakenPieces.add(takenPiece);
                    } else if(takenPiece.getAlliance().isBlack()) {
                        blackTakenPieces.add(takenPiece);
                    } else {
                        throw new RuntimeException("fuck. you did something wrong");
                    }
                }
            }

            Collections.sort(whiteTakenPieces, new Comparator<>() {

                @Override
                public int compare(Piece o1, Piece o2) {
                    return Ints.compare(o1.getType().getValue(), o2.getType().getValue());
                }
            });

            Collections.sort(blackTakenPieces, new Comparator<Piece>() {

                @Override
                public int compare(Piece o1, Piece o2) {
                    return Ints.compare(o1.getType().getValue(), o2.getType().getValue());
                }
            });



        }


    }



}
