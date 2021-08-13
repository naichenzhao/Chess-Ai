package gui;

import chessEngine.ChessBoard.Move;
import chessEngine.ChessPieces.Piece;
import com.google.common.primitives.Ints;
import gui.Table.MoveLog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakesPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static Color PANEL_COLOUR = Color.decode("0xFDFE6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakesPiecesPanel() {
        super(new BorderLayout());
        setBackground(Color.decode("0xFDF5E6"));
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOUR);
        this.southPanel.setBackground(PANEL_COLOUR);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {

        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<Piece>();

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
                        takenPiece.toString() + ".gif";
                image = ImageIO.read(new File(path));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
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
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.northPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        validate();


    }



}
