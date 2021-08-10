package gui;

import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.BoardUtils;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Tile;
import chessEngine.ChessPieces.Piece;
import chessEngine.Player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);


    private final static String CWD = System.getProperty("user.dir");
    private static String pieceImagePath = CWD + getDefaultPath();

    private final static Color lightTile = new Color(232, 235, 239);
    private final static Color darkTile = new Color(125, 135, 150);

    public Table() {
        this.gameFrame = new JFrame("chess");
        
        final JMenuBar tableMenuBar = createMenuBar();

        // Set all the values
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.chessBoard = Board.createStandardBoard();

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);


        this.gameFrame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(pieceStyleSelector());
        return tableMenuBar;

    }

    private JMenu pieceStyleSelector() {

        final JMenu pieceMenu = new JMenu("style");

        final JMenuItem defaultStyle = new JMenuItem("Default");
        defaultStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = CWD + getDefaultPath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem fancyStyle = new JMenuItem("Fancy");
        fancyStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = CWD + getFancyPiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem fancy2Style = new JMenuItem("Fancy 2");
        fancy2Style.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = CWD + getFancy2PiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem holyWarriors = new JMenuItem("Holy Warriors");
        holyWarriors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = CWD + getHolyWarriorsPiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem simple = new JMenuItem("Simple");
        simple.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = CWD + getSimplePiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        pieceMenu.add(defaultStyle);
        pieceMenu.add(fancyStyle);
        pieceMenu.add(fancy2Style);
        pieceMenu.add(holyWarriors);
        pieceMenu.add(simple);

        return pieceMenu;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open your fucking file");
            }
        });

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(69);
            }
        });


        fileMenu.add(openPGN);
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        public BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();

        }

        public void drawBoard(final Board board) {
            removeAll();
            for(final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    private class TilePanel extends JPanel {

        private final int tileID;

        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignPieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if(isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if(isLeftMouseButton(e)) {
                        System.out.println("fuck");
                        if(sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getCoordinate(), destinationTile.getCoordinate());
                            final MoveTransition transition = chessBoard.getPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()) {

                                chessBoard = transition.getBoard();
                                // TODO: Add move to move log
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        public void drawTile(final Board board) {
            assignTileColour();
            assignPieceIcon(board);
            validate();
        }

        private void assignPieceIcon(final Board board) {
            this.removeAll();
            if(board.getTile(this.tileID).isOccupied()) {
                String path = pieceImagePath +
                        board.getTile(this.tileID).getPiece().getAlliance().toString().substring(0, 1) +
                        board.getTile(this.tileID).getPiece().toString() + ".gif";
                try {
                    final BufferedImage image = ImageIO.read(new File(path));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }

        private void assignTileColour() {
            if(BoardUtils.FIRST_ROW[this.tileID] ||
               BoardUtils.THIRD_ROW[this.tileID] ||
               BoardUtils.FIFTH_ROW[this.tileID] ||
               BoardUtils.SEVENTH_ROW[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? lightTile : darkTile);
            } else if(BoardUtils.SECOND_ROW[this.tileID] ||
                    BoardUtils.FOURTH_ROW[this.tileID] ||
                    BoardUtils.SIXTH_ROW[this.tileID] ||
                    BoardUtils.EIGHTH_ROW[this.tileID]) {
                setBackground(this.tileID % 2 != 0 ? lightTile : darkTile);
            }


        }


    }



    private static String getSimplePiecePath() {
        return File.separator + "art" + File.separator + "simple" + File.separator;
    }

    private static String getFancyPiecePath() {
        return File.separator + "art" + File.separator + "fancy" + File.separator;
    }

    private static String getFancy2PiecePath() {
        return File.separator + "art" + File.separator + "fancy2" + File.separator;
    }

    private static String getHolyWarriorsPiecePath() {
        return File.separator + "art" + File.separator + "holywarriors" + File.separator;
    }

    private static String getDefaultPath() {
        return File.separator + "art" + File.separator + "default" + File.separator;
    }


}
