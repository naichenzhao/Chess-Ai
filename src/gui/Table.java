package gui;

import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.BoardUtils;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Tile;
import chessEngine.ChessPieces.Piece;
import chessEngine.Player.MoveTransition;
import com.google.common.collect.Lists;

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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.*;

public class Table {

    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakesPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private final MoveLog moveLog;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(830, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);


    private final static String CWD = System.getProperty("user.dir");
    private static String pieceImagePath = getDefaultPath();

    private final static Color lightTile = new Color(232, 235, 239);
    private final static Color darkTile = new Color(125, 135, 150);
    private final static Color highlightTile = new Color(255, 6, 0);

    public Table() {
        this.gameFrame = new JFrame("chess");
        
        final JMenuBar tableMenuBar = createMenuBar();

        // Set all the values
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakesPiecesPanel();

        this.boardDirection = BoardDirection.NORMAL;
        highlightLegalMoves = true;

        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);


        this.gameFrame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferenceMenu());
        return tableMenuBar;

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

    private JMenu createPreferenceMenu() {
        final JMenu preferenceMenu = new JMenu("Preferences");

        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });


        final JCheckBoxMenuItem legalMoveHighlighter = new JCheckBoxMenuItem("Highlight Legal Moves", true);
        legalMoveHighlighter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighter.isSelected();
            }
        });


        preferenceMenu.add(flipBoardMenuItem);
        preferenceMenu.addSeparator();

        preferenceMenu.add(legalMoveHighlighter);
        preferenceMenu.add(pieceStyleSelectorMenu());

        return preferenceMenu;
    }


    public enum BoardDirection {

        NORMAL{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }

    private JMenu pieceStyleSelectorMenu() {

        final JMenu pieceMenu = new JMenu("style");

        final JMenuItem defaultStyle = new JMenuItem("Default");
        defaultStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = getDefaultPath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem fancyStyle = new JMenuItem("Fancy");
        fancyStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = getFancyPiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem fancy2Style = new JMenuItem("Fancy 2");
        fancy2Style.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = getFancy2PiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem holyWarriors = new JMenuItem("Holy Warriors");
        holyWarriors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = getHolyWarriorsPiecePath();
                boardPanel.drawBoard(chessBoard);
            }
        });

        final JMenuItem simple = new JMenuItem("Simple");
        simple.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieceImagePath = getSimplePiecePath();
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
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    public static class MoveLog {

        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        public boolean removeMove(Move move) {
            return this.moves.remove(move);
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
                                chessBoard = transition.getToBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }

                        invokeLater(() -> {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            takenPiecesPanel.redo(moveLog);
                            boardPanel.drawBoard(chessBoard);
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
            highlightLegalMoves(chessBoard);
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

        private void highlightLegalMoves( final Board board) {
            if(highlightLegalMoves) {
                for(final Move move : pieceLegalMoves(board)) {
                    if(move.getDestination() == this.tileID) {
                        if(board.getTile(this.tileID).isOccupied()) {
                            setBorder(BorderFactory.createLineBorder(highlightTile, 5));
                        } else {
                            try {
                                String path = getUtilsPath() + "green_dot.png";
                                add(new JLabel(new ImageIcon(ImageIO.read(new File(path)))));
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getAlliance() == board.getPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColour() {
            if(BoardUtils.FIRST_ROW[this.tileID] ||
               BoardUtils.THIRD_ROW[this.tileID] ||
               BoardUtils.FIFTH_ROW[this.tileID] ||
               BoardUtils.SEVENTH_ROW[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? lightTile : darkTile);
                setBorder(null);
            } else if(BoardUtils.SECOND_ROW[this.tileID] ||
                    BoardUtils.FOURTH_ROW[this.tileID] ||
                    BoardUtils.SIXTH_ROW[this.tileID] ||
                    BoardUtils.EIGHTH_ROW[this.tileID]) {
                setBackground(this.tileID % 2 != 0 ? lightTile : darkTile);
                setBorder(null);
            }
        }
    }


    private static String getSimplePiecePath() {
        return CWD + File.separator + "art" + File.separator + "simple" + File.separator;
    }

    private static String getFancyPiecePath() {
        return CWD + File.separator + "art" + File.separator + "fancy" + File.separator;
    }

    private static String getFancy2PiecePath() {
        return CWD + File.separator + "art" + File.separator + "fancy2" + File.separator;
    }

    private static String getHolyWarriorsPiecePath() {
        return CWD + File.separator + "art" + File.separator + "holywarriors" + File.separator;
    }

    private static String getDefaultPath() {
        return CWD + File.separator + "art" + File.separator + "default" + File.separator;
    }

    public static String getUtilsPath() {
        return CWD + File.separator + "art" + File.separator + ".utils" + File.separator;
    }

    public static String getImagePath() {
        return pieceImagePath;
    }


}
